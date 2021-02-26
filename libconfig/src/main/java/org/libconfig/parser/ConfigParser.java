/* ConfigParser.java */
/* Generated By:JavaCC: Do not edit this line. ConfigParser.java */
package org.libconfig.parser;

import java.util.ArrayList;
import java.util.List;

import org.libconfig.Config;
import org.libconfig.Setting;

/**
 *
 */
public class ConfigParser implements ConfigParserConstants {

	private ParserContext parserCtx;

	final public Config buildConfiguration() throws ParseException {
		parserCtx = new ParserContext(new Config());
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case NAME_TOKEN: {
			setting();
			label_1: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case NAME_TOKEN: {
					;
					break;
				}
				default:
					jj_la1[0] = jj_gen;
					break label_1;
				}
				setting();
			}
			break;
		}
		default:
			jj_la1[1] = jj_gen;
			;
		}
		jj_consume_token(0);
		{
			if ("" != null) {
				return parserCtx.getConfig();
			}
		}
		throw new Error("Missing return statement in function");
	}

	final private void setting() throws ParseException {
		jj_consume_token(NAME_TOKEN);
		final String name = token.image;
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case COLON: {
			jj_consume_token(COLON);
			break;
		}
		case EQUALS: {
			jj_consume_token(EQUALS);
			break;
		}
		default:
			jj_la1[2] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
		value(name);
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case SEMICOLON: {
			jj_consume_token(SEMICOLON);
			break;
		}
		case COMMA: {
			jj_consume_token(COMMA);
			break;
		}
		default:
			jj_la1[3] = jj_gen;

		}
	}

	final private void valueList(final String name) throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case BOOLEAN_TOKEN:
		case INTEGER_TOKEN:
		case HEX_INTEGER:
		case FLOATING_POINT_TOKEN:
		case STRING_TOKEN:
		case LPAREN:
		case LBRACE:
		case LBRACKET: {
			value(name);
			label_2: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case COMMA: {
					;
					break;
				}
				default:
					jj_la1[4] = jj_gen;
					break label_2;
				}
				jj_consume_token(COMMA);
				value(name);
			}
			break;
		}
		default:
			jj_la1[5] = jj_gen;
			;
		}
	}

	final private void value(final String name) throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case BOOLEAN_TOKEN:
		case INTEGER_TOKEN:
		case HEX_INTEGER:
		case FLOATING_POINT_TOKEN:
		case STRING_TOKEN: {
			scalarValue(name, null);
			break;
		}
		case LBRACKET: {
			arrayValue(name);
			break;
		}
		case LPAREN: {
			listValue(name);
			break;
		}
		case LBRACE: {
			groupValue(name);
			break;
		}
		default:
			jj_la1[6] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void scalarValueList(final String name, final List<Object> values) throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case BOOLEAN_TOKEN:
		case INTEGER_TOKEN:
		case HEX_INTEGER:
		case FLOATING_POINT_TOKEN:
		case STRING_TOKEN: {
			scalarValue(name, values);
			label_3: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case COMMA: {
					;
					break;
				}
				default:
					jj_la1[7] = jj_gen;
					break label_3;
				}
				jj_consume_token(COMMA);
				scalarValue(name, values);
			}
			break;
		}
		default:
			jj_la1[8] = jj_gen;
			;
		}
	}

	final private void scalarValue(final String name, final List<Object> values) throws ParseException {
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case BOOLEAN_TOKEN: {
			jj_consume_token(BOOLEAN_TOKEN);
			final Boolean booleanValue = Boolean.parseBoolean(token.image);
			addValueOrSettings(name, booleanValue, values);
			break;
		}
		case INTEGER_TOKEN: {
			jj_consume_token(INTEGER_TOKEN);
			final Integer integerValue = Integer.parseInt(token.image);
			addValueOrSettings(name, integerValue, values);
			break;
		}
		case HEX_INTEGER: {
			jj_consume_token(HEX_INTEGER);
			final String cuttedStringValue = token.image.substring(2, token.image.length());
			final Integer hexIntValue = Integer.parseInt(cuttedStringValue, 16);
			addValueOrSettings(name, hexIntValue, values);
			break;
		}
		case FLOATING_POINT_TOKEN: {
			jj_consume_token(FLOATING_POINT_TOKEN);
			final Double doubleValue = Double.parseDouble(token.image);
			addValueOrSettings(name, doubleValue, values);
			break;
		}
		case STRING_TOKEN: {
			jj_consume_token(STRING_TOKEN);
			String stringValue = token.image;
			stringValue = stringValue.replaceAll("\u005c"", "");
			addValueOrSettings(name, stringValue, values);
			break;
		}
		default:
			jj_la1[9] = jj_gen;
			jj_consume_token(-1);
			throw new ParseException();
		}
	}

	final private void addValueOrSettings(final String name, final Object value, final List<Object> values)
			throws ParseException {
		if (values != null) {
			values.add(value);
		} else {
			parserCtx.addScalarSetting(name, value);
		}
	}

	final private void arrayValue(final String name) throws ParseException {
		final ArrayList<Object> values = new ArrayList<>();
		jj_consume_token(LBRACKET);
		scalarValueList(null, values);
		jj_consume_token(RBRACKET);
		parserCtx.addArraySetting(name, values.toArray(new Object[values.size()]));
	}

	final private void listValue(final String name) throws ParseException {
		final Setting listSetting = parserCtx.addListSetting(name);
		parserCtx.setParent(listSetting);
		jj_consume_token(LPAREN);
		valueList(null);
		jj_consume_token(RPAREN);
		parserCtx.setParent(listSetting.getParent());
	}

	final private void groupValue(final String name) throws ParseException {
		final Setting groupSetting = parserCtx.addGroupSetting(name);
		parserCtx.setParent(groupSetting);
		jj_consume_token(LBRACE);
		switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
		case NAME_TOKEN: {
			setting();
			label_4: while (true) {
				switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
				case NAME_TOKEN: {
					;
					break;
				}
				default:
					jj_la1[10] = jj_gen;
					break label_4;
				}
				setting();
			}
			break;
		}
		default:
			jj_la1[11] = jj_gen;
			;
		}
		jj_consume_token(RBRACE);
		parserCtx.setParent(groupSetting.getParent());
	}

	/** Generated Token Manager. */
	public ConfigParserTokenManager	token_source;
	SimpleCharStream				jj_input_stream;
	/** Current token. */
	public Token					token;
	/** Next token. */
	public Token					jj_nt;
	private int						jj_ntk;
	private int						jj_gen;
	final private int[]				jj_la1	= new int[12];
	static private int[]			jj_la1_0;
	static private int[]			jj_la1_1;
	static {
		jj_la1_init_0();
		jj_la1_init_1();
	}

	private static void jj_la1_init_0() {
		jj_la1_0 = new int[] { 0x800000, 0x800000, 0x0, 0x0, 0x0, 0x544a9000, 0x544a9000, 0x0, 0x4a9000, 0x4a9000,
				0x800000, 0x800000, };
	}

	private static void jj_la1_init_1() {
		jj_la1_1 = new int[] { 0x0, 0x0, 0xa, 0x5, 0x4, 0x0, 0x0, 0x4, 0x0, 0x0, 0x0, 0x0, };
	}

	/** Constructor with InputStream. */
	public ConfigParser(final java.io.InputStream stream) {
		this(stream, null);
	}

	/** Constructor with InputStream and supplied encoding */
	public ConfigParser(final java.io.InputStream stream, final String encoding) {
		try {
			jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
		} catch (final java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source = new ConfigParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	/** Reinitialise. */
	public void ReInit(final java.io.InputStream stream) {
		ReInit(stream, null);
	}

	/** Reinitialise. */
	public void ReInit(final java.io.InputStream stream, final String encoding) {
		try {
			jj_input_stream.ReInit(stream, encoding, 1, 1);
		} catch (final java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	/** Constructor. */
	public ConfigParser(final java.io.Reader stream) {
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new ConfigParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	/** Reinitialise. */
	public void ReInit(final java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	/** Constructor with generated Token Manager. */
	public ConfigParser(final ConfigParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	/** Reinitialise. */
	public void ReInit(final ConfigParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 12; i++) {
			jj_la1[i] = -1;
		}
	}

	private Token jj_consume_token(final int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null) {
			token = token.next;
		} else {
			token = token.next = token_source.getNextToken();
		}
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	/** Get the next Token. */
	final public Token getNextToken() {
		if (token.next != null) {
			token = token.next;
		} else {
			token = token.next = token_source.getNextToken();
		}
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	/** Get the specific Token. */
	final public Token getToken(final int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null) {
				t = t.next;
			} else {
				t = t.next = token_source.getNextToken();
			}
		}
		return t;
	}

	private int jj_ntk_f() {
		if ((jj_nt = token.next) == null) {
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		} else {
			return (jj_ntk = jj_nt.kind);
		}
	}

	private final java.util.List<int[]>	jj_expentries	= new java.util.ArrayList<>();
	private int[]						jj_expentry;
	private int							jj_kind			= -1;

	/** Generate ParseException. */
	public ParseException generateParseException() {
		jj_expentries.clear();
		final boolean[] la1tokens = new boolean[36];
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 12; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
					if ((jj_la1_1[i] & (1 << j)) != 0) {
						la1tokens[32 + j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 36; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.add(jj_expentry);
			}
		}
		final int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = jj_expentries.get(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	/** Enable tracing. */
	final public void enable_tracing() {
	}

	/** Disable tracing. */
	final public void disable_tracing() {
	}

}
