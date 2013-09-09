package org.libconfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.libconfig.Setting.Type;

public class Config {

	public static enum Format {
		EQUALS,
		COLON,
		NONE;
	};
	
	public static enum NumberFormat {
		DEFAULT,
		HEX;
	};
	
	private final Setting 	root;

	private Format 		format;
	
	private NumberFormat numberFormat;
	
	public Config() {
		root = new Setting("_ROOT_", Type.UNKNOWN);
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

	public Setting getRoot() {
		return root;
	}

	public Setting lookup(String name) { 
//		for (Setting setting : root.get)
		return null; 
	}

	public Setting addSetting(String name, String value) {
		Setting setting = new Setting(name, Type.STRING);
		return setting;
	}

	public Setting addSetting(String name, int value) {
		Setting setting = new Setting(name, Type.INTEGER);
		return setting;
	}
	
	public Setting addSetting(String name, double value) {
		Setting setting = new Setting(name, Type.FLOAT);
		return setting;
	}
	
	public Setting addSetting(String name, boolean value) {
		Setting setting = new Setting(name, Type.BOOLEAN);
		return setting;
	}

	public Setting addSetting(Type type) {
		if (type != Type.ARRAY && type != Type.GROUP && type != Type.LIST)
			throw new IllegalArgumentException("Unsupported group type " + type);
		return new Setting(type);
	}
	
	public void writeFile(String file) throws IOException {
		if (file == null) throw new IllegalArgumentException("File has not been set");
		writeFile(new File(file));
	}

	public void writeFile(File file) throws IOException {
		if (file == null) throw new IllegalArgumentException("File has not been set");
		writeFile(new FileWriter(file));
	}

	public void writeFile(Writer out) throws IOException {
		if (out == null) throw new IllegalArgumentException("Output stream has not been set");
		try {
//			for (Setting setting : root.getChildren()) {
//				setting.write(out, this);
//				out.flush();
//			}
//			out.write("\n");
		} finally {
			out.close();
		}
	}
	
}
