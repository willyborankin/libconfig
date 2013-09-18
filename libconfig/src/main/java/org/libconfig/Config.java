package org.libconfig;

import java.util.ArrayList;
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
		Setting found = null;
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
	
	public Setting addScalar(String name, String value) {
		return createSetting(name, Type.STRING, value);
	}

	public Setting addScalar(String name, Integer value) {
		return createSetting(name, Type.INTEGER, value);
	}
	
	public Setting addScalar(String name, Double value) {
		return createSetting(name, Type.FLOAT, value);
	}
	
	public Setting addScalar(String name, Boolean value) {
		return createSetting(name, Type.BOOLEAN, value);
	}

	public Setting addArray(String name, String ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		arraySetting.value = copyArrayValues(values, null);
		return arraySetting;
	}
	
	public Setting addArray(String name, Integer ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		arraySetting.value = copyArrayValues(values, null);
		return arraySetting;
	}

	public Setting addArray(String name, Double ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		arraySetting.value = copyArrayValues(values, null);
		return arraySetting;
	}

	public Setting addArray(String name, Boolean ... values) {
		Setting arraySetting = createSetting(name, Type.ARRAY);
		arraySetting.value = copyArrayValues(values, null);
		return arraySetting;
	}

	public Setting addList(String name) {
		return createSetting(name, Type.LIST, new ArrayList<>());
	}

	public Setting addGroup(String name) {
		return createSetting(name, Type.GROUP, new ArrayList<>());
	}

	private Setting createSetting(String name, Type type) {
		return createSetting(name, type, null);
	}
	
	private <T> Setting createSetting(String name, Type type, T value) {
		Setting setting = createSetting(name, type, value, null);
		settings.put(setting.getName(), setting);
		return setting;
	}
	
	protected <T> Setting createSetting(String name, Type type, T value, Setting parent) {
		Setting setting = new Setting(name, type);
		setting.config = this;
		setting.type = type;
		setting.value = value;
		setting.parent = parent;
		return setting;
	}

	protected <T> Setting[] copyArrayValues(T[] from, Setting parent) {
		Setting[] settings = new Setting[from.length];
		int i = 0;
		for (T value : from) {
			settings[i] = createSetting(null, resolveType(value.getClass()), value, parent);
			i++;
		}
		return settings;
	}
	
	protected static <T> Type resolveType(Class<T> value) {
		Type type;
		if (value.equals(String.class)) {
			type = Type.STRING;
		} else if (value.equals(Integer.class)) {
			type = Type.INTEGER;
		} else if (value.equals(Double.class)) {
			type = Type.FLOAT;
		} else if (value.equals(Boolean.class)) {
			type = Type.BOOLEAN;
		} else {
			throw new IllegalArgumentException("Unsupported scalar type " + value);
		}
		return type;
	}
	
	protected Map<String, Setting> getSettings() {
		return settings;
	}
	
}
