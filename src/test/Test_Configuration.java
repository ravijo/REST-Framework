package test;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import util.common.Util;
import util.config.reader.elements.ConfigurationImpl;
import util.config.reader.elements.Scenario;

public class Test_Configuration {
    private static final Logger logger = Logger
	    .getLogger(Test_Configuration.class);

    public static void main(String[] args) {
	Util.initLogger();

	List<Scenario> s = new ConfigurationImpl().getScenarios();

	logger.info("Below are the Scenarios-\n" + Arrays.toString(s.toArray())
		+ "\n\n\n\n");

	Test_Configuration tc = new Test_Configuration();
	for (Scenario sc : s) {
	    tc.printInfo(sc);
	    tc.getScenario(sc);
	    tc.getContent(sc);
	    tc.getContentType(sc);
	    tc.getExpectedAttribute(sc);
	    tc.getExpectedHTTPResponseCode(sc);
	    tc.getHTTPMethodType(sc);
	    tc.getURL(sc);
	    tc.getExecuteIt(sc);
	    tc.getTestCase(sc);
	    tc.getAuthorizedUser(sc);
	    tc.getCacheAttributeName(sc);
	    tc.getCacheAttributeCustomName(sc);
	}

    }

    private void getAuthorizedUser(Scenario scenario) {
	logger.info("getAuthorizedUser="
		+ scenario.getTestCase().isAuthorizedUser());
    }

    private void getCacheAttributeCustomName(Scenario scenario) {
	logger.info("getCacheAttributeCustomName="
		+ scenario.getTestCase().getCacheAttributeCustomName());
    }

    private void getCacheAttributeName(Scenario scenario) {
	logger.info("getCacheAttributeName="
		+ scenario.getTestCase().getCacheAttributeName());
    }

    private void getContent(Scenario scenario) {
	logger.info("getContent=" + scenario.getContent());
    }

    private void getContentType(Scenario scenario) {
	logger.info("getContentType=" + scenario.getContentType());
    }

    private void getExecuteIt(Scenario scenario) {
	logger.info("getExecuteIt=" + scenario.getTestCase().isExecutable());
    }

    private void getExpectedAttribute(Scenario scenario) {
	logger.info("getExpectedAttribute="
		+ scenario.getTestCase().getExpectedAttribute());
    }

    private void getExpectedHTTPResponseCode(Scenario scenario) {
	logger.info("getExpectedHTTPResponseCode="
		+ scenario.getTestCase().getExpectedHttpResponseCode());
    }

    private void getHTTPMethodType(Scenario scenario) {
	logger.info("getHTTPMethodType=" + scenario.getRequestType());
    }

    private void getScenario(Scenario scenario) {
	logger.info("getScenario=" + scenario);
    }

    private void getTestCase(Scenario scenario) {
	logger.info("getTestCase=" + scenario.getTestCase());
    }

    private void getURL(Scenario scenario) {
	logger.info("getURL=" + scenario.getUrl());
    }

    private void printInfo(Scenario scenario) {
	logger.info("\nScenario=" + scenario + "\nContent="
		+ scenario.getContent());
    }
}
