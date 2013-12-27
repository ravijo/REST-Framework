package services.rest.executor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wiztools.commons.Charsets;
import org.wiztools.restclient.ServiceLocator;
import org.wiztools.restclient.View;
import org.wiztools.restclient.bean.ContentType;
import org.wiztools.restclient.bean.ContentTypeBean;
import org.wiztools.restclient.bean.ReqEntityStringBean;
import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.RequestBean;
import org.wiztools.restclient.bean.RequestExecuter;
import org.wiztools.restclient.bean.Response;
import org.wiztools.restclient.bean.SSLHostnameVerifier;
import org.wiztools.restclient.bean.SSLReqBean;

import util.common.Constants;
import util.common.Util;
import util.config.reader.elements.Configuration;
import util.config.reader.elements.ConfigurationImpl;
import util.config.reader.elements.Scenario;
import util.config.reader.elements.spreadSheet.Attribute;
import util.json.parser.JSONParser;
import util.json.parser.JSONParserImpl;
import util.junit.runners.ParameterizedTestRunner;
import util.junit.runners.ParameterizedTestRunner.Parameters;

@RunWith(value = ParameterizedTestRunner.class)
public class TestCaseExecutor {

    /*
     * I have observed that @Parameter annotation is being called before
     * 
     * @BeforeClass. Below is the hack.
     */
    static {
	Util.initLogger();
    }

    @AfterClass
    public static void distroy() {
	// Shutdown the logger please
	Util.shutDownLogger();
    }

    @Parameters(name = "{0}")
    public static Collection<Scenario[]> getParameters() {

	final List<Scenario[]> scenarioList = new ArrayList<Scenario[]>();

	final Configuration config = new ConfigurationImpl();

	List<Scenario> scenarios = config.getScenarios();
	for (Scenario scenario : scenarios) {
	    scenarioList.add(new Scenario[] { scenario });
	}

	return scenarioList;
    }

    private static SSLReqBean getSSLReq() {
	final SSLReqBean sSLReq = new SSLReqBean();
	sSLReq.setTrustSelfSignedCert(true);
	sSLReq.setHostNameVerifier(SSLHostnameVerifier.ALLOW_ALL);
	return sSLReq;
    }

    private static SSLReqBean ssl = null;

    @BeforeClass
    public static void setup() {
	logger = Logger.getLogger(TestCaseExecutor.class);

	Util.configureRestClient();

	Util.initCookieManager();

	ssl = getSSLReq();
    }

    private final ResourceProvider resourceProvider = new ResourceProvider();

    private static Logger logger;

    public TestCaseExecutor(Scenario scenario) {
	this.resourceProvider.setScenario(scenario);
    }

    @Test
    public void execute() {

	try {
	    RequestBean request = resourceProvider.getRequestBean();

	    if (ConfigurationImpl.isSSLEnable()) {
		request.setSslReq(ssl);
	    }

	    if (resourceProvider.getScenario().getRequestType()
		    .equals(Constants.HTTP_POST)) {
		final ContentType contentType = new ContentTypeBean(
			resourceProvider.getContentType(), Charsets.UTF_8);
		final ReqEntityStringBean body = new ReqEntityStringBean(
			resourceProvider.getContent(), contentType);
		request.setBody(body);
	    }

	    if (resourceProvider.isAuthorizedUser()) {
		Set<HttpCookie> cookies = Util.getAllCookies();
		resourceProvider.addAllCookiesToReq(request, cookies);
	    }

	    final View view = new View() {

		/**
		 * This callback is called when the request is aborted during
		 * its progress by the call of RequestExecuter.abortExecution().
		 */
		@Override
		public void doCancelled() {
		    logger.info("TestCase (" + resourceProvider.getScenario()
			    + ") cancelled");
		}

		/**
		 * When the request has completed, this method is called.
		 */
		@Override
		public void doEnd() {
		    logger.info("TestCase (" + resourceProvider.getScenario()
			    + ") end");
		}

		/**
		 * Whenever an error is encountered, this is called. This
		 * usually is the error trace.
		 * 
		 * @param error
		 */
		@Override
		public void doError(String error) {
		    final String msg = "TestCase ("
			    + resourceProvider.getScenario() + ") Error ("
			    + error + ")";
		    logger.error(msg);
		    fail(msg);
		}

		/**
		 * When the request processing is completed, the response object
		 * is received by this method.
		 * 
		 * @param response
		 */
		@Override
		public void doResponse(Response response) {

		    List<Attribute> headerData = resourceProvider
			    .getHeaderDataFromResponse(response);

		    if (resourceProvider.isAuthorizedUser()) {
			resourceProvider.setHeaderDataToCookie(headerData);
		    }

		    final String statusLine = response.getStatusLine();
		    int httpResponeCode = response.getStatusCode();

		    String body = resourceProvider.getResponseBody(response);
		    body = body == null ? "" : body;

		    String responseProperties = headerData
			    + resourceProvider.getResponseProperties(
				    statusLine, httpResponeCode, body);

		    logger.info("TestCase (" + resourceProvider.getScenario()
			    + ") Response Info (" + responseProperties + ")");

		    /*
		     * If the response body is empty
		     */
		    if (!Util.isNotEmpty(body)) {

			boolean condition = resourceProvider
				.getExpectedHTTPResponseCode() == httpResponeCode;
			String msg = "Condition ("
				+ resourceProvider
					.getExpectedHTTPResponseCode()
				+ " EQUAL TO " + httpResponeCode + ") is ("
				+ condition + ")";

			logger.info("TestCase ("
				+ resourceProvider.getScenario() + ") " + msg);
			assertTrue(msg, condition);
		    }

		    else {

			ContentType contentType = response.getContentType();
			String responseContentType = null;

			if (Util.isNotEmpty(contentType)) {
			    responseContentType = contentType.getContentType();
			}

			/*
			 * If the response is JSON
			 */
			if (Util.isNotEmpty(resourceProvider
				.getExpectedAttributeName())
				&& resourceProvider
					.isResponseJSON(responseContentType)) {
			    final JSONParser jsonParser = new JSONParserImpl(
				    body);
			    String valueFromRespone = jsonParser
				    .parse(resourceProvider
					    .getExpectedAttributeName());

			    if (Util.isNotEmpty(resourceProvider
				    .getCacheParamName())) {
				String valueToKeepInCache = jsonParser
					.parse(resourceProvider
						.getCacheParamName());
				Util.addToCache(resourceProvider
					.getCacheParamCustomName(),
					valueToKeepInCache);
			    }

			    boolean condition = resourceProvider
				    .getExpectedHTTPResponseCode() == httpResponeCode
				    && resourceProvider
					    .getExpectedAttributeValue()
					    .equals(valueFromRespone);
			    String msg = "Condition ("
				    + resourceProvider
					    .getExpectedHTTPResponseCode()
				    + " EQUAL TO "
				    + httpResponeCode
				    + " AND "
				    + resourceProvider
					    .getExpectedAttributeValue()
				    + " EQUAL TO " + valueFromRespone
				    + ") is (" + condition + ")";

			    logger.info("TestCase ("
				    + resourceProvider.getScenario() + ") "
				    + msg);
			    assertTrue(msg, condition);
			}

			/*
			 * If the response is not JSON, we are ONLY checking
			 * http response code
			 */
			else {

			    boolean condition = resourceProvider
				    .getExpectedHTTPResponseCode() == httpResponeCode;
			    String msg = "Condition ("
				    + resourceProvider
					    .getExpectedHTTPResponseCode()
				    + " EQUAL TO " + httpResponeCode + ") is ("
				    + condition + ")";

			    logger.info("TestCase ("
				    + resourceProvider.getScenario() + ") "
				    + msg);
			    assertTrue(msg, condition);
			}
		    }
		}

		/**
		 * This is called just before starting the request processing.
		 * 
		 * @param request
		 *            The same request object passed to RequestExecuter.
		 */
		@Override
		public void doStart(Request request) {

		    String cookiesInfo = null;
		    if (resourceProvider.isAuthorizedUser()) {
			List<HttpCookie> cookies = request.getCookies();
			Util.addAllCookies(cookies);
			cookiesInfo = Arrays.toString(cookies.toArray());
		    }

		    logger.info("TestCase (" + resourceProvider.getScenario()
			    + ") start. Request Info (Cookies (" + cookiesInfo
			    + ")"
			    + resourceProvider.getRequestProperties(request)
			    + ")");
		}

	    };

	    // Execute the request
	    RequestExecuter executer = ServiceLocator
		    .getInstance(RequestExecuter.class);
	    executer.execute(request, view);

	} catch (Exception e) {
	    final String msg = "TestCase (" + resourceProvider.getScenario()
		    + ") " + e.getMessage();
	    logger.error(msg);
	    fail(msg);
	}
    }
}
