package org.libconfig.test;

import static org.testng.Assert.*;

import org.libconfig.Config;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
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
		
		Setting arraySetting = config.addSetting("SOME_SETTINGS_ARRAY", Type.ARRAY);
		Setting listSetting = config.addSetting("SOME_SETTINGS_LIST", Type.LIST);
		for (int i = 0; i < 10; i++) {
			arraySetting.addSetting(i);
			listSetting.addSetting(i);
		}
		
		Setting arrayValueSetting = arraySetting.lookup(0);
		
		assertEquals(arraySetting.getPath(), "SOME_SETTINGS_ARRAY");
		assertEquals(listSetting.getPath(), "SOME_SETTINGS_LIST");
		assertEquals(arrayValueSetting.getPath(), "SOME_SETTINGS_LIST");
		
		assertNotNull(arraySetting.lookup(0));
	}
	
	
	private Config createConfig() {
		Config config = new Config();
		config.addSetting("SOME_SETTINGS_1", true);

		Setting setting = config.addSetting("SOME_SETTINGS_0", 1);
		setting.addSetting("CHILD1", 1);
		Setting child2Setting = setting.addSetting("CHILD2", 2);
		child2Setting.addSetting("CHIL2_CHILD_1", "SomeValue1"); 
		child2Setting.addSetting("CHIL2_CHILD_2", "SomeValue2");

		return config;
	}
	
}
