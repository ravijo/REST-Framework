package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.wiztools.commons.Charsets;
import org.wiztools.restclient.ServiceLocator;
import org.wiztools.restclient.View;
import org.wiztools.restclient.bean.ContentType;
import org.wiztools.restclient.bean.ContentTypeBean;
import org.wiztools.restclient.bean.HTTPMethod;
import org.wiztools.restclient.bean.ReqEntityStringBean;
import org.wiztools.restclient.bean.Request;
import org.wiztools.restclient.bean.RequestBean;
import org.wiztools.restclient.bean.RequestExecuter;
import org.wiztools.restclient.bean.Response;
import org.wiztools.restclient.bean.SSLHostnameVerifier;
import org.wiztools.restclient.bean.SSLReqBean;

public class Test_HTTPClientRequest {

    // @BeforeClass
    // public static void setUpClass() throws Exception {
    // }
    //
    // @AfterClass
    // public static void tearDownClass() throws Exception {
    // }
    //
    // @Before
    // public void setUp() throws Exception {
    // }
    //
    // @After
    // public void tearDown() throws Exception {
    // }
    private SSLReqBean getSSLReq() {
	SSLReqBean sSLReq = new SSLReqBean();
	sSLReq.setTrustSelfSignedCert(true);
	sSLReq.setHostNameVerifier(SSLHostnameVerifier.ALLOW_ALL);
	return sSLReq;
    }

    /**
     * Test of run method, of class HTTPRequestThread.
     */
    @Test
    public void run() {
	try {
	    String ip = "127.0.0.1";
	    String port = "8443";
	    String username = "user2";
	    String passwd = username;
	    String loginURL = "https://" + ip + ":" + port
		    + "/hwe/services/awpf/platform/authentication/login";
	    String loginContent = "{\"j_username\":\"" + username
		    + "\", \"j_password\":\"" + passwd
		    + "\", \"appId\":\"testapplication\"}";

	    final ContentType contentType = new ContentTypeBean(
		    "application/json", Charsets.UTF_8);
	    RequestBean request = new RequestBean();
	    request.setUrl(new URL(loginURL));
	    request.setMethod(HTTPMethod.POST);
	    final ReqEntityStringBean rBean = new ReqEntityStringBean(
		    loginContent, contentType);
	    request.setBody(rBean);

	     SSLReqBean ssl = getSSLReq();
	     request.setSslReq(ssl);

	    View view = new View() {

		/**
		 * This callback is called when the request is aborted during
		 * its progress by the call of RequestExecuter.abortExecution().
		 */
		@Override
		public void doCancelled() {

		}

		/**
		 * When the request has completed, this method is called.
		 */
		@Override
		public void doEnd() {
		}

		/**
		 * Whenever an error is encountered, this is called. This
		 * usually is the error trace.
		 * 
		 * @param error
		 */
		@Override
		public void doError(String error) {
		    fail(error);
		}

		/**
		 * When the request processing is completed, the response object
		 * is received by this method.
		 * 
		 * @param response
		 */
		@Override
		public void doResponse(Response response) {
		    // MultiValueMap<String, String> header_data = response
		    // .getHeaders();
		    //
		    // System.out.println("\nResponse Header starts here");
		    // for (String key : header_data.keySet()) {
		    // for (String value : header_data.get(key)) {
		    // System.out.println(key + "]=[" + value);
		    // }
		    // }
		    //
		    // // response.
		    //
		    // System.out.println("Response Header ends here. ["
		    // + response.getContentType().getContentType() + "]");

		    byte[] bodyByte = response.getResponseBody();
		    String responseFromServer = new String(bodyByte,
			    Charsets.UTF_8);

		    System.out.println("execution time="
			    + response.getExecutionTime() + "\nstatus code="
			    + response.getStatusCode() + "\nstatus line="
			    + response.getStatusLine() + "\ncontent type="
			    + response.getContentType()
			    + "\nresponse from server=" + responseFromServer);

		    String expectedResponse = "{\"login\":\"user2\"}";

		    assertEquals(expectedResponse, responseFromServer);
		}

		/**
		 * This is called just before starting the request processing.
		 * 
		 * @param request
		 *            The same request object passed to RequestExecuter.
		 */
		@Override
		public void doStart(Request request) {
		    // MultiValueMap<String, String> allHeaders =
		    // request.getHeaders();
		    // Collection<String> allValues = allHeaders.values();
		    //
		    System.out.println("Req Header starts here");
		    // Iterator<String> it = allValues.iterator();
		    // while (it.hasNext()) {
		    // String value = it.next();
		    // System.out.print(value + ", ");
		    // }

		    List<HttpCookie> cookies = request.getCookies();
		    for (HttpCookie cookie : cookies) {
			System.out.println("cookie=" + cookie + " ,name="
				+ cookie.getName() + ", value="
				+ cookie.getValue());
		    }

		    System.out.println("Req Header ends here" + request);
		    //
		    // System.out.println(request.getTestScript() + ", "
		    // + request.getBody() + ", " + request.getMethod() + ", "
		    // + request.getUrl());
		}
	    };

	    // Execute:
	    RequestExecuter executer = ServiceLocator
		    .getInstance(RequestExecuter.class);
	    executer.execute(request, view);

	} catch (MalformedURLException e) {
	    fail(e.getMessage());
	}
    }
}
