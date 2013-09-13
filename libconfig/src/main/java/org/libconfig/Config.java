package org.libconfig;

import java.util.LinkedHashMap;
import java.util.Map;

import org.libconfig.Setting.Type;

public class Config {

	private final Map<String, Setting> settings;

	public Config() {
		settings = new LinkedHashMap<>();
	}

	public Setting lookup(String name) { 
		if (name == null) throw new IllegalArgumentException("Name for settings has not been set");
		if (name.contains(".")) {
			String[] names = name.split(".");
		}
		return settings.get(name); 
	}

	public Setting addSetting(String name, String value) {
		return createSetting(name, Type.STRING);
	}

	public Setting addSetting(String name, int value) {
		return createSetting(name, Type.INTEGER);
	}
	
	public Setting addSetting(String name, double value) {
		return createSetting(name, Type.FLOAT);
	}
	
	public Setting addSetting(String name, boolean value) {
		return createSetting(name, Type.BOOLEAN);
	}

	public Setting addSetting(String name, Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		return createSetting(name, type);
	}
	
	private Setting createSetting(String name, Type type) {
		Setting setting = new Setting(name, type);
		setting.setConfig(this);
		settings.put(setting.getName(), setting);
		return setting;
	}

	protected Map<String, Setting> getSettings() {
		return settings;
	}
	
}
