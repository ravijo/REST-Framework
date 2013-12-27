package test;

import org.apache.log4j.Logger;

import util.common.Util;
import util.common.cache.CacheManager;
import util.common.cookie.CookieManagerImpl;

public class Test_CacheManager {

    private static final Logger logger = Logger
	    .getLogger(Test_CacheManager.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
	new Test_CacheManager().test();
    }

    public Test_CacheManager() {
	Util.initLogger();
    }

    private void test() {
	CacheManager.init(new CookieManagerImpl());

	String hi = CacheManager.getValue("hi");
	logger.info(hi);
	System.out.println("hi=" + hi);

	CacheManager.store("ram", "shyam");

	String ram = CacheManager.getValue("ram");
	logger.info(ram);

	CacheManager.resetCache();

	String hello = CacheManager.getValue("hello");
	logger.info(hello);
    }

}
