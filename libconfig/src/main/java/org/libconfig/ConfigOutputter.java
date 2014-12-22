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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

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
	
	private void printValue(Object value, Writer writer) throws IOException {
		if (value instanceof String) {
			printString(value, writer);
		} else {
			writer.write(value != null ? value.toString() : "");
		}
	}
	
	private void printSetting(Setting setting, Writer writer) throws IOException {
		switch (setting.getType()) {
		case STRING:
			printString(setting, writer);
			break;
		case INTEGER:
		case UNKNOWN:
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
		String value = setting.getValue();
		printString(value, writer);
	}

	private void printString(Object value, Writer writer) throws IOException {
		writer.write(QUOTES);
		writer.write(value != null ? value.toString() : "");
		writer.write(QUOTES);
	}
	
	private void printInt(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		if (numberFormat == NumberFormat.HEX) {}
		printValue(setting.getValue(), writer);
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
		Setting[] values = setting.getValue();
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				writer.write(COMMA);
				writer.write(" ");
			}
			printSetting(values[i], writer);
		}
		writer.write(RBRACKET);
		depth--;
	} 
	
	private void printList(Setting setting, Writer writer) throws IOException {
		printName(setting, writer);
		depth++;
		writer.write(LPAREN);
		boolean innerComplexType = false; 
		List<Setting> settings = setting.getValue();
		for (int i = 0; i < settings.size(); i++) {
			Setting subSetting = settings.get(i);
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
		List<Setting> settings = setting.getValue();
		for (int i = 0; i < settings.size(); i++) {
			if (i > 0) {
				writer.write(COMMA);
				writer.write(" ");
			}
			writer.write(getIndent());
			printSetting(settings.get(i), writer);
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
