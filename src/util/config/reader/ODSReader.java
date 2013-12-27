package util.config.reader;

import org.odftoolkit.simple.SpreadsheetDocument;

import util.config.reader.elements.Properties;
import util.config.reader.elements.SpreadSheet;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:55:26 PM
 * 
 * @author ravij
 * @since
 */
public interface ODSReader {

    /**
     * Returns the last used spreadSheet.
     * <p>
     * Use this method, if parsing the last CONFIG_FILE again. First get the
     * spreadSheet then call
     * {@link #readConfigurations(SpreadsheetDocument, String)} for faster
     * response.
     * </p>
     * 
     * @return the spreadSheet
     */
    SpreadsheetDocument getLastSpreadSheet();

    Properties readProperties(SpreadsheetDocument spreadSheet, String sheetName);

    Properties readProperties(String file, String sheetName);

    SpreadSheet readSpreadSheet(SpreadsheetDocument spreadSheet,
	    String sheetName);

    SpreadSheet readSpreadSheet(String file, String sheetName);
}
