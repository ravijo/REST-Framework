package util.config.reader.elements.spreadSheet;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:51:07 PM
 * 
 * @author ravij
 * @since
 */
public class Service {
    private static final Logger logger = Logger.getLogger(Service.class);
    private String name;
    private String url;
    private String requestType;
    private String contentStructure;
    private String contentType;

    private List<TestCase> testCases;

    /**
     * Constructor
     * 
     * @param name
     */
    public Service(String name) {
	this.name = name;
    }

    /**
     * Constructor
     * 
     * @param name
     * @param url
     * @param requestType
     * @param contentStructure
     * @param contentType
     */
    public Service(String name, String url, String requestType,
	    String contentStructure, String contentType) {
	this.name = name;
	this.url = url;
	this.requestType = requestType;
	this.contentStructure = contentStructure;
	this.contentType = contentType;

    }

    public void addTestCases(List<TestCase> testCase) {
	this.testCases = testCase;
    }

    /**
     * @return the contentStructure
     */
    public String getContentStructure() {
	return contentStructure;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
	return contentType;
    }

    /**
     * Returns the
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the requestType
     */
    public String getRequestType() {
	return requestType;
    }

    /**
     * @param testCaseName
     * @return service
     */
    public TestCase getTestCaseByName(String testCaseName) {

	for (TestCase testCase : testCases) {
	    if (testCase.getName().equals(testCaseName)) {
		return testCase;
	    }
	}
	logger.error("Unable to find testcase (" + testCaseName
		+ ") for service (" + this.name + ")");
	return null;
    }

    /**
     * Returns the
     * 
     * @return the testCases
     */
    public List<TestCase> getTestCases() {
	return testCases;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param contentStructure
     *            the contentStructure to set
     */
    public void setContentStructure(String contentStructure) {
	this.contentStructure = contentStructure;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @param requestType
     *            the requestType to set
     */
    public void setRequestType(String requestType) {
	this.requestType = requestType;
    }

    /**
     * @param testCases
     *            the testCases to set
     */
    public void setTestCases(List<TestCase> testCases) {
	this.testCases = testCases;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    @Override
    public String toString() {
	return name;
    }

}
