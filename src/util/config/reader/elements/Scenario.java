package util.config.reader.elements;

import util.config.reader.elements.spreadSheet.TestCase;

public interface Scenario {

    String getContent();

    String getContentStructure();

    String getContentType();

    String getRequestType();

    String getServiceName();

    String getServiceUrl();

    TestCase getTestCase();

    String getUrl();

    void setContent(String content);

    void setUrl(String url);
}
