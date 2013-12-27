package util.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.common.cache.CacheManager;
import util.common.cookie.CookieManager;
import util.common.cookie.CookieManagerImpl;
import util.config.reader.elements.spreadSheet.Attribute;

/**
 * @author ravij
 * 
 */
public final class Util {
    private static final Logger logger = Logger.getLogger(Util.class);

    private static CookieManager cookieManager = null;

    private static final Pattern varFinder = Pattern
	    .compile(Constants.VARIABLE_FINDER_REGEX);

    private static Map<String, String> restClientProperties;

    /**
     * This method configures the RestClient.
     */
    public static void configureRestClient() {

	final File restClientPropertiesFile = new File(
		Constants.REST_CLIENT_PROPERTIES);

	if (restClientPropertiesFile.exists()) {
	    boolean deleted = restClientPropertiesFile.delete();
	    logger.debug("Deleting existing Rest Client Properties ("
		    + restClientPropertiesFile.getAbsolutePath() + ") file ("
		    + deleted + ")");
	} else {
	    boolean mkdirs = restClientPropertiesFile.getParentFile().mkdirs();
	    logger.debug("Creating parent directory for Rest Client Properties ("
		    + restClientPropertiesFile.getAbsolutePath()
		    + ") file ("
		    + mkdirs + ")");
	}

	final Properties prop = new Properties();

	try {
	    // set the properties value
	    prop.putAll(restClientProperties);

	    // save properties to project root folder
	    prop.store(new FileOutputStream(restClientPropertiesFile),
		    "Customized RESTClient Properties");

	} catch (IOException e) {
	    logger.error("Error while configuring Rest Client Properties ("
		    + restClientPropertiesFile.getAbsolutePath() + ") file. ("
		    + e.getMessage() + ")");
	}
    }

    public static String fillFromCache(String str) {

	String varName = null;

	/*
	 * Index variables keep track of parsed string
	 */
	int index = 0;
	int newIndex = 0;

	while (isNotEmpty(varName = getVarName(str))
		&& (newIndex = str.indexOf(varName, index) + varName.length()) > index) {

	    String varValue = CacheManager.getValue(varName);

	    logger.debug("Found a cache variable (" + varName
		    + ") in content (" + str + ") which has value (" + varValue
		    + ") ");

	    str = findAndReplaceAll(str, varName, varValue);

	    index = newIndex;
	}

	return !isNotEmpty(str) ? "" : str;
    }

    public static String fillValue(final String str, final Attribute attribute) {

	String attributeName = attribute.getName();
	String attributeValue = attribute.getValue();

	return findAndReplaceAll(str, attributeName, attributeValue);
    }

    public static String fillValues(final String str,
	    final List<Attribute> attributes) {

	String temp = str;
	for (Attribute attribute : attributes) {
	    temp = fillValue(temp, attribute);
	}

	return fillFromCache(temp);
    }

    private static String findAndReplaceAll(final String content,
	    final String strToFind, final String strToReplace) {

	if (strToReplace != null && Util.isNotEmpty(strToFind)
		&& content.contains(strToFind)) {

	    logger.debug("Replacing (" + strToFind + ") with (" + strToReplace
		    + ") in string (" + content + ")");

	    return content.replaceAll(Constants.VARIABLE_PREFIX + strToFind,
		    strToReplace);
	} else {
	    return content;
	}
    }

    /**
     * @param throwable
     * @return
     */
    public static String getStatckTrace(final Throwable throwable) {

	if (!isNotEmpty(throwable)) {
	    return "";
	}

	final Writer writer = new StringWriter();
	throwable.printStackTrace(new PrintWriter(writer));
	return writer.toString();
    }

    /**
     * @param content
     * @return variable name in the content
     */
    private static String getVarName(final String content) {
	// {"name":"$user"}

	Matcher matcher = varFinder.matcher(content);
	if (matcher.find()) {
	    return matcher.group(1);
	}

	return null;
    }

    /**
     * If the input string is /hwe/application/ it will return hwe/application
     * 
     * @param str
     * @return string which is not surrounded by slash.
     */
    public static String getWithoutSurroundedBySlash(final String str) {
	char[] arr = str.toCharArray();
	int start = 0;
	int count = arr.length;
	int last = count - 1;

	if (arr[0] == '/') {
	    start++;
	    count--;
	}

	if (arr[last] == '/') {
	    count--;
	}

	return new String(arr, start, count);
    }

    public static void initCookieManager() {
	cookieManager = new CookieManagerImpl();
	CacheManager.init(cookieManager);
    }

    public static Set<HttpCookie> getAllCookies() {
	return cookieManager.getAllCookies();
    }

    public static CookieManager getCookieManager() {
	return cookieManager;
    }

    public static void addAllCookies(List<HttpCookie> c) {
	cookieManager.addAllCookies(c);
    }

    /**
     * This method is used to Initializing logger
     */
    public static void initLogger() {

	String logDir = System.getProperty("user.dir") + Constants.SLASH
		+ "logs" + Constants.SLASH;
	System.setProperty("logDir", logDir);

	PropertyConfigurator.configure(Constants.LOG4J_CONFIG_FILE);

	logger.debug("Initializing logger using config file ("
		+ Constants.LOG4J_CONFIG_FILE + ") log directory (" + logDir
		+ ")");

    }

    public static boolean isNotEmpty(final Object obj) {
	if (obj == null) {
	    return false;
	}

	else if (obj instanceof String) {
	    return !((String) obj).trim().isEmpty();
	}

	return true;
    }

    /**
     * This method check the input string
     * 
     * @param str
     * @return true, only if the str is either true or yes or 1
     */
    public static boolean isTrue(final String str) {
	final String trimmed = str.trim();
	String lowerCase = trimmed.toLowerCase();

	return !trimmed.isEmpty()
		&& (lowerCase.equals("true") || lowerCase.equals("yes") || lowerCase
			.equals("1"));
    }

    /**
     * @param properties
     */
    public static void setRestClientProperties(
	    final Map<String, String> properties) {
	restClientProperties = properties;
    }

    /**
     * 
     */
    public static void shutDownLogger() {
	logger.debug("Shutting down logger.");
	LogManager.shutdown();
    }

    public static void addToCache(List<Attribute> attributes) {
	for (Attribute attribute : attributes) {
	    addToCache(attribute.getName(), attribute.getValue());
	}
    }

    public static void addToCache(String key, String value) {
	CacheManager.store(key, value);
    }
}