package org.libconfig.test;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.libconfig.Config;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
import org.testng.annotations.Test;

public class ConfigValuesSettingsTest {

	@Test
	public void testScalarTypes() {
		
		Config config = new Config();
		
		config.addScalar("SOME_STR", "str");
		config.addScalar("SOME_INT", 1);
		config.addScalar("SOME_FLOAT", 1.2d);
		config.addScalar("SOME_BOOL", true);
		
		assertEquals(config.lookup("SOME_STR").getType(), Type.STRING);
		assertEquals(config.lookup("SOME_INT").getType(), Type.INTEGER);
		assertEquals(config.lookup("SOME_FLOAT").getType(), Type.FLOAT);
		assertEquals(config.lookup("SOME_BOOL").getType(), Type.BOOLEAN);
		
		assertEquals(config.lookup("SOME_STR").getValue(), "str");
		assertEquals(config.lookup("SOME_INT").getValue(), 1);
		assertEquals(config.lookup("SOME_FLOAT").getValue(), 1.2d);
		assertEquals(config.lookup("SOME_BOOL").getValue(), true);
		
	}

	@Test
	public void testArray() {
		
		Config config = new Config();
		
		config.addArray("SOME_STR_ARRAY", new String[] { "str1", "str2", "str3", "str4" });
		config.addArray("SOME_INT_ARRAY", new Integer[] { 1 });
		config.addArray("SOME_FLOAT_ARRAY", new Double[] { 1.2d, 1.3d });
		config.addArray("SOME_BOOL_ARRAY", new Boolean[] { true, false });
		
		assertEquals(config.lookup("SOME_STR_ARRAY").getType(), Type.ARRAY);
		assertEquals(config.lookup("SOME_INT_ARRAY").getType(), Type.ARRAY);
		assertEquals(config.lookup("SOME_FLOAT_ARRAY").getType(), Type.ARRAY);
		assertEquals(config.lookup("SOME_BOOL_ARRAY").getType(), Type.ARRAY);
		
		checkArrayValuesType(config.lookup("SOME_STR_ARRAY"), Type.STRING);
		checkArrayValuesType(config.lookup("SOME_INT_ARRAY"), Type.INTEGER);
		checkArrayValuesType(config.lookup("SOME_FLOAT_ARRAY"), Type.FLOAT);
		checkArrayValuesType(config.lookup("SOME_BOOL_ARRAY"), Type.BOOLEAN);

		Setting[] values = config.lookup("SOME_STR_ARRAY").getValue();
		assertEquals(values[0].getValue(), "str1");
		assertEquals(values[1].getValue(), "str2");
		assertEquals(values[2].getValue(), "str3");
		assertEquals(values[3].getValue(), "str4");
		
		values = config.lookup("SOME_INT_ARRAY").getValue();
		assertEquals(values[0].getValue(), 1);
		
		values = config.lookup("SOME_FLOAT_ARRAY").getValue();
		assertEquals(values[0].getValue(), 1.2d);
		assertEquals(values[1].getValue(), 1.3d);
		
		values = config.lookup("SOME_BOOL_ARRAY").getValue();
		assertEquals(values[0].getValue(), true);
		assertEquals(values[1].getValue(), false);

	}
	
	@Test
	public void testList() {
		Config config = new Config();
		
		Setting listSetting = config.addList("SOME_LIST");
		listSetting.addArray(new Boolean[] { true, false });
		listSetting.addArray(new String[] { "a", "b" });
		listSetting.addGroup().addScalar("gg", 1).addScalar("hh", 2);

		assertEquals(listSetting.getType(), Type.LIST);
		
		List<Setting> settings = listSetting.getValue();
		
		assertEquals(settings.get(0).getType(), Type.ARRAY);
		assertEquals(settings.get(1).getType(), Type.ARRAY);
		assertEquals(settings.get(2).getType(), Type.GROUP);

		Setting arraySetting = settings.get(0);
		checkArrayValuesType(arraySetting, Type.BOOLEAN);

		Setting[] arraySettings = arraySetting.getValue();
		assertEquals(arraySettings[0].getValue(), true);
		assertEquals(arraySettings[1].getValue(), false);
		
		Setting groupSetting = settings.get(2);
		
		List<Setting> groupSettings = groupSetting.getValue();
		assertEquals(groupSettings.get(0).getType(), Type.INTEGER);
		assertEquals(groupSettings.get(1).getType(), Type.INTEGER);

		assertEquals(groupSettings.get(0).getValue(), 1);
		assertEquals(groupSettings.get(1).getValue(), 2);
 	}
	
	private void checkArrayValuesType(Setting arraySetting, Type type) {
		Setting[] settings = arraySetting.getValue();
		for (Setting setting : settings) {
			assertEquals(setting.getType(), type);
		}
	}
	
}
