package org.libconfig;

import java.io.IOException;
import java.io.Writer;
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

	public <T> T getValue() {
		return (T) settingValue;
	}
	
	protected void addValue(Object value) {
		this.settingValue = value;
	}
	
	public Type getType() {
		return type;
	}

	public Setting addSetting(String name, String value) {
		Setting setting = createSetting(name, Type.STRING);
		setting.addValue(value);
		return setting;
	}
	
	public Setting addSetting(String name, int value) {
		Setting setting = createSetting(name, Type.INTEGER);
		setting.addValue(value);
		return setting;
	}
	
	public Setting addSetting(String name, double value) {
		Setting setting = createSetting(name, Type.FLOAT);
		setting.addValue(value);
		return setting;
	}
	
	public Setting addSetting(String name, boolean value) {
		Setting setting = createSetting(name, Type.BOOLEAN);
		setting.addValue(value);
		return setting;
	}
	
	public Setting addSetting(String value) {
		return createSetting(null, Type.STRING); 
	}
	
	public Setting addSetting(int value) {
		if (getType() != Type.LIST && getType() != Type.ARRAY)
			throw new IllegalArgumentException("Such method does not applicable for type " + type);
		return createSetting(null, Type.INTEGER); 
	}
	
	public Setting addSetting(double value) {
		return createSetting(null, Type.FLOAT); 
	}
	
	public Setting addSetting(boolean value) {
		return createSetting(null, Type.BOOLEAN); 
	}
	
	public Setting addSetting(Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		if (getType() != Type.GROUP && getType() != Type.LIST)
			throw new IllegalArgumentException("Setting with type " + getType() + " does not support type " + type);
		return createSetting(name, type);
	}
	
	public Setting addSetting(String name, Type type, int ... array) {
		return createSetting(name, type);
	}
	
	private Setting createSetting(String name, Type type) {
		Setting setting = new Setting(name, type);
		settings.add(setting);
		return setting;
	}
	
	protected void write(Writer writer, Config config) throws IOException {
		if (getName() != null) {
			writer.write(getName());
			writer.write(config.getFormat().value());
		}
		if (getType() == Type.GROUP) {
			writer.write("{\n");
		} else if (getType() == Type.LIST) {
			writer.write("(");
		} else if (getType() == Type.ARRAY) {
			writer.write("[");
		}
		writer.write(getValue() != null ? getValue().toString() : "");
		if (!settings.isEmpty()) {
			for (Setting setting : settings) {
				setting.write(writer, config);
			}
		}
		if (getType() == Type.GROUP) {
			writer.write("}");
		} else if (getType() == Type.LIST) {
			writer.write(")");
		} else if (getType() == Type.ARRAY) {
			writer.write("]");
		}
		writer.write("\n");
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
