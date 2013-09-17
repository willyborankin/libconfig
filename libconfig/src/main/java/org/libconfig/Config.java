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
		Setting found;
		if (name.contains(".")) {
			String[] names = name.split("\\.");
			found = names.length != 0 ? lookup(names) : null;
		} else {
			found = settings.get(name);
		}
		return found; 
	}

	private Setting lookup(String ... others) {
		Setting found = settings.get(others[0]);
		if (found != null) {
			for (int i = 1; i < others.length; i++) {
				found = found.lookup(others[i]);
			}
		}
		return found;
	}
	
	public Setting addSetting(String name, String value) {
		return createSetting(name, Type.STRING, value);
	}

	public Setting addSetting(String name, int value) {
		return createSetting(name, Type.INTEGER, value);
	}
	
	public Setting addSetting(String name, double value) {
		return createSetting(name, Type.FLOAT, value);
	}
	
	public Setting addSetting(String name, boolean value) {
		return createSetting(name, Type.BOOLEAN, value);
	}

	public Setting addSetting(String name, String ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		for (String value : values)
			arraySetting.addSetting(value);
		return arraySetting;
	}
	
	public Setting addSetting(String name, int ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		for (int value : values)
			arraySetting.addSetting(value);
		return arraySetting;
	}
	
	public Setting addSetting(String name, double ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		for (double value : values)
			arraySetting.addSetting(value);
		return arraySetting;
	}
	
	public Setting addSetting(String name, boolean ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		for (boolean value : values)
			arraySetting.addSetting(value);
		return arraySetting;
	}
	
	public Setting addSetting(String name, Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		return createSetting(name, type);
	}
	
	private Setting createSetting(String name, Type type) {
		return createSetting(name, type, null);
	}
	
	private <T> Setting createSetting(String name, Type type, T value) {
		Setting setting = new Setting(name, type);
		setting.setConfig(this);
		setting.addValue(value);
		settings.put(setting.getName(), setting);
		return setting;
	}

	protected Map<String, Setting> getSettings() {
		return settings;
	}
	
}
