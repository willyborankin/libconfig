package org.libconfig;

import java.io.Writer;

public abstract class SettingValue<T> {
	
	private T value;

	public SettingValue() {
		this(null);
	}
	
	public SettingValue(T value) {
		super();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void addValue(T value) {
		this.value = value;
	}

	protected abstract void write(Writer writer, Config config);
	
}
