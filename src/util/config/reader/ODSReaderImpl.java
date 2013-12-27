package util.config.reader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import util.common.Constants;
import util.common.Util;
import util.config.reader.elements.Properties;
import util.config.reader.elements.PropertiesImpl;
import util.config.reader.elements.SpreadSheet;
import util.config.reader.elements.SpreadSheetImpl;
import util.config.reader.elements.spreadSheet.Attribute;
import util.config.reader.elements.spreadSheet.Service;
import util.config.reader.elements.spreadSheet.TestCase;

/**
 * <p>
 * </p>
 * <b>Created on</b>: Aug 19, 2013 | 7:55:26 PM
 * 
 * @author ravij
 * @since
 */
public class ODSReaderImpl implements ODSReader {

    private static final Logger logger = Logger.getLogger(ODSReaderImpl.class);

    private SpreadsheetDocument spreadSheet;

    private Table checkTableAvailability(String sheetname)
	    throws NullPointerException {
	Table table = spreadSheet.getTableByName(sheetname);
	if (!Util.isNotEmpty(table)) {
	    List<Table> allTables = spreadSheet.getTableList();

	    StringBuilder sb = new StringBuilder();

	    for (Table presentTable : allTables) {
		sb.append(presentTable.getTableName());
		sb.append(',');
	    }
	    sb.deleteCharAt(sb.length() - 1);

	    throw new NullPointerException("Sheet (" + sheetname
		    + ") doesn't exists. Following sheets are available- ("
		    + sb.toString() + ")");
	}
	return table;
    }

    private String getCellValueAt(Table sheet, int colIndex, int rowIndex) {
	return sheet.getCellByPosition(colIndex, rowIndex).getDisplayText();
    }

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
    @Override
    public SpreadsheetDocument getLastSpreadSheet() {
	return spreadSheet;
    }

    /**
     * @param spreadSheet
     * @param sheetName
     * @return Properties
     */
    @Override
    public Properties readProperties(SpreadsheetDocument spreadSheet,
	    String sheetName) {

	logger.debug("Reading properties. Sheet name (" + sheetName + ")");

	Properties props = null;
	try {
	    props = new PropertiesImpl(sheetName);
	    Table sheet = checkTableAvailability(sheetName);

	    int totalRows = sheet.getRowCount();

	    // The first row is left for headers. No need to read it.
	    for (int row = Constants.PROPERTY_START_ROW; row < totalRows; row++) {
		String attributeName = getCellValueAt(sheet,
			Constants.PROPERTY_NAME_COLUMN, row);
		boolean isAttributeCellNull = !Util.isNotEmpty(attributeName);
		if (isAttributeCellNull) {
		    break;
		} else {
		    String attributeValue = getCellValueAt(sheet,
			    Constants.PROPERTY_VALUE_COLUMN, row);
		    props.addAttribute(new Attribute(attributeName,
			    attributeValue));
		}
	    }
	} catch (NullPointerException e) {
	    logger.error(e.getMessage());
	}

	return props;
    }

    /**
     * @param CONFIG_FILE
     * @param sheetName
     * @return Properties
     */
    @Override
    public Properties readProperties(String file, String sheetName) {
	try {
	    spreadSheet = SpreadsheetDocument.loadDocument(file);
	    return readProperties(spreadSheet, sheetName);
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}
	return null;
    }

    /**
     * This methods reads the sheet in the provided SpreadSheet
     * 
     * @param spreadSheet
     * @param sheetName
     * @return SpreadSheet
     */
    @Override
    public SpreadSheet readSpreadSheet(SpreadsheetDocument spreadSheet,
	    String sheetName) {

	logger.debug("Reading test cases. Sheet name (" + sheetName + ")");

	SpreadSheet conf = null;
	try {
	    conf = new SpreadSheetImpl(sheetName);
	    final Table sheet = checkTableAvailability(sheetName);
	    final int totalRows = sheet.getRowCount();

	    Service service = null;
	    List<TestCase> testCases = null;
	    TestCase testCase = null;

	    // The first row is left for headers. No need to read it.
	    for (int row = Constants.CONFIGURATION_START_ROW; row < totalRows; row++) {

		String serviceName = getCellValueAt(sheet,
			Constants.CONFIGURATION_SERVICE_NAME_COLUMN, row);

		boolean isServiceCellNull = !Util.isNotEmpty(serviceName);

		if (!isServiceCellNull) {

		    String url = getCellValueAt(sheet,
			    Constants.CONFIGURATION_SERVICE_URL_COLUMN, row);
		    String requestType = getCellValueAt(sheet,
			    Constants.CONFIGURATION_REQUEST_TYPE_COLUMN, row);
		    String contentStructure = getCellValueAt(sheet,
			    Constants.CONFIGURATION_CONTENT_COLUMN, row);
		    String contentType = getCellValueAt(sheet,
			    Constants.CONFIGURATION_CONTENT_TYPE_COLUMN, row);
		    logger.debug("New service found. Service name ("
			    + serviceName + ") url(" + url + ") requestType("
			    + requestType + ") contentStructure("
			    + contentStructure + ") contentType(" + contentType
			    + ")");
		    service = new Service(serviceName, url, requestType,
			    contentStructure, contentType);
		    testCases = new ArrayList<TestCase>();
		}

		else {
		    // (0,1)=Login
		    // (1,2)=With valid credential
		    // (2,2)=Username
		    // (3,2)=user2

		    // (0,12)=TEST IT
		    // (1,13)=For authorized user with valid applicationID
		    // (2,13)=NAME13
		    // (3,13)=VALUE13

		    String testCaseName = getCellValueAt(sheet,
			    Constants.CONFIGURATION_TESTCASE_COLUMN, row);
		    String inputAttributeName = getCellValueAt(
			    sheet,
			    Constants.CONFIGURATION_INPUT_ATTRIBUTE_NAME_COLUMN,
			    row);
		    String inputAttributeValue = getCellValueAt(
			    sheet,
			    Constants.CONFIGURATION_INPUT_ATTRIBUTE_VALUE_COLUMN,
			    row);

		    String nextTestCaseName = "";
		    String nextServiceName = "";

		    // We should not read outside from the table
		    int nextRow = row + 1;
		    if (nextRow < totalRows) {
			nextServiceName = getCellValueAt(sheet,
				Constants.CONFIGURATION_SERVICE_NAME_COLUMN,
				nextRow);
			nextTestCaseName = getCellValueAt(sheet,
				Constants.CONFIGURATION_TESTCASE_COLUMN,
				nextRow);
		    }

		    boolean isTestCaseCellNull = !Util.isNotEmpty(testCaseName);
		    boolean isAttributeCellNull = !Util
			    .isNotEmpty(inputAttributeName);
		    boolean isNextServiceCellNull = !Util
			    .isNotEmpty(nextServiceName);
		    boolean isNextTestCaseCellNull = !Util
			    .isNotEmpty(nextTestCaseName);

		    if (isNextServiceCellNull && isNextTestCaseCellNull
			    && isTestCaseCellNull && isAttributeCellNull) {
			// end of table here
			testCases.add(testCase);
			service.addTestCases(testCases);
			conf.addService(service);
			break;
		    }

		    else if (!isTestCaseCellNull) {
			// new test case found here

			boolean authorizedUser = Util.isTrue(getCellValueAt(
				sheet, Constants.CONFIGURATION_AUTHORIZED_USER,
				row));

			int executionOrder = stringToInt(getCellValueAt(sheet,
				Constants.CONFIGURATION_EXECUTION_ORDER_COLUMN,
				row));

			boolean executable = Util
				.isTrue(getCellValueAt(
					sheet,
					Constants.CONFIGURATION_EXECUTE_IT_COLUMN,
					row));

			int expectedHttpResponseCode = stringToInt(getCellValueAt(
				sheet,
				Constants.CONFIGURATION_EXPECTED_HTTP_RESPONSE_CODE_COLUMN,
				row));
			String expectedAttributeName = getCellValueAt(
				sheet,
				Constants.CONFIGURATION_EXPECTED_ATTRIBUTE_NAME_COLUMN,
				row);
			String expectedAttributeValue = getCellValueAt(
				sheet,
				Constants.CONFIGURATION_EXPECTED_ATTRIBUTE_VALUE_COLUMN,
				row);

			String cacheAttributeName = getCellValueAt(sheet,
				Constants.CONFIGURATION_CACHE_ATTRIBUTE_NAME,
				row);

			String cacheAttributeCustomName = getCellValueAt(
				sheet,
				Constants.CONFIGURATION_CACHE_ATTRIBUTE_CUSTOM_NAME,
				row);

			Attribute inputAttribute = new Attribute(
				inputAttributeName, inputAttributeValue);
			Attribute expectedAttribute = new Attribute(
				expectedAttributeName, expectedAttributeValue);

			logger.debug("New Test Case found. Test Case name ("
				+ testCaseName + ") authorized user ("
				+ authorizedUser + ") execution Order ("
				+ executionOrder + ") executable(" + executable
				+ ") input Attribute" + inputAttribute
				+ " expected Attribute" + expectedAttribute
				+ " expected Http Response Code("
				+ expectedHttpResponseCode
				+ ") cache attribute name ("
				+ cacheAttributeName
				+ ") cache attribute custom name ("
				+ cacheAttributeCustomName + ")");

			testCase = new TestCase(testCaseName, authorizedUser,
				executionOrder, executable, inputAttribute,
				expectedAttribute, expectedHttpResponseCode,
				cacheAttributeName, cacheAttributeCustomName);
		    }

		    else if (!isAttributeCellNull) {
			// new attribute found. add it to test case here
			Attribute inputAttribute = new Attribute(
				inputAttributeName, inputAttributeValue);
			logger.debug("New Attribute found. Attribute "
				+ inputAttribute);
			testCase.addInputAttribute(inputAttribute);
		    }

		    else if (isAttributeCellNull && isNextServiceCellNull) {
			// test case ends. add it here
			testCases.add(testCase);
		    }

		    else if (isAttributeCellNull && !isNextServiceCellNull) {
			// service ends here. add all the test
			// cases to service
			testCases.add(testCase);
			service.addTestCases(testCases);
			conf.addService(service);
		    }
		}
	    }
	} catch (NullPointerException e) {
	    logger.error(e.getMessage());
	}

	return conf;
    }

    /**
     * This methods reads the sheet in the provided spreadSheet CONFIG_FILE
     * 
     * @param CONFIG_FILE
     * @param sheetName
     * @return SpreadSheet
     */
    @Override
    public SpreadSheet readSpreadSheet(String file, String sheetName) {
	try {
	    spreadSheet = SpreadsheetDocument.loadDocument(file);
	    return readSpreadSheet(spreadSheet, sheetName);
	} catch (Exception e) {
	    logger.error(e.getMessage());
	}
	return null;
    }

    private int stringToInt(String str) {
	try {
	    return Integer.parseInt(str);
	} catch (NumberFormatException e) {
	    logger.error("Input string (" + str + ") is not a valid integer. "
		    + e.getMessage());
	    return -1;
	}
    }
}
