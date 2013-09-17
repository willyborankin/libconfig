package org.libconfig;

import java.util.ArrayList;
import java.util.List;

public class Setting {

	public static enum Type {
		STRING,
		INTEGER,
		FLOAT,
		BOOLEAN,
		LIST,
		ARRAY,
		GROUP;
	};
	
	private final String name;
	
	private final Type type;
	
	private Config config;
	
	private Setting parent;
	
	private Object settingValue;
	
	private List<Setting> settings;
	
	public Setting(Type type) {
		this(null, type);
	}

	public Setting(String name, Type type) {
		this.name = name;
		this.type = type;
		settings = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) settingValue;
	}
	
	protected void addValue(Object value) {
		this.settingValue = value;
	}

	public Setting getParent() {
		return parent;
	}
	
	protected void setParent(Setting parent) {
		this.parent = parent;
	}

	public Type getType() {
		return type;
	}

	public Config getConfig() {
		return config;
	}

	protected void setConfig(Config config) {
		this.config = config;
	}

	public int getLength() {
		return settings.size();
	}

	public Setting lookup(String name) {
		Setting found = null;
		for (Setting setting : settings) {
			if (name.equals(setting.getName())) {
				found = setting;
				break;
			}
		}
		return found; 
	}
	
	public Setting lookup(int index) {
		if (type != Type.LIST && type != Type.ARRAY)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return settings.get(index);
	}
	
	public String getPath() {
		String path = getName();
		Setting parentCursor = parent;
		while (parentCursor != null) {
			String tmp = path;
//			if (parentCursor.getType() == Type.ARRAY || parentCursor.getType() == Type.GROUP) {
//				path = ".[" + parentCursor.getSettings().values(). + "]" + tmp;
//			} else {
				path = parentCursor.getName() + "." + tmp;
//			}
			parentCursor = parentCursor.getParent();
		}
		return path;
	}
	
	public Setting addSetting(String name, String value) {
		Setting setting = createSetting(name, Type.STRING, value);
		return setting;
	}
	
	public Setting addSetting(String name, int value) {
		Setting setting = createSetting(name, Type.INTEGER, value);
		return setting;
	}
	
	public Setting addSetting(String name, double value) {
		Setting setting = createSetting(name, Type.FLOAT, value);
		return setting;
	}
	
	public Setting addSetting(String name, boolean value) {
		Setting setting = createSetting(name, Type.BOOLEAN, value);
		return setting;
	}
	
	public Setting addSetting(String value) {
		return createSetting(null, Type.STRING, value); 
	}
	
	public Setting addSetting(int value) {
		if (getType() != Type.LIST && getType() != Type.ARRAY)
				throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return createSetting(null, Type.INTEGER, value);
	}
	
	public Setting addSetting(double value) {
		if (getType() != Type.LIST && getType() != Type.ARRAY)
				throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return createSetting(null, Type.FLOAT, value); 
	}
	
	public Setting addSetting(boolean value) {
		if (getType() != Type.LIST && getType() != Type.ARRAY)
				throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return createSetting(null, Type.BOOLEAN, value); 
	}
	
	public Setting addSetting(Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		if (getType() != Type.GROUP && getType() != Type.LIST)
			throw new IllegalArgumentException("Setting with type " + getType() + " does not support type " + type);
		return createSetting(name, type);
	}
	
	public Setting addSetting(String name, Type type, String ... array) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		return createSetting(name, type, array);
	}
	
	public Setting addSetting(String name, int ... array) {
		if (getType() != Type.ARRAY)
			throw new IllegalArgumentException("Unsupported type " + type);
		return createSetting(name, Type.INTEGER, array);
	}
	
	public Setting addSetting(String name, double ... array) {
		if (getType() != Type.ARRAY)
			throw new IllegalArgumentException("Unsupported type " + type);
		return createSetting(name, Type.FLOAT, array);
	}
	
	public Setting addSetting(String name, boolean ... array) {
		if (getType() != Type.ARRAY)
			throw new IllegalArgumentException("Unsupported type " + type);
		return createSetting(name, Type.BOOLEAN, array);
	}
	
	private Setting createSetting(String name, Type type) {
		return createSetting(name, type, null);
	}
	
	private <T> Setting createSetting(String name, Type type, T value) {
		Setting setting = new Setting(name, type);
		setting.setConfig(config);
		setting.setParent(this);
		setting.addValue(value);
		settings.add(setting);
		return setting;
	}
	
	protected List<Setting> getSettings() {
		return settings;
	}
	
	@Override
	public String toString() {
		return "Setting [name=" + name + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
