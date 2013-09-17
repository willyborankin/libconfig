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
	
	protected String name;
	
	protected Type type;
	
	protected Config config;
	
	private Setting parent;
	
	protected Object value;
	
	protected Setting(Type type) {
		this(null, type);
	}

	protected Setting(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}
	
	public Setting getParent() {
		return parent;
	}
	
	public Type getType() {
		return type;
	}

	public Config getConfig() {
		return config;
	}

	@SuppressWarnings("unchecked")
	public int getLength() {
		int length;
		switch (type) {
		case ARRAY:
			length = ((Object[]) value).length;
			break;
		case LIST:
		case GROUP:
			length = ((List<Setting>) value).size();
			break;
		default:
			length = 0;
		}
		return length;
	}

	public Setting lookup(String name) {
		Setting found = null;
//		for (Setting setting : settings) {
//			if (name.equals(setting.getName())) {
//				found = setting;
//				break;
//			}
//		}
		return found; 
	}
	
	public Setting lookup(int index) {
		if (type != Type.LIST && type != Type.ARRAY)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return null;//settings.get(index);
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
	
	public Setting addScalar(String name, String value) {
		if (type != Type.GROUP)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.STRING, value);
		return this;
	}
	
	public Setting addScalar(String name, Integer value) {
		if (type != Type.GROUP)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.INTEGER, value);
		return this;
	}
	
	public Setting addScalar(String name, Double value) {
		if (type != Type.GROUP)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.FLOAT, value);
		return this;
	}
	
	public Setting addScalar(String name, Boolean value) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.BOOLEAN, value);
		return this;
	}
	
	public Setting addScalar(String value) {
		if (type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(null, Type.STRING, value);
		return this;
	}
	
	public Setting addScalar(Integer value) {
		if (type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(null, Type.INTEGER, value);
		return this;
	}
	
	public Setting addScalar(Double value) {
		if (type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(null, Type.FLOAT, value);
		return this;
	}
	
	public Setting addScalar(Boolean value) {
		if (type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(null, Type.BOOLEAN, value);
		return this;
	}
	
	public Setting addArray(String name, String ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.ARRAY, values);
		return this;
	}
	
	public Setting addArray(String name, Integer ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.ARRAY, values);
		return this;
	}
	
	public Setting addArray(String name, Double ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.ARRAY, values);
		return this;
	}
	
	public Setting addArray(String name, Boolean ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.ARRAY, values);
		return this;
	}
	
	public Setting addList(String name) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting listSetting = applaySetting(name, Type.LIST);
		return listSetting;
	}
	
	public Setting addGroup(String name) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return applaySetting(name, Type.GROUP);
	}
	
	public Setting addGroup() {
		return addGroup(null);
	}
	
	private <T> void applay(String name, Type type, T value) {
		@SuppressWarnings("unchecked")
		List<Setting> settings = (List<Setting>) this.value;
		settings.add(createSetting(name, type, value));
	}
	
	private <T> Setting applaySetting(String name, Type type) {
		@SuppressWarnings("unchecked")
		List<Setting> settings = (List<Setting>) this.value;
		Setting setting = createSetting(name, type, new ArrayList<>());
		settings.add(setting);
		return setting;
	}
	
//	public Setting addSetting(Type type) {
//		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
//			throw new IllegalArgumentException("Unsupported group type " + type);
//		if (getType() != Type.GROUP && getType() != Type.LIST)
//			throw new IllegalArgumentException("Setting with type " + getType() + " does not support type " + type);
//		return createSetting(name, type);
//	}

	public Setting addSetting(String name, String ... array) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		applay(name, Type.ARRAY, array);
		return this;
	}
//	
//	public Setting addSetting(String name, int ... array) {
//		if (getType() != Type.ARRAY)
//			throw new IllegalArgumentException("Unsupported type " + type);
//		Setting arraySetting = createSetting(name, Type.ARRAY);
//		for (int value : array) arraySetting.addSetting(value);
//		return arraySetting;
//	}
//	
//	public Setting addSetting(String name, double ... array) {
//		if (getType() != Type.ARRAY)
//			throw new IllegalArgumentException("Unsupported type " + type);
//		Setting arraySetting = createSetting(name, Type.ARRAY);
//		for (double value : array) arraySetting.addSetting(value);
//		return arraySetting;
//	}
//	
//	public Setting addSetting(String name, boolean ... array) {
//		if (getType() != Type.ARRAY)
//			throw new IllegalArgumentException("Unsupported type " + type);
//		Setting arraySetting = createSetting(name, Type.ARRAY);
//		for (boolean value : array) arraySetting.addSetting(value);
//		return arraySetting;
//	}
//	
//	private Setting createSetting(String name, Type type) {
//		return createSetting(name, type, null);
//	}
//	
	private <T> Setting createSetting(String name, Type type, T value) {
		Setting setting = new Setting(name, type);
		setting.config = config;
		setting.parent = this;
		setting.value = value;
		return setting;
	}
//	
//	protected List<Setting> getSettings() {
//		return settings;
//	}
	
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
