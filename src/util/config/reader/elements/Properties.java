package util.config.reader.elements;

import java.util.List;

import util.config.reader.elements.spreadSheet.Attribute;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 21, 2013 | 12:13:53 AM
 * 
 * @author ravij
 * @since
 */
public interface Properties {

    void addAttribute(Attribute e);

    Attribute getAttributeByName(String attributeName);

    List<Attribute> getAttributes();

    String getSheetName();
}
