package util.config.reader.elements;

import util.config.reader.elements.spreadSheet.TestCase;

public class ScenarioImpl implements Scenario {

    private final String serviceName;
    private final String serviceUrl;
    private final String requestType;
    private final String contentStructure;
    private final String contentType;
    private final TestCase testCase;
    private String url;
    private String content;

    public ScenarioImpl(String serviceName, String serviceUrl,
	    String requestType, String contentStructure, String contentType,
	    TestCase testCase) {
	this.serviceName = serviceName;
	this.serviceUrl = serviceUrl;
	this.requestType = requestType;
	this.contentStructure = contentStructure;
	this.contentType = contentType;
	this.testCase = testCase;
    }

    /**
     * @return the content
     */
    @Override
    public String getContent() {
	return content;
    }

    /**
     * @return the contentStructure
     */
    @Override
    public String getContentStructure() {
	return contentStructure;
    }

    /**
     * @return the contentType
     */
    @Override
    public String getContentType() {
	return contentType;
    }

    /**
     * @return the requestType
     */
    @Override
    public String getRequestType() {
	return requestType;
    }

    /**
     * @return the serviceName
     */
    @Override
    public String getServiceName() {
	return serviceName;
    }

    /**
     * @return the serviceUrl
     */
    @Override
    public String getServiceUrl() {
	return serviceUrl;
    }

    /**
     * @return the testCase
     */
    @Override
    public TestCase getTestCase() {
	return testCase;
    }

    /**
     * @return the url
     */
    @Override
    public String getUrl() {
	return url;
    }

    /**
     * @param content
     *            the content to set
     */
    @Override
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * @param url
     *            the url to set
     */
    @Override
    public void setUrl(String url) {
	this.url = url;
    }

    @Override
    public String toString() {
	return serviceName + " " + testCase.getName();
    }

}
