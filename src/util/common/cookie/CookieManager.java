package util.common.cookie;

import java.net.HttpCookie;
import java.util.List;
import java.util.Set;

/**
 * @author ravij
 * 
 */
public interface CookieManager {

    void addAllCookies(List<HttpCookie> c);

    void addCookie(HttpCookie c);

    void addCookieFromHeader(String headerName, String headerValue);

    Set<HttpCookie> getAllCookies();

    HttpCookie getCookie(String cookieName);

    void resetCookieStore();
}