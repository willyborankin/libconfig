package org.libconfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.libconfig.Setting.Type;

public class Config {

	public static enum Format {
		EQUALS(" = "),
		COLON(":"),
		NONE("");
		
		private final String value;

		private Format(String value) {
			this.value = value;
		}
		
		public String value() {
			return value;
		}
		
	};
	
	public static enum NumberFormat {
		DEFAULT,
		HEX;
	};
	
	private final List<Setting> settings;

	private Format 		format;
	
	private NumberFormat numberFormat;
	
	public Config() {
		settings = new ArrayList<>();
		format = Format.COLON;
		numberFormat = NumberFormat.DEFAULT;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	public Setting lookup(String name) { 
//		for (Setting setting : root.get)
		return null; 
	}

	public Setting addSetting(String name, String value) {
		return createSetting(name, Type.STRING);
	}

	public Setting addSetting(String name, int value) {
		return createSetting(name, Type.INTEGER);
	}
	
	public Setting addSetting(String name, double value) {
		return createSetting(name, Type.FLOAT);
	}
	
	public Setting addSetting(String name, boolean value) {
		return createSetting(name, Type.BOOLEAN);
	}

	public Setting addSetting(String name, Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		return createSetting(name, type);
	}
	
	private Setting createSetting(String name, Type type) {
		Setting setting = new Setting(name, type);
		settings.add(setting);
		return setting;
	}
	
	public void write(String file) throws IOException {
		if (file == null) throw new IllegalArgumentException("File has not been set");
		write(new File(file));
	}

	public void write(File file) throws IOException {
		if (file == null) throw new IllegalArgumentException("File has not been set");
		write(new FileOutputStream(file));
	}

	public void write(OutputStream out) throws IOException {
		if (out == null) throw new IllegalArgumentException("Output stream has not been set");
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, Charset.defaultCharset()));
		for (Setting setting : settings) {
			setting.write(bufferedWriter, this);
		}
		bufferedWriter.flush();
		bufferedWriter.write("\n");
	}
	
}
