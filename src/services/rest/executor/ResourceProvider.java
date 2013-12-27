package services.rest.executor;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.MultiValueMap;
import org.wiztools.restclient.bean.ContentType;
import org.wiztools.restclient.bean.HTTPMethod;
import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.RequestBean;
import org.wiztools.restclient.bean.Response;

import util.common.Constants;
import util.common.Util;
import util.config.reader.elements.Scenario;
import util.config.reader.elements.spreadSheet.Attribute;

public class ResourceProvider {

    private static final Logger logger = Logger
	    .getLogger(ResourceProvider.class);

    private Scenario scenario;

    protected ResourceProvider() {
    }

    protected void addAllCookiesToReq(RequestBean request,
	    Set<HttpCookie> cookies) {
	for (HttpCookie cookie : cookies) {
	    request.addCookie(cookie);
	}
    }

    protected String getCacheParamCustomName() {
	String cacheAttributeName = getCacheParamName();
	String cacheAttributeCustomName = getScenario().getTestCase()
		.getCacheAttributeCustomName();

	return Util.isNotEmpty(cacheAttributeName)
		&& Util.isNotEmpty(cacheAttributeCustomName) ? cacheAttributeCustomName
		: cacheAttributeName;
    }

    protected String getCacheParamName() {
	return scenario.getTestCase().getCacheAttributeName();
    }

    protected String getContent() {
	String content = scenario.getContent();
	return Util.fillFromCache(content);
    }

    protected String getContentType() {
	String contentType = scenario.getContentType();

	if (!Util.isNotEmpty(contentType)) {
	    contentType = Constants.DEFAULT_CONTENT_TYPE;
	}

	return contentType;
    }

    protected Attribute getExpectedAttribute() {
	return scenario.getTestCase().getExpectedAttribute();
    }

    protected String getExpectedAttributeName() {
	return getExpectedAttribute().getName();
    }

    protected String getExpectedAttributeValue() {
	String val = getExpectedAttribute().getValue();
	return Util.fillFromCache(val);
    }

    protected int getExpectedHTTPResponseCode() {
	return scenario.getTestCase().getExpectedHttpResponseCode();
    }

    protected String getHeaderDataFromRequest(Request request) {
	final StringBuilder sb = new StringBuilder();

	MultiValueMap<String, String> headerData = request.getHeaders();

	for (String headerName : headerData.keySet()) {
	    for (String headerValue : headerData.get(headerName)) {
		sb.append("(");
		sb.append(headerName);
		sb.append("=");
		sb.append(headerValue);
		sb.append(") ");
	    }
	}

	return sb.toString();

    }

    protected List<Attribute> getHeaderDataFromResponse(Response response) {

	final List<Attribute> data = new ArrayList<Attribute>();

	MultiValueMap<String, String> headerData = response.getHeaders();

	for (String headerName : headerData.keySet()) {
	    for (String headerValue : headerData.get(headerName)) {
		data.add(new Attribute(headerName, headerValue));
	    }
	}
	return data;
    }

    protected HTTPMethod getHTTPMethodType() {
	final String method = scenario.getRequestType();
	if (Constants.HTTP_GET.equals(method)) {
	    return HTTPMethod.GET;
	} else if (Constants.HTTP_POST.equals(method)) {
	    return HTTPMethod.POST;
	} else if (Constants.HTTP_PUT.equals(method)) {
	    return HTTPMethod.PUT;
	} else if (Constants.HTTP_PATCH.equals(method)) {
	    return HTTPMethod.PATCH;
	} else if (Constants.HTTP_DELETE.equals(method)) {
	    return HTTPMethod.DELETE;
	} else if (Constants.HTTP_HEAD.equals(method)) {
	    return HTTPMethod.HEAD;
	} else if (Constants.HTTP_OPTIONS.equals(method)) {
	    return HTTPMethod.OPTIONS;
	} else if (Constants.HTTP_TRACE.equals(method)) {
	    return HTTPMethod.TRACE;
	} else {
	    logger.warn("Unknown HTTP method (" + method
		    + ") encountered. Setting default HTTP method (GET)");
	    return HTTPMethod.GET;
	}
    }

    protected RequestBean getRequestBean() throws MalformedURLException {
	final RequestBean request = new RequestBean();
	request.setUrl(new URL(getURL()));
	request.setMethod(getHTTPMethodType());
	return request;
    }

    protected String getRequestProperties(Request request) {
	final StringBuilder sb = new StringBuilder();

	sb.append("(URL=");
	sb.append(request.getUrl());
	sb.append(") (Method=");
	sb.append(request.getMethod());
	sb.append(") (HTTP Version=");
	sb.append(request.getHttpVersion());
	sb.append(") (Auth=");
	sb.append(request.getAuth());
	sb.append(") (Body=");
	sb.append(request.getBody());
	sb.append(") (SSL Request=");
	sb.append(request.getSslReq());
	sb.append(") (Headers=");
	sb.append(getHeaderDataFromRequest(request));
	sb.append(")");

	return sb.toString();
    }

    protected String getResponseBody(Response response) {

	byte[] body = response.getResponseBody();

	if (body.length > 0) {
	    ContentType contentType = response.getContentType();
	    Charset charset = null;

	    if (Util.isNotEmpty(contentType)) {
		charset = contentType.getCharset();
	    } else {
		logger.debug("TestCase (" + getScenario()
			+ ") No Content Type found");
	    }

	    if (!Util.isNotEmpty(charset)) {
		logger.debug("TestCase ("
			+ getScenario()
			+ ") Charset is not available in Response Content-Type header. Hence using UTF8 as charset");
		charset = Charsets.UTF_8;
	    }

	    return new String(body, charset);

	} else {
	    logger.debug("TestCase (" + getScenario()
		    + ") Empty response found.");
	}
	return null;
    }

    protected String getResponseProperties(String statusLine, int statusCode,
	    String body) {
	StringBuilder sb = new StringBuilder();
	sb.append("(Status Line=");
	sb.append(statusLine);
	sb.append(") (Status Code=");
	sb.append(statusCode);
	sb.append(") (Response Body=");
	sb.append(body);
	sb.append(")");

	return sb.toString();
    }

    /**
     * @return the scenario
     */
    protected Scenario getScenario() {
	return scenario;
    }

    protected String getURL() {
	String url = scenario.getUrl();

	return Util.fillFromCache(url);
    }

    protected boolean isAuthorizedUser() {
	return scenario.getTestCase().isAuthorizedUser();
    }

    protected boolean isResponseJSON(final String responseContentType) {
	return Util.isNotEmpty(responseContentType)
		&& responseContentType.equals("application/json");
    }

    protected void setHeaderDataToCookie(List<Attribute> data) {

	for (Attribute header : data) {
	    Util.getCookieManager().addCookieFromHeader(header.getName(),
		    header.getValue());
	}
    }

    /**
     * @param scenario
     *            the scenario to set
     */
    protected void setScenario(Scenario scenario) {
	this.scenario = scenario;
    }

}