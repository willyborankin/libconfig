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
	
	protected Setting parent;
	
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
		if (getName().equals(name)) {
			found = this;
		} else {
			if (type == Type.LIST || type == Type.GROUP) {
				List<Setting> settings = getValue();
				for (Setting setting : settings) {
					found = setting.lookup(name);
					if (found != null) break;
				}
			}
		}
		return found; 
	}
	
	public String getPath() {
		String path = getName();
		Setting parentCursor = parent;
		while (parentCursor != null) {
			String tmp = path;
			path = parentCursor.getName() + "." + tmp;
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
		Setting arraySetting = applaySetting(name, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, this);
		return this;
	}
	
	public Setting addArray(String name, Integer ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(name, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, this);
		return this;
	}
	
	public Setting addArray(String name, Double ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(name, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, this);
		return this;
	}
	
	public Setting addArray(String name, Boolean ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(name, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, this);
		return this;
	}
	
	public Setting addArray(String ... values) {
		if (type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(null, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, this);
		return this;
	}
	
	public Setting addArray(Integer ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(null, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, arraySetting);
		return this;
	}
	
	public Setting addArray(Double ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(null, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, arraySetting);
		return this;
	}
	
	public Setting addArray(Boolean ... values) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		Setting arraySetting = applaySetting(null, Type.ARRAY);
		arraySetting.value = config.copyArrayValues(values, arraySetting);
		return this;
	}
	
	public Setting addList(String name) {
		if (type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return applaySetting(name, Type.LIST);
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
		List<Setting> settings = getValue();
		settings.add(config.createSetting(name, type, value, this));
	}
	
	private <T> Setting applaySetting(String name, Type type) {
		@SuppressWarnings("unchecked")
		List<Setting> settings = (List<Setting>) this.value;
		Setting setting = config.createSetting(name, type, new ArrayList<>(), this);
		settings.add(setting);
		return setting;
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
