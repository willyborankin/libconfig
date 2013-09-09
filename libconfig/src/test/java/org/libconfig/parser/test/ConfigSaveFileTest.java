package org.libconfig.parser.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.libconfig.Config;
import org.libconfig.Config.Format;
import org.libconfig.Config.NumberFormat;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;
import org.libconfig.parser.ConfigParser;
import org.libconfig.parser.ParseException;
import org.testng.annotations.Test;

public class ConfigSaveFileTest {

	@Test
	public void saveRightStructure() throws IOException, ParseException {
		Config configuration = new Config();
		configuration.setFormat(Format.COLON);
		configuration.setNumberFormat(NumberFormat.HEX);
		
		Setting setting1 = configuration.addSetting("SOME_SETTING_0", 1340);
		Setting setting2 = configuration.addSetting("SOME_SETTING_1", false);
		Setting setting3 = configuration.addSetting("SOME_SETTING_2", 123e-100);
		Setting setting4 = configuration.addSetting("SOME_SETTING_3","asdsadsadsds");

		Setting groupSetting = configuration.addSetting(Type.GROUP);
		
		groupSetting.addSetting("ff", "ff");
		groupSetting.addSetting("gg", 1);
		groupSetting.addSetting("hh", 2.5d);
		groupSetting.addSetting("ee", true);
		
		File file = new File("11.cgf");
		configuration.writeFile(file);
		//parse back
		ConfigParser configParser = new ConfigParser(new FileInputStream(file));
		configParser.getConfiguration();
	}
	
}
