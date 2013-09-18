package org.libconfig.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.libconfig.Config;
import org.libconfig.Setting;
import org.testng.annotations.Test;

public class ConfigLookupAndPathsTest {

	@Test
	public void testLookUp() {
		
		Config config = createConfig();
		
		Setting rootSetting = config.lookup("SOME_SETTINGS_1");
		Setting longPathSetting = config.lookup("SOME_SETTINGS_0.CHILD2.CHIL2_CHILD_1");
		
		assertNotNull(rootSetting);
		assertNotNull(longPathSetting);

		assertEquals(rootSetting.getName(), "SOME_SETTINGS_1");
		assertEquals(longPathSetting.getName(), "CHIL2_CHILD_1");
	}
	
	@Test
	public void testPaths() {
		Config config = createConfig();
		
		Setting rootSetting = config.lookup("SOME_SETTINGS_1");
		Setting longPathSetting = config.lookup("SOME_SETTINGS_0.CHILD2.CHIL2_CHILD_1");
		
		assertEquals(rootSetting.getPath(), "SOME_SETTINGS_1");
		assertEquals(longPathSetting.getPath(), "SOME_SETTINGS_0.CHILD2.CHIL2_CHILD_1");
	}

	@Test
	public void testArrayAndListPaths() {
		Config config = createConfig();
		
		Setting arraySetting = config.addArray("SOME_SETTINGS_ARRAY", 1, 2, 2, 3, 4, 6, 5, 6, 6);
		Setting listSetting = config.addList("SOME_SETTINGS_LIST");
		listSetting.addArray(1,2,3);

		List<Setting> listSettings = config.lookup("SOME_SETTINGS_LIST").getValue();
		Setting[] arraySettings = listSettings.get(0).getValue();
		System.out.println(arraySettings[0].getPath());
		
//		List<Integer> arraySettings = arraySetting.getValue();
// 		
//		Integer setting = arraySettings.get(0);
//		System.out.println(setting.getType());
		
		assertEquals(arraySetting.getPath(), "SOME_SETTINGS_ARRAY");
		assertEquals(listSetting.getPath(), "SOME_SETTINGS_LIST");
	}
	
	
	private Config createConfig() {
		Config config = new Config();
		config.addScalar("SOME_SETTINGS_1", true);

		Setting setting = config.addGroup("SOME_SETTINGS_0");
		Setting child2Setting = setting.addGroup("CHILD2");
		child2Setting.addScalar("CHIL2_CHILD_1", "SomeValue1"); 
		child2Setting.addScalar("CHIL2_CHILD_2", "SomeValue2");

		return config;
	}
	
}
