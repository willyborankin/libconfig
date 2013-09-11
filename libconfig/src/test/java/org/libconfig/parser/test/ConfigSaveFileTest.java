package org.libconfig.parser.test;

import java.io.IOException;

import org.libconfig.Config;
import org.libconfig.Config.Format;
import org.libconfig.Config.NumberFormat;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
import org.libconfig.parser.ParseException;
import org.testng.annotations.Test;

public class ConfigSaveFileTest {

	@Test
	public void saveRightStructure() throws IOException, ParseException {
		Config configuration = new Config();
		configuration.setFormat(Format.COLON);
		configuration.setNumberFormat(NumberFormat.HEX);
		
		configuration.addSetting("SCALAR_SETTING_0", 1340);
		configuration.addSetting("SCALAR_SETTING_1", false);
		configuration.addSetting("SCALAR_SETTING_2", 123e-100);
		configuration.addSetting("SCALAR_SETTING_3","asdsadsadsds");

		Setting groupSetting = configuration.addSetting("GROUP_SETTING_1", Type.GROUP);
		
		groupSetting.addSetting("ff", "ff");
		groupSetting.addSetting("gg", 1);
		groupSetting.addSetting("hh", 2.5d);
		groupSetting.addSetting("ee", true);

		groupSetting.addSetting("INNER_GROUP_ARRAY_1", Type.ARRAY);
		
		Setting innerGroupInGroup = groupSetting.addSetting("INNER_GROUP_GROUP_1", Type.GROUP);
		innerGroupInGroup.addSetting("jj", true);
		innerGroupInGroup.addSetting("kk", 1);
		
		Setting innerGroupInGroup2 = innerGroupInGroup.addSetting("INNER_GROUP_GROUP_2", Type.GROUP);
		innerGroupInGroup2.addSetting("jj1", false);
		innerGroupInGroup2.addSetting("kk1", 2);
		
		Setting innerListInGroup = innerGroupInGroup.addSetting("INNER_LIST_GROUP_1", Type.LIST);
		for (int i = 0; i < 10; i++) {
			innerListInGroup.addSetting(i);
		}
		
		Setting innerArrayInGroup = innerGroupInGroup.addSetting("INNER_ARRAY_GROUP_1", Type.ARRAY);
		for (int i = 0; i < 10; i++) {
			innerArrayInGroup.addSetting(i);
		}
		
		Setting listSetting = configuration.addSetting("SIMPLE_LIST", Type.LIST);
		for (int i = 0; i < 10; i++) {
			listSetting.addSetting(i);
		}
		
		Setting arraySetting = configuration.addSetting("SIMPLE_ARRAY", Type.ARRAY);
		for (int i = 0; i < 10; i++) {
			arraySetting.addSetting(i);
		}
//		File file = new File("11.cgf");
		configuration.write(System.out);
//		parse back
//		ConfigParser configParser = new ConfigParser(new FileInputStream(file));
//		Config config = configParser.buildConfiguration();
//		Setting rootSetting = config.getRoot();
	}
	
}
