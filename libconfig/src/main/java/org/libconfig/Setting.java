package org.libconfig;

import java.io.IOException;
import java.io.Writer;

public class Setting {

	public static enum Type {
		STRING,
		INTEGER,
		FLOAT,
		BOOLEAN,
		LIST,
		ARRAY,
		GROUP,
		UNKNOWN;
	};
	
	private final String name;
	
	private final Type type;
	
	private SettingValue<?> settingValue;
	
	public Setting(Type type) {
		this(null, type);
	}

	public Setting(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public <T> T getValue() {
		return (T) settingValue.getValue();
	}
	
	public Type getType() {
		return type;
	}

	public Setting addSetting(String name, String value) {
		return null;
	}
	
	public Setting addSetting(String name, int value) {
		return null;
	}
	
	public Setting addSetting(String name, double value) {
		return null;
	}
	
	public Setting addSetting(String name, boolean value) {
		return null;
	}
	
	public Setting addSetting(Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		if (getType() != Type.GROUP && getType() != Type.LIST)
			throw new IllegalArgumentException("Setting with type " + getType() + " does not support type " + type);
		return new Setting(type);
	}
	
	protected void write(Writer writer, Config config) throws IOException {
		if (getName() != null) {
			writer.write(getName());
		}
//		if (getValue() != null) {
//			getValue().write(writer, configuration);
//		} else {
//			writer.write(" ");
//		}
		writer.write("\n");
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
