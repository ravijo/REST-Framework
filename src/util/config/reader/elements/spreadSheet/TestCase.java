package util.config.reader.elements.spreadSheet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.common.Util;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:46:40 PM
 * 
 * @author ravij
 * @since
 */
public class TestCase {
    private static final Logger logger = Logger.getLogger(TestCase.class);
    private String name;
    private int executionOrder = 0;
    private boolean authorizedUser;
    private boolean executable = true;
    private int expectedHttpResponseCode;
    private List<Attribute> inputAttributes;
    private Attribute expectedAttribute;
    private String cacheAttributeName;
    private String cacheAttributeCustomName;

    /**
     * Constructor
     * 
     * @param name
     * @param attribute
     */
    public TestCase(String name, Attribute inputAttribute) {
	init();
	addInputAttribute(inputAttribute);
    }

    /**
     * Constructor
     * 
     * @param name
     * @param attribute
     */
    public TestCase(String name, Attribute inputAttribute, int executionOrder,
	    boolean executable) {
	this.name = name;
	this.executable = executable;
	this.executionOrder = executionOrder;

	init();
	addInputAttribute(inputAttribute);
    }

    /**
     * Constructor
     * 
     * @param name
     * @param attribute
     */
    public TestCase(String name, boolean authorizedUser, int executionOrder,
	    boolean executable, Attribute inputAttribute,
	    Attribute expetedAttribute, int expectedHttpResponseCode,
	    String cacheAttributeName, String cacheAttributeCustomName) {
	this.name = name;
	this.authorizedUser = authorizedUser;
	this.executable = executable;
	this.executionOrder = executionOrder;
	this.expectedHttpResponseCode = expectedHttpResponseCode;

	init();
	addInputAttribute(inputAttribute);
	setExpectedAttribute(expetedAttribute);
	setCacheAttributeName(cacheAttributeName);
	setCacheAttributeCustomName(cacheAttributeCustomName);
    }

    /**
     * @param e
     */
    public void addInputAttribute(Attribute e) {
	this.inputAttributes.add(e);
    }

    /**
     * @return the cacheAttributeCustomName
     */
    public String getCacheAttributeCustomName() {
	return cacheAttributeCustomName;
    }

    /**
     * @return the cacheAttributeName
     */
    public String getCacheAttributeName() {
	return cacheAttributeName;
    }

    /**
     * @return the executionOrder
     */
    public int getExecutionOrder() {
	return executionOrder;
    }

    public Attribute getExpectedAttribute() {
	return expectedAttribute;
    }

    public int getExpectedHttpResponseCode() {
	return expectedHttpResponseCode;
    }

    /**
     * @param attributeName
     * @return service
     */
    public Attribute getInputAttributeByName(String attributeName) {

	for (Attribute attribute : inputAttributes) {
	    if (attribute.getName().equals(attributeName)) {
		return attribute;
	    }
	}

	logger.error("Unable to find input attribute (" + attributeName
		+ ") for test case (" + this.name + ")");
	return null;
    }

    /**
     * @return the inputAttributes
     */
    public List<Attribute> getInputAttributes() {
	return inputAttributes;
    }

    /**
     * Returns the
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    private void init() {
	this.inputAttributes = new ArrayList<Attribute>();
    }

    /**
     * @return the authorizedUser
     */
    public boolean isAuthorizedUser() {
	return authorizedUser;
    }

    /**
     * @return the executable
     */
    public boolean isExecutable() {
	return executable;
    }

    /**
     * @param authorizedUser
     *            the authorizedUser to set
     */
    public void setAuthorizedUser(boolean authorizedUser) {
	this.authorizedUser = authorizedUser;
    }

    /**
     * @param cacheAttributeCustomName
     *            the cacheAttributeCustomName to set
     */
    public void setCacheAttributeCustomName(String cacheAttributeCustomName) {
	if (Util.isNotEmpty(cacheAttributeCustomName)) {
	    this.cacheAttributeCustomName = cacheAttributeCustomName;
	}
    }

    /**
     * @param cacheAttributeName
     *            the cacheAttributeName to set
     */
    public void setCacheAttributeName(String cacheAttributeName) {
	if (Util.isNotEmpty(cacheAttributeName)) {
	    this.cacheAttributeName = cacheAttributeName;
	}
    }

    /**
     * @param executable
     *            the executable to set
     */
    public void setExecutable(boolean executable) {
	this.executable = executable;
    }

    /**
     * @param executionOrder
     *            the executionOrder to set
     */
    public void setExecutionOrder(int executionOrder) {
	this.executionOrder = executionOrder;
    }

    public void setExpectedAttribute(Attribute expectedAttribute) {
	this.expectedAttribute = expectedAttribute;
    }

    public void setExpectedHttpResponseCode(int expectedHttpResponseCode) {
	this.expectedHttpResponseCode = expectedHttpResponseCode;
    }

    /**
     * @param inputAttributes
     *            the inputAttributes to set
     */
    public void setInputAttributes(List<Attribute> inputAttributes) {
	this.inputAttributes = inputAttributes;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }

}
