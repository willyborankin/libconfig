package org.libconfig.parser;

import org.libconfig.Config;
import org.libconfig.Setting;
import org.libconfig.Setting.Type;

public class ParserContext {

	private Setting parent;
	
	private final Config config;

	public ParserContext(Config config) {
		super();
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public Setting getParent() {
		return parent;
	}

	public void setParent(Setting parent) {
		this.parent = parent;
	}

	public <T> Setting addScalarSetting(String name, T value) {
		Setting setting;
		if (parent == null) {
			setting = config.addScalar(name, value);
		} else {
			if (parent.getType() == Type.LIST) {
				setting = parent.addScalar(value);
			} else {
				setting = parent.addScalar(name, value);
			}
		}
		return setting;
	}
	
	public <T> Setting addArraySetting(String name, T[] values) {
		Setting setting;
		if (parent == null) {
			setting = config.addArray(name, values);
		} else {
			if (parent.getType() == Type.LIST)
				setting = parent.addArray(values);
			else
				setting = parent.addArray(name, values);
		}
		return setting;
	}
	
	public Setting addGroupSetting(String name) {
		Setting setting;
		if (parent == null) {
			setting = config.addGroup(name);
		} else {
			setting = parent.addGroup(name);
		}
		return setting;
	}

	public Setting addListSetting(String name) {
		Setting setting;
		if (parent == null) {
			setting = config.addList(name);
		} else {
			setting = parent.addList(name);
		}
		return setting;
	}
}
