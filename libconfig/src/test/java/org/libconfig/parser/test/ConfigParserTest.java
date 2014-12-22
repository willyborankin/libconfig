package org.libconfig.parser.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.InputStream;
import java.util.List;

import org.libconfig.Config;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
import org.libconfig.parser.ConfigParser;
import org.libconfig.parser.ParseException;
import org.testng.annotations.Test;

public class ConfigParserTest {

	@Test
	public void testScalar() throws ParseException {
		
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("scalar-values.conf");
		ConfigParser configParser = new ConfigParser(in);
		Config config = configParser.buildConfiguration();

		Setting intSetting = config.lookup("someConfig");
		Setting strSetting = config.lookup("someConfig1");
		
		Setting blnTrueSetting = config.lookup("someConfig2");
		Setting blnFalseSetting = config.lookup("someConfig3");
		
		Setting hexIntSetting = config.lookup("someConfig4");

		Setting dbl1Setting = config.lookup("someConfig5");
		Setting dbl2Setting = config.lookup("someConfig6");
		Setting dbl3Setting = config.lookup("someConfig7");

		checkInt(intSetting, 123);
		checkInt(hexIntSetting, 123);

		checkBoolean(blnTrueSetting, true);
		checkBoolean(blnFalseSetting, false);

		checkFloat(dbl1Setting, 123.456d);
		checkFloat(dbl2Setting, .456d);
		checkFloat(dbl3Setting, 123e-34d);
		
		assertNotNull(strSetting);
		assertEquals(strSetting.getType(), Type.STRING);
		assertEquals(strSetting.getValue(), "asdasdasd");
	}

	
	@Test
	public void testArray() throws ParseException {
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("complex-values.conf");
		ConfigParser configParser = new ConfigParser(in);
		Config config = configParser.buildConfiguration();

		Setting arraySetting = config.lookup("intArray");
		checkSetting(arraySetting, Type.ARRAY);

		Setting[] aSettings = arraySetting.getValue();
		checkScalarValuesAndTypes(aSettings, new Integer[] { 1, 2, 3, 4, 5, 6, 7 });
	}
	
	@Test
	public void testGroup() throws ParseException {
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("complex-values.conf");
		ConfigParser configParser = new ConfigParser(in);
		Config config = configParser.buildConfiguration();

		Setting simpleGroupSetting = config.lookup("simpleGroup");
		checkSetting(simpleGroupSetting, Type.GROUP);

		Setting complexGroupSetting = config.lookup("complexGroup");
		checkSetting(complexGroupSetting, Type.GROUP);
		
		List<Setting> lSettings = simpleGroupSetting.getValue();
		checkScalarValuesAndTypes(
				lSettings.toArray(new Setting[lSettings.size()]), 
				new String[] { "ff", "gg", "hh", "jj", "kk" }, 
				new Object[] { "asd", 1, 0.3d, true, new Integer[] { 1, 2, 3, 4, 5 } });
		
		lSettings = complexGroupSetting.getValue();
	}
	
	@Test
	public void testList() throws ParseException {
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("complex-values.conf");
		ConfigParser configParser = new ConfigParser(in);
		Config config = configParser.buildConfiguration();
		
		Setting simpleList = config.lookup("simpleList");
		checkSetting(simpleList, Type.LIST);

		Setting complexList = config.lookup("complexList");
		checkSetting(complexList, Type.LIST);
		
		
		List<Setting> lSettings = simpleList.getValue();
		checkScalarValuesAndTypes(lSettings.toArray(new Setting[lSettings.size()]), new Integer[] { 1, 2, 3, 4 });		

		lSettings = complexList.getValue();
		assertNull(lSettings.get(0).getName());
		assertNull(lSettings.get(1).getName());
		assertNull(lSettings.get(2).getName());

		assertEquals(lSettings.get(0).getType(), Type.LIST);
		assertEquals(lSettings.get(1).getType(), Type.ARRAY);
		assertEquals(lSettings.get(2).getType(), Type.GROUP);

//		aSettings = lSettings.get(1).getValue();
//		checkScalarValuesAndTypes(aSettings, 5, 6, 7, 8);
//		lSettings = lSettings.get(0).getValue();
//		checkScalarValuesAndTypes(lSettings.toArray(new Setting[lSettings.size()]), 1, 2, 3, 4);
	}
	
	private <T> void checkScalarValuesAndTypes(Setting[] settings, T[] expectedValue) {
		checkScalarValuesAndTypes(settings, null, expectedValue);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void checkScalarValuesAndTypes(Setting[] settings, String[] names, T[] expectedValue) {
		assertEquals(settings.length, expectedValue.length);
		if (names != null)
			assertEquals(settings.length, names.length);
		int i = 0;
		for (Setting setting : settings) {
			if (names == null) assertNull(setting.getName());
			else assertEquals(setting.getName(), names[i]);
			if (setting.getType() == Type.ARRAY) {
				Setting[] aSettings = setting.getValue();
				checkScalarValuesAndTypes(aSettings, (T[]) expectedValue[i]);
			} else {
				assertEquals(setting.getType(), setting.getConfig().resolveType(expectedValue[i]));
				assertEquals(setting.getValue(), expectedValue[i]);
			}
			i++;
		}
	}
	
	private void checkInt(Setting setting, int expectedValue) {
		checkSetting(setting, Type.INTEGER);
		assertEquals(setting.getValue(), expectedValue);
	}

	private void checkBoolean(Setting setting, boolean expectedValue) {
		checkSetting(setting, Type.BOOLEAN);
		assertEquals(setting.getValue(), expectedValue);
	}
	
	private void checkFloat(Setting setting, double expectedValue) {
		checkSetting(setting, Type.FLOAT);
		assertEquals(setting.getValue(), expectedValue);
	}
	
	private void checkSetting(Setting setting, Type type) {
		assertNotNull(setting);
		assertEquals(setting.getType(), type);
	}
	
	@Test
	public void testOpenFacesConfig() throws ParseException {
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("opencv.conf");
		ConfigParser configParser = new ConfigParser(in);
		Config config = configParser.buildConfiguration();
		
		Setting rootSetting = config.lookup("faceDetectionConfig");
		Setting dptSetting = rootSetting.lookup("dataProviderType");
		
		System.err.println(rootSetting);
		System.err.println(dptSetting);
	}
	
}
