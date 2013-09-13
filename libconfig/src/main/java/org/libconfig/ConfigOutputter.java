package org.libconfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.libconfig.Setting.Type;

public class ConfigOutputter {

	public static enum Format {
		EQUALS,
		COLON;
	};
	
	public static enum NumberFormat {
		DEFAULT,
		HEX;
	};
	
	
	public final static char LPAREN 		= '(';

	public final static char RPAREN 		= ')';
	
	public final static char LBRACE 		= '{'; 
	
	public final static char RBRACE 		= '}';
	
	public final static char LBRACKET 	= '[';
	
	public final static char RBRACKET 	= ']';
	
	public final static char SEMICOLON 	= ';';
	
	public final static char COLON 		= ':';
	
	public final static char COMMA 		= ',';
	
	public final static char EQUALS 		= '=';
	
	public final static char QUOTES 		= '"';
	
	private final static int DEFALUT_DEPTH = 16;
	
	private char configNameValueSeparator;
	
	private String[] depthIndent;
	
	private int depth;
	
	private Format format;
	
	private NumberFormat numberFormat;
	
	public ConfigOutputter() {
		this(Format.COLON, NumberFormat.DEFAULT);
	}
	
	public ConfigOutputter(Format format) {
		this(format, NumberFormat.DEFAULT);
	}
	
	public ConfigOutputter(NumberFormat numberFormat) {
		this(Format.COLON, numberFormat);
	}
	
	public ConfigOutputter(Format format, NumberFormat numberFormat) {
		this.format = format;
		this.numberFormat = numberFormat;
		this.depthIndent = new String[DEFALUT_DEPTH];
		this.depth = 0;
	}
	
	public void output(Config config, String file) throws IOException {
		if (config == null) throw new IllegalArgumentException("Configuration is not set");
		if (file == null) throw new IllegalArgumentException("File has not been set");
		output(config, new File(file));
	}

	public void output(Config config, File file) throws IOException {
		if (config == null) throw new IllegalArgumentException("Configuration is not set");
		if (file == null) throw new IllegalArgumentException("File has not been set");
		output(config, new FileOutputStream(file));
	}

	public void output(Config config, OutputStream out) throws IOException {
		if (config == null) throw new IllegalArgumentException("Configuration is not set");
		if (out == null) throw new IllegalArgumentException("Output stream has not been set");
		
		depth = 0;
		configNameValueSeparator = (format == Format.COLON) ? COLON : EQUALS;
		
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, Charset.defaultCharset()));
		for (Setting setting : config.getSettings().values()) {
			printSetting(setting, bufferedWriter);
			bufferedWriter.write(SEMICOLON);
			if (depth == 0)
				bufferedWriter.write("\n");
		}
		bufferedWriter.flush();
	}
	
	private void printSetting(Setting setting, Writer writer) throws IOException {
		switch (setting.getType()) {
		case STRING:
			printString(setting, writer);
			break;
		case INTEGER:
			printInt(setting, writer);
			break;
		case FLOAT:
			printFloat(setting, writer);
			break;
		case BOOLEAN:
			printBoolean(setting, writer);
			break;
		case ARRAY:
			printArray(setting, writer);
			break;
		case LIST:
			printList(setting, writer);
			break;
		case GROUP:
			printGroup(setting, writer);
			break;
		}
	}

	private void printString(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		writer.write(QUOTES);
		writer.write(setting.getValue() != null ? setting.getValue().toString() : "");
		writer.write(QUOTES);
	}
	
	private void printInt(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		if (numberFormat == NumberFormat.HEX) {}
		writer.write(setting.getValue() != null ? setting.getValue().toString() : "");
	}
	
	private void printFloat(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		writer.write(setting.getValue() != null ? setting.getValue().toString() : "");
	}

	private void printBoolean(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		writer.write(setting.getValue() != null ? setting.getValue().toString() : "");
	}
	
	private void printArray(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		writer.write(LBRACKET);
		depth++;
		for (int i = 0; i < setting.getSettings().size(); i++) {
			if (i > 0) {
				writer.write(COMMA);
				writer.write(" ");
			}
			printSetting(setting.getSettings().get(i), writer);
		}
		writer.write(RBRACKET);
		depth--;
	} 
	
	private void printList(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		depth++;
		writer.write(LPAREN);
		boolean innerComplexType = false; 
		for (int i = 0; i < setting.getSettings().size(); i++) {
			Setting subSetting = setting.getSettings().get(i);
			if (i > 0) {
				writer.write(COMMA);
				writer.write(" ");
			}
			if (subSetting.getType() == Type.LIST || 
				subSetting.getType() == Type.ARRAY ||
				subSetting.getType() == Type.GROUP) {
				writer.write(getIndent());
				innerComplexType = true;
			}
			printSetting(subSetting, writer);
		}
		if (innerComplexType) {
			writer.write(getIndent());
			depth--;
			writer.write(RPAREN);
		} else {
			writer.write(RPAREN);
			depth--;
		}	
	} 

	private void printGroup(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		depth++;
		writer.write(LBRACE);
		for (int i = 0; i < setting.getSettings().size(); i++) {
			if (i > 0) {
				writer.write(COMMA);
				writer.write(" ");
			}
			writer.write(getIndent());
			printSetting(setting.getSettings().get(i), writer);
		}
		depth--;
		writer.write(getIndent());
		writer.write(RBRACE);
	} 

	private void printName(Setting setting, Writer writer) throws IOException {
		if (setting.getName() != null) {
			writer.write(setting.getName());
			writer.write(" ");
			writer.write(configNameValueSeparator);
			writer.write(" ");
		}
	}
	
	private String getIndent() {
		if (depthIndent[depth] == null) {
			StringBuilder s = new StringBuilder("\r");
			for (int i = 0 ; i < depth; i++) s.append("  ");
			depthIndent[depth] = s.toString();
		} 
		return depthIndent[depth];
	}
	
}
