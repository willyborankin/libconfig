package org.libconfig.parser;

import org.libconfig.Config;
import org.libconfig.Setting;

public class ParserContext {

	private final Setting setting;
	
	private final Config configuration;

	public ParserContext(Setting setting, Config configuration) {
		super();
		this.setting = setting;
		this.configuration = configuration;
	}

	public Setting getSetting() {
		return setting;
	}

	public Config getConfiguration() {
		return configuration;
	}

}
