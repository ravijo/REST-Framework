/**
 * 
 */
package util.common.cache;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import util.common.Util;
import util.common.cookie.CookieManager;

/**
 * @author ravij
 * 
 */
public class CacheManager {

    private static CookieManager cookieManager;
    private static final Map<String, String> cache = new HashMap<String, String>();

    private static final Logger logger = Logger.getLogger(CacheManager.class);

    public static void deleteValue(String key) {
	logger.debug("Retrieving value for key (" + key + ") from cache");
	cache.remove(key);
    }

    public static String getValue(String key) {
	logger.debug("Retrieving value for key (" + key + ") from cache");
	String value = cache.get(key);

	if (!Util.isNotEmpty(value)) {
	    logger.debug("No value found for the specified key (" + key
		    + ") in cache. Now checking cookies with cookie name ("
		    + key + ")");

	    try {
		HttpCookie cookie = cookieManager.getCookie(key);
		value = cookie.getValue();

		logger.debug("Found value (" + value + ") for name (" + key
			+ ") in cookies");

	    } catch (NullPointerException e) {
		logger.debug("No cookie found with the specified name (" + key
			+ "). " + e.getMessage());
	    }
	}

	return value;
    }

    /**
	 * 
	 */
    public static void init(CookieManager cm) {
	cookieManager = cm;
    }

    public static void resetCache() {
	logger.debug("Resetting cache");
	cache.clear();
    }

    public static void store(String key, String value) {
	if (Util.isNotEmpty(key)) {
	    logger.debug("Adding key (" + key + ") with value (" + value
		    + ") in cache");
	    cache.put(key, value);
	}

	else {
	    logger.debug("NULL or empty key can't be added into cache.");
	}
    }

}
