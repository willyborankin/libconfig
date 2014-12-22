/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 *
 * Copyright (c) 2013, 2014, Andrey Pleskach (willyborankin@gmail.com). All rights reserved.
 *
 * The libconfig is licensed under the terms of the GPLv2
 * <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 * There are not special exceptions to the terms and conditions of the GPLv2.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation; version 2
 * of the License.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
 * Floor, Boston, MA 02110-1301  USA
 *
******************************************************************************/
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

	public <T> T lookupValue(String name) { 
		Setting setting = lookup(name);
		T value = null;
		if (setting != null) value = setting.getValue();
		return value;
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
	
	public <T> Setting addScalar(String name, T value) {
		return createSetting(name, resolveType(value), value);
	}
	
	public <T> Setting addArray(String name, T[] values) {
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
		if (settings.containsKey(name)) 
			throw new IllegalArgumentException("Dublicate setting name " + name);
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
		for (int i = 0; i < from.length; i++) {
			settings[i] = createSetting(null, resolveType(from[i]), from[i], parent);
		}
		return settings;
	}
	
	public <T> Type resolveType(T value) {
		Type type = Type.UNKNOWN;
		if (value != null) {
			if (value instanceof String) {
				type = Type.STRING;
			} else if (value instanceof Integer) {
				type = Type.INTEGER;
			} else if (value instanceof Double) {
				type = Type.FLOAT;
			} else if (value instanceof Boolean) {
				type = Type.BOOLEAN;
			} else {
				throw new IllegalArgumentException("Unsupported scalar type " + value);
			}
		}
		return type;
	}
	
	protected Map<String, Setting> getSettings() {
		return settings;
	}
	
}
