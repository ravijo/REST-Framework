package util.common;

import java.io.File;

public final class Constants {

    public final static String SLASH = File.separator;

    public final static String LOG4J_CONFIG_FILE = "config" + SLASH
	    + "log4j.properties";
    public static final String CONFIG_FILE = "config" + SLASH + "config.ods";
    public static final String PROPERTIES_SHEET_NAME = "Properties";

    public static final String PROPERTY_PROJECT_NAME = "project name";
    public static final String PROPERTY_SHEET_NAME = "sheet name";
    public static final String PROPERTY_CONTEXT_PATH = "context path";
    public static final String PROPERTY_SERVER_IP_HOST_NAME = "server ip/host name";
    public static final String PROPERTY_SERVER_PORT = "server port";
    public static final String PROPERTY_FOLLOW_EXECUTION_ORDER = "follow \"execution order\"";
    public static final String PROPERTY_FOLLOW_EXECUTE_IT = "follow \"execute it\"";
    public static final String PROPERTY_SECURE_BROWSING_ENABLED = "secure browsing enabled";
    public final static String PROPERTY_CLIENT_REQ_TIMEOUT = "http request timeout";
    public static final int PROPERTY_START_ROW = 1;
    public static final int PROPERTY_NAME_COLUMN = 0;
    public static final int PROPERTY_VALUE_COLUMN = 1;

    public static final int CONFIGURATION_START_ROW = 1;
    public static final int CONFIGURATION_SERVICE_NAME_COLUMN = 0;
    public static final int CONFIGURATION_SERVICE_URL_COLUMN = 1;
    public static final int CONFIGURATION_REQUEST_TYPE_COLUMN = 2;
    public static final int CONFIGURATION_CONTENT_COLUMN = 3;
    public static final int CONFIGURATION_CONTENT_TYPE_COLUMN = 4;
    public static final int CONFIGURATION_TESTCASE_COLUMN = 5;
    public static final int CONFIGURATION_EXECUTION_ORDER_COLUMN = 6;
    public static final int CONFIGURATION_EXECUTE_IT_COLUMN = 7;
    public static final int CONFIGURATION_INPUT_ATTRIBUTE_NAME_COLUMN = 8;
    public static final int CONFIGURATION_INPUT_ATTRIBUTE_VALUE_COLUMN = 9;
    public static final int CONFIGURATION_EXPECTED_ATTRIBUTE_NAME_COLUMN = 10;
    public static final int CONFIGURATION_EXPECTED_ATTRIBUTE_VALUE_COLUMN = 11;
    public static final int CONFIGURATION_EXPECTED_HTTP_RESPONSE_CODE_COLUMN = 12;
    public static final int CONFIGURATION_AUTHORIZED_USER = 13;
    public static final int CONFIGURATION_CACHE_ATTRIBUTE_NAME = 14;
    public static final int CONFIGURATION_CACHE_ATTRIBUTE_CUSTOM_NAME = 15;

    public final static String HTTP_GET = "GET";
    public final static String HTTP_POST = "POST";
    public final static String HTTP_PUT = "PUT";
    public final static String HTTP_PATCH = "PATCH";
    public final static String HTTP_DELETE = "DELETE";
    public final static String HTTP_HEAD = "HEAD";
    public final static String HTTP_OPTIONS = "OPTIONS";
    public final static String HTTP_TRACE = "TRACE";
    public final static String DEFAULT_CONTENT_TYPE = "text/plain";

    /*
     * Don't remove forward slash before dollar sign since dollar without
     * forward slash is treated as a regular expression character
     */
    // public final static String VARIABLE_PREFIX = "\\$";
    public final static String VARIABLE_PREFIX = "@";

    /*
     * Don't remove forward slash before dot sign since dot without forward
     * slash is treated as a regular expression character
     */
    public final static String SPLIT_BY_DOT = "\\.";
    public final static String ARRAY_SPLITER_REGEX = "(.*)\\[(\\d+)\\]";

    public final static String VARIABLE_FINDER_REGEX = ".*" + VARIABLE_PREFIX
	    + "(\\w+).*";

    /*
     * Rest client uses this file to save configurations
     */
    public final static String REST_CLIENT_PROPERTIES = System
	    .getProperty("user.home")
	    + SLASH
	    + ".rest-client"
	    + SLASH
	    + "rest-client.properties";

    public final static String REST_CLIENT_REQ_TIMEOUT = "request-timeout-in-millis";
}
