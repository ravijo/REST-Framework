package test;

import org.apache.log4j.Logger;

import util.common.Util;

public class Test_Util {
    private static final Logger logger = Logger.getLogger(Test_Util.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
	Test_Util u = new Test_Util();
	u.initLoggerTest();
	u.isTrueTest();
	u.getWithoutSurroundedBySlashTest();
	u.isNotEmptyTest();
    }

    public Test_Util() {
	Util.initLogger();
    }

    private void getWithoutSurroundedBySlashTest() {
	String[] strings = { "/hwe", "hwe", "/hwe/", "/hwe/applicatioon/login",
		"hwe/applicatioon/login", "/hwe/applicatioon/login/" };
	for (String str : strings) {
	    String ans = Util.getWithoutSurroundedBySlash(str);
	    logger.info("string=" + str + ", WithoutSurroundedBySlash=" + ans);
	}
    }

    private void initLoggerTest() {
	logger.info("this is info.");
	logger.debug("this is debug");
	logger.warn("this is warn");
	logger.error("this is error");
	logger.fatal("this is fatal");
	logger.trace("this is trace");
    }

    private void isNotEmptyTest() {
	String[] strings = { "", "TRUE", "    ", " truE", "yes ", " Yes ",
		" Y E S ", null, "0", "No", "false", "False", "FALSE", "noway" };

	for (String str : strings) {
	    boolean isNotEmpty = Util.isNotEmpty(str);
	    logger.info("string=" + str + ", isNotEmpty=" + isNotEmpty);
	}

	logger.info("isNotEmpty=" + Util.isNotEmpty(null));

    }

    private void isTrueTest() {
	String[] strings = { "true", "TRUE", "True", "truE", "yes", "Yes",
		"YES", "1", "0", "No", "false", "False", "FALSE", "noway" };

	for (String str : strings) {
	    boolean isTrue = Util.isTrue(str);
	    logger.info("string=" + str + ", isTrue=" + isTrue);
	}
    }
}
