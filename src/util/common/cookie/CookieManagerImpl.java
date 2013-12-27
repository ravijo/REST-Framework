package util.common.cookie;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

public class CookieManagerImpl implements CookieManager {

    private final static String SET_COOKIE = "set-cookie";
    private final static String DOMAIN = "Domain";
    private final static String PATH = "Path";
    private final static String SECURE = "Secure";
    private final static String MAX_AGE = "Max-Age";
    private final static String COMMENT = "Comment";
    private final static String VERSION = "Version";

    private final static String COOKIES_SPLIT_BY = ";";
    private final static String COOKIE_KEYVALUE_SPLIT_BY = "=";

    private static final Logger logger = Logger
	    .getLogger(CookieManagerImpl.class);

    private final Set<HttpCookie> cookies;

    public CookieManagerImpl() {
	cookies = new HashSet<HttpCookie>();
    }

    @Override
    public void addAllCookies(List<HttpCookie> c) {
	boolean status = cookies.addAll(c);
	logger.debug("Adding all cookies (" + Arrays.toString(c.toArray())
		+ ") status(" + status + ")");
    }

    @Override
    public void addCookie(HttpCookie c) {
	boolean status = cookies.add(c);
	logger.debug("Adding cookie (" + c + ") status(" + status + ")");
    }

    @Override
    public void addCookieFromHeader(final String headerName,
	    final String headerValue) {
	/*
	 * Set-Cookie=JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/hwe .
	 * In the this header, header name is Set-Cookie and headerValue is
	 * JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/hwe . Note that
	 * the header value contains one cookie which is JSESSIONID and its Path
	 * is /hwe
	 */

	// Set-Cookie=JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/hwe
	// Set-Cookie=JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/
	// Version=1;
	// Comment="SetCookie Counter";
	// Domain="localhost";
	// Max-Age=86400;
	// Expires=Thu, 15-Aug-2013 20:19:19 GMT;
	// Path=/cookie/SetCookie

	// We are interested in set-cookie ONLY
	if (headerName.toLowerCase(Locale.ENGLISH).startsWith(SET_COOKIE)) {
	    // This is set-cookie header

	    HttpCookie cookie = createCookie(headerValue);
	    addCookie(cookie);
	}
    }

    private HttpCookie createCookie(final String setCookie) {
	// JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/hwe

	String[] keyValuePairs = setCookie.split(COOKIES_SPLIT_BY);

	// The leading string in cookie is always cookie name
	String[] cookieNameValuePair = splitCookieKeyValue(keyValuePairs[0]);
	String cookieName = cookieNameValuePair[0];
	String cookieValue = cookieNameValuePair[1];

	HttpCookie cookie = new HttpCookie(cookieName, cookieValue);

	int len = keyValuePairs.length;
	if (len > 1) {
	    for (int i = 1; i < len; i++) {
		String[] keyValuePair = splitCookieKeyValue(keyValuePairs[i]);
		cookie = setProperty(cookie, keyValuePair);
	    }
	}

	return cookie;
    }

    @Override
    public Set<HttpCookie> getAllCookies() {
	return cookies;
    }

    @Override
    public HttpCookie getCookie(String cookieName) {
	for (HttpCookie cookie : cookies) {
	    if (cookie.getName().equalsIgnoreCase(cookieName)) {
		return cookie;
	    }
	}

	throw new NullPointerException("No cookie exists with name ("
		+ cookieName + ")");
    }

    @Override
    public void resetCookieStore() {
	cookies.clear();
    }

    private HttpCookie setProperty(HttpCookie cookie, String[] prop) {

	String name = prop[0];

	String value = prop.length > 1 ? prop[1] : "";

	if (name.equalsIgnoreCase(DOMAIN)) {
	    cookie.setDomain(value);
	}

	else if (name.equalsIgnoreCase(PATH)) {
	    cookie.setPath(value);
	}

	else if (name.equalsIgnoreCase(VERSION)) {
	    cookie.setVersion(Integer.parseInt(value));
	}

	else if (name.equalsIgnoreCase(COMMENT)) {
	    cookie.setComment(value);
	}

	else if (name.equalsIgnoreCase(MAX_AGE)) {
	    cookie.setMaxAge(Long.parseLong(value));
	}

	else if (name.equalsIgnoreCase(SECURE)) {
	    cookie.setSecure(true);
	} else {
	    logger.debug("Unable to identify the cookie property name (" + name
		    + ") value (" + value + ")");
	}

	return cookie;
    }

    private String[] splitCookieKeyValue(String keyValuePair) {
	return keyValuePair.trim().split(COOKIE_KEYVALUE_SPLIT_BY);
    }

}
