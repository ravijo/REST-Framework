package util.config.reader.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odftoolkit.simple.SpreadsheetDocument;

import util.common.Constants;
import util.common.Util;
import util.config.reader.ODSReader;
import util.config.reader.ODSReaderImpl;
import util.config.reader.elements.spreadSheet.Attribute;
import util.config.reader.elements.spreadSheet.Service;
import util.config.reader.elements.spreadSheet.TestCase;

public class ConfigurationImpl implements Configuration {

    private static final ODSReader ods = new ODSReaderImpl();
    private final List<Scenario> scenarios;
    private final Properties properties;
    private final String rootUrl;
    private static boolean sslEnable;

    /*
     * We don't need proxy to connect to server. Hence I am hard coding it.
     */
    private final static String REST_CLIENT_PROXY_OPTIONS_IS_ENABLED = "proxy.options.is_enabled";
    private final static String REST_CLIENT_PROXY_OPTIONS_HOST = "proxy.options.host";
    private final static String REST_CLIENT_PROXY_OPTIONS_PORT = "proxy.options.port";
    private final static String REST_CLIENT_PROXY_OPTIONS_IS_AUTH_ENABLED = "proxy.options.is_auth_enabled";
    private final static String REST_CLIENT_PROXY_OPTIONS_USERNAME = "proxy.options.username";
    private final static String REST_CLIENT_PROXY_OPTIONS_PASSWORD = "proxy.options.password";

    public ConfigurationImpl() {
	properties = ods.readProperties(Constants.CONFIG_FILE,
		Constants.PROPERTIES_SHEET_NAME);

	// root URL can't be created before reading the properties sheet
	rootUrl = createRootURL();

	Util.setRestClientProperties(getRestClientProperties());

	String ConfigurationImplSheet = properties.getAttributeByName(
		Constants.PROPERTY_SHEET_NAME).getValue();

	SpreadsheetDocument spreadSheet = ods.getLastSpreadSheet();

	SpreadSheet sheet = ods.readSpreadSheet(spreadSheet,
		ConfigurationImplSheet);

	scenarios = preProcessing(sheet);
    }

    private String createCompleteURL(Scenario scenario) {
	// http://192.168.6.126:8080/hwe/ui/framework/login/login.html
	String serviceUrl = scenario.getServiceUrl();
	List<Attribute> attributes = scenario.getTestCase()
		.getInputAttributes();

	serviceUrl = Util.fillValues(serviceUrl, attributes);

	return rootUrl + "/" + Util.getWithoutSurroundedBySlash(serviceUrl);
    }

    private String createContent(Scenario scenario) {
	String contentStructure = scenario.getContentStructure();

	List<Attribute> inputAttributes = scenario.getTestCase()
		.getInputAttributes();

	Util.addToCache(inputAttributes);

	return Util.fillValues(contentStructure, inputAttributes);
    }

    private String createRootURL() {
	// http://192.168.6.126:8080/hwe/ui/framework/login/login.html

	sslEnable = Util.isTrue(properties.getAttributeByName(
		Constants.PROPERTY_SECURE_BROWSING_ENABLED).getValue());

	final String protocol = sslEnable ? "https" : "http";

	final String ipOrHostname = properties.getAttributeByName(
		Constants.PROPERTY_SERVER_IP_HOST_NAME).getValue();

	final String port = properties.getAttributeByName(
		Constants.PROPERTY_SERVER_PORT).getValue();

	final String contextPath = properties.getAttributeByName(
		Constants.PROPERTY_CONTEXT_PATH).getValue();

	// http://192.168.6.126:8080/hwe
	return protocol + "://" + ipOrHostname + ":" + port + "/"
		+ Util.getWithoutSurroundedBySlash(contextPath);
    }

    private Map<String, String> getRestClientProperties() {
	final Map<String, String> restClientProperties = new HashMap<String, String>();

	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_IS_ENABLED, "false");
	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_HOST, "");
	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_PORT, "8080");
	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_IS_AUTH_ENABLED,
		"false");
	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_USERNAME, "");
	restClientProperties.put(REST_CLIENT_PROXY_OPTIONS_PASSWORD, "");

	String reqTimeout = properties.getAttributeByName(
		Constants.PROPERTY_CLIENT_REQ_TIMEOUT).getValue();

	restClientProperties.put(Constants.REST_CLIENT_REQ_TIMEOUT, reqTimeout);

	return restClientProperties;
    }

    /**
     * @return the scenarios
     */
    @Override
    public List<Scenario> getScenarios() {
	return scenarios;
    }

    private List<Scenario> preProcessing(final SpreadSheet sheet) {
	final List<Scenario> scenariosPhase1 = processExecuteIt(sheet);
	final List<Scenario> scenariosPhase2 = processExecutionOrder(scenariosPhase1);
	final List<Scenario> scenariosPhase3 = processEachScenario(scenariosPhase2);
	return scenariosPhase3;
    }

    private List<Scenario> processEachScenario(List<Scenario> scenarios) {

	for (Scenario scenario : scenarios) {

	    String url = createCompleteURL(scenario);
	    scenario.setUrl(url);

	    String content = createContent(scenario);
	    scenario.setContent(content);
	}

	return scenarios;
    }

    private List<Scenario> processExecuteIt(SpreadSheet sheet) {

	final List<Scenario> scenarios = new ArrayList<Scenario>();

	List<Service> services = sheet.getServices();
	Attribute followExecuteIt = properties
		.getAttributeByName(Constants.PROPERTY_FOLLOW_EXECUTE_IT);

	for (Service service : services) {

	    String serviceName = service.getName();
	    String serviceUrl = service.getUrl();
	    String requestType = service.getRequestType();
	    String contentStructure = service.getContentStructure();
	    String contentType = service.getContentType();

	    List<TestCase> allTcs = service.getTestCases();

	    for (TestCase testCase : allTcs) {

		if (Util.isTrue(followExecuteIt.getValue())) {

		    if (testCase.isExecutable()) {
			Scenario scenario = new ScenarioImpl(serviceName,
				serviceUrl, requestType, contentStructure,
				contentType, testCase);

			scenarios.add(scenario);
		    }
		} else {

		    Scenario scenario = new ScenarioImpl(serviceName,
			    serviceUrl, requestType, contentStructure,
			    contentType, testCase);

		    scenarios.add(scenario);
		}
	    }
	}

	return scenarios;
    }

    private List<Scenario> processExecutionOrder(List<Scenario> scenarios) {
	Attribute followExecutionOrder = properties
		.getAttributeByName(Constants.PROPERTY_FOLLOW_EXECUTION_ORDER);

	if (Util.isTrue(followExecutionOrder.getValue())) {

	    Collections.sort(scenarios, new Comparator<Scenario>() {

		@Override
		public int compare(Scenario s1, Scenario s2) {
		    Integer s1ExecutionOrder = new Integer(s1.getTestCase()
			    .getExecutionOrder());
		    Integer s2ExecutionOrder = new Integer(s2.getTestCase()
			    .getExecutionOrder());
		    return s1ExecutionOrder.compareTo(s2ExecutionOrder);
		}
	    });
	}

	return scenarios;
    }

    public static boolean isSSLEnable() {
	return sslEnable;
    }

}
