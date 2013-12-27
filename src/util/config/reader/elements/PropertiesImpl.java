package util.config.reader.elements;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.config.reader.elements.spreadSheet.Attribute;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 21, 2013 | 12:13:53 AM
 * 
 * @author ravij
 * @since
 */
public class PropertiesImpl implements Properties {
    private static final Logger logger = Logger.getLogger(PropertiesImpl.class);
    private String sheetName;
    private List<Attribute> attributes;

    public PropertiesImpl(String sheetName) {
	this.sheetName = sheetName;
	this.attributes = new ArrayList<Attribute>();
    }

    public PropertiesImpl(String sheetName, Attribute e) {
	this.sheetName = sheetName;
	this.attributes = new ArrayList<Attribute>();
	addAttribute(e);
    }

    @Override
    public void addAttribute(Attribute e) {
	this.attributes.add(e);
    }

    /**
     * @param attributeName
     * @return Attribute
     */
    @Override
    public Attribute getAttributeByName(String attributeName) {

	for (Attribute attribute : attributes) {
	    if (attribute.getName().equals(attributeName)) {
		return attribute;
	    }
	}
	logger.error("Unable to find attribute (" + attributeName
		+ ") in sheet (" + this.sheetName + ")");
	return null;
    }

    /**
     * Returns the
     * 
     * @return the attributes
     */
    @Override
    public List<Attribute> getAttributes() {
	return attributes;
    }

    /**
     * Returns the
     * 
     * @return the sheetName
     */
    @Override
    public String getSheetName() {
	return sheetName;
    }

}
