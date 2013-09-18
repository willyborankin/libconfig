package org.libconfig.test;

import java.io.IOException;

import org.libconfig.Config;
import org.libconfig.ConfigOutputter;
import org.libconfig.Setting;
import org.libconfig.parser.ParseException;
import org.testng.annotations.Test;

public class ConfigSaveFileTest {

	@Test
	public void saveRightStructure() throws IOException, ParseException {
		Config config = new Config();
		
		config.addScalar("SCALAR_SETTING_0", 1340);
		config.addScalar("SCALAR_SETTING_1", false);
		config.addScalar("SCALAR_SETTING_2", 123e-100);
		config.addScalar("SCALAR_SETTING_3","asdsadsadsds");

		Setting groupSetting = config.addGroup("GROUP_SETTING_1");

		groupSetting.addScalar("ff", "ff");
		groupSetting.addScalar("gg", 1);
		groupSetting.addScalar("hh", 2.5d);
		groupSetting.addScalar("ee", true);

		groupSetting.addArray("INNER_GROUP_ARRAY_1", new String[] { "a", "b", "c" });

		Setting innerGroupInGroup = groupSetting.addGroup("INNER_GROUP_GROUP_1");
		innerGroupInGroup.addScalar("jj", true);
		innerGroupInGroup.addScalar("kk", 1);

		Setting innerGroupInGroup2 = innerGroupInGroup.addGroup("INNER_GROUP_GROUP_2");
		innerGroupInGroup2.addScalar("jj1", false);
		innerGroupInGroup2.addScalar("kk1", 2);

		Setting innerListInGroup = innerGroupInGroup.addList("INNER_LIST_GROUP_1");
		innerListInGroup.addScalar(1).addScalar(2).addScalar(3);

		innerGroupInGroup.addArray("INNER_ARRAY_GROUP_1", 5, 6, 7, 8, 9, 10);

		Setting listSetting = config.addList("SIMPLE_LIST");
		for (int i = 1; i < 5; i++) {
			listSetting.addScalar(i);
		}
		config.addArray("SIMPLE_ARRAY", 11, 12, 13, 14, 15, 16);

//		File file = new File("11.cgf");
		ConfigOutputter configOutputter = new ConfigOutputter();
		configOutputter.output(config, System.err);
		
//		parse back
//		ConfigParser configParser = new ConfigParser(new FileInputStream(file));
//		Config config = configParser.buildConfiguration();
//		Setting rootSetting = config.getRoot();
	}
	
}
