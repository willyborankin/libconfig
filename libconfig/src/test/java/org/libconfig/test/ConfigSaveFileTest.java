package org.libconfig.test;

import java.io.File;
import java.io.IOException;

import org.libconfig.Config;
import org.libconfig.ConfigOutputter;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
import org.libconfig.parser.ParseException;
import org.testng.annotations.Test;

public class ConfigSaveFileTest {

	@Test
	public void saveRightStructure() throws IOException, ParseException {
		Config config = new Config();
		
		config.addSetting("SCALAR_SETTING_0", 1340);
		config.addSetting("SCALAR_SETTING_1", false);
		config.addSetting("SCALAR_SETTING_2", 123e-100);
		config.addSetting("SCALAR_SETTING_3","asdsadsadsds");

		Setting groupSetting = config.addSetting("GROUP_SETTING_1", Type.GROUP);
		
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
		
		Setting listSetting = config.addSetting("SIMPLE_LIST", Type.LIST);
		for (int i = 0; i < 10; i++) {
			listSetting.addSetting(i);
		}
		
		Setting arraySetting = config.addSetting("SIMPLE_ARRAY", Type.ARRAY);
		for (int i = 0; i < 10; i++) {
			arraySetting.addSetting(i);
		}
		
		File file = new File("11.cgf");
		ConfigOutputter configOutputter = new ConfigOutputter();
		configOutputter.output(config, file);
		
//		parse back
//		ConfigParser configParser = new ConfigParser(new FileInputStream(file));
//		Config config = configParser.buildConfiguration();
//		Setting rootSetting = config.getRoot();
	}
	
}
