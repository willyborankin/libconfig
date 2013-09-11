package org.libconfig.parser.test;

import java.io.InputStream;

import org.libconfig.Config;
import org.libconfig.parser.ConfigParser;
import org.libconfig.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ConfigParserTest {

	private static Logger LOG = LoggerFactory.getLogger(ConfigParserTest.class);

	@Test
	public void testScalarValues() throws ParseException {
		
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("scalar-values.conf");
		ConfigParser configParser = new ConfigParser(in);
		LOG.info("Start");
		Config configuration = configParser.buildConfiguration();
		LOG.info("Stop");
//		Assert.assertFalse(configuration.getSettings().isEmpty());
//		for (Setting setting : configuration.getSettings()) {
//			LOG.info("{}={}", setting.getName(), setting.getValue());
//		}
	} 

	@Test
	public void testOpenFacesConfig() throws ParseException {
		InputStream in = ConfigParserTest.class.getClassLoader().getResourceAsStream("opencv.conf");
		ConfigParser configParser = new ConfigParser(in);
		LOG.info("Start");
		Config configuration = configParser.buildConfiguration();
		LOG.info("Stop");
	}
	
}
