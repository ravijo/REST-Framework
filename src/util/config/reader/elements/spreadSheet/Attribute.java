package util.config.reader.elements.spreadSheet;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:47:13 PM
 * 
 * @author ravij
 * @since
 */
public class Attribute {

    private String name;
    private String value;

    /**
     * Constructor
     * 
     * @param name
     * @param value
     */
    public Attribute(String name, String value) {
	this.name = name;
	this.value = value;
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
     * Returns the
     * 
     * @return the value
     */
    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return "(" + name + "=" + value + ")";
    }
}
