package util.json.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.common.Constants;
import util.common.Util;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;

public class JSONParserImpl implements JSONParser {

    private static Pattern arraySpliter = null;
    private final JsonObject json;

    public JSONParserImpl(Reader rdr) throws IOException, ParseException {
	JsonObject json = JsonObject.readFrom(rdr);
	this.json = json;
	initRegex();
    }

    public JSONParserImpl(String str) throws ParseException {
	JsonObject json = JsonObject.readFrom(str);
	this.json = json;
	initRegex();
    }

    private Object getValue(JsonValue jv) throws UnsupportedOperationException {

	if (!Util.isNotEmpty(jv)) {
	    throw new NullPointerException("Input key contains invalid path.");
	} else if (jv.isObject()) {
	    return jv.asObject();
	} else if (jv.isBoolean()) {
	    return jv.asBoolean();
	} else if (jv.isArray()) {
	    return jv.asArray();
	} else if (jv.isString()) {
	    return jv.asString();
	} else if (jv.isNumber()) {
	    return jv.toString();
	} else {
	    throw new IllegalArgumentException("Provided JSON value (" + jv
		    + ") is not supposed.");
	}
    }

    private Object getValueFromArray(JsonObject jsonObject, String key) {
	// If the key is array[1], so arrayName=array and arrayIndex=1
	String arrayName = "";
	int arrayIndex = -1;

	Matcher matcher = arraySpliter.matcher(key);
	if (matcher.find()) {
	    arrayName = matcher.group(1);
	    try {
		arrayIndex = Integer.parseInt(matcher.group(2));
	    } catch (NumberFormatException e) {
		arrayIndex = -1;
	    }
	}

	if (arrayIndex == -1) {
	    // JSON is not an array
	    return getValue(jsonObject.get(key));
	} else {
	    // JSON is an array
	    JsonArray array = (JsonArray) getValue(jsonObject.get(arrayName));
	    return getValue(array.get(arrayIndex));
	}
    }

    private void initRegex() {
	if (!Util.isNotEmpty(arraySpliter)) {
	    arraySpliter = Pattern.compile(Constants.ARRAY_SPLITER_REGEX);
	}
    }

    @Override
    public String parse(String key) {

	String[] keys = key.split(Constants.SPLIT_BY_DOT);
	int len = keys.length;

	if (len == 0) {
	    throw new IllegalArgumentException("Input key is empty.");
	} else if (len == 1) {
	    return (String) getValueFromArray(json, keys[0]);
	} else {
	    JsonObject rootNode = (JsonObject) getValueFromArray(json, keys[0]);
	    for (int i = 1; i < len - 1; i++) {
		JsonObject childNode = (JsonObject) getValueFromArray(rootNode,
			keys[i]);
		rootNode = childNode;
	    }
	    return (String) getValueFromArray(rootNode, keys[len - 1]);
	}
    }
}
