package test;

import java.net.HttpCookie;

import util.common.cookie.CookieManager;
import util.common.cookie.CookieManagerImpl;

public class Test_CookieManager {

    /**
     * @param args
     */
    public static void main(String[] args) {
	CookieManager c = new CookieManagerImpl();
	String headerName = "Set-Cookie";
	new java.net.CookieManager();

	String c1 = "JSESSIONID=1AA1CD11BB11BB9CE62BB339F680B031; Path=/hwe";
	String c2 = "LSID=DQAAAK…Eaem_vYg; Domain=docs.foo.com; Path=/accounts; Expires=Wed, 13 Jan 2021 22:23:01 GMT; Secure; HttpOnly";

	c.addCookieFromHeader(headerName, c1);
	c.addCookieFromHeader(headerName, c2);

	HttpCookie jSessionId = c.getCookie("JSESSIONID");
	System.out.println(jSessionId.getName());
	System.out.println(jSessionId.getValue());
	System.out.println(jSessionId.getPath());

	HttpCookie LSID = c.getCookie("LSID");
	System.out.println(LSID.getName());
	System.out.println(LSID.getValue());
	System.out.println(LSID.getPath());

	// String[] a = c2.split(";");
	// for (String s : a) {
	// String[] b = s.trim().split("=");
	// for (int k = 0; k < b.length; k++) {
	// System.out.println("b[" + k + "]=" + b[k]);
	// }
	// }

    }

}
