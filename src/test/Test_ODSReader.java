package test;

//package test.util.config.reader;
//
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.odftoolkit.simple.SpreadsheetDocument;
//
//import util.config.reader.Attribute;
//import util.config.reader.SpreadSheet;
//import util.config.reader.ODSReader;
//import util.config.reader.Properties;
//import util.config.reader.Service;
//import util.config.reader.TestCase;
//
///**
// * <p>
// * </p>
// * <b>Created on</b>: Aug 20, 2013 | 11:16:52 AM
// * 
// * @author ravij
// * @since
// */
//public class TestODSReader {
//	private static final Logger logger = Logger.getLogger(TestODSReader.class);
//
//	private final String propertySheetName = "AWPF";
//	private final String configurationSheetName = "Detailed";
//	// private final String CONFIG_FILE =
//	// "D:/Documents and Settings/ravij/My Documents/My-Backup/Drive-C/eclipse_workspace/CommonUtil/config/AWPF_Services_Automation/AWPF_Services_Automation.ods";
//	private final String file = "D:/Documents and Settings/ravij/My Documents/My-Backup/Drive-C/eclipse_workspace/CommonUtil/config/AWPF_Services_Automation.ods";
//
//	private final ODSReader ods = new ODSReader();
//
//	private SpreadSheet readAllConfigurations() {
//		logger.info("readAllConfigurations");
//
//		logger.info("Reading " + configurationSheetName + " sheet");
//
//		SpreadSheet conf = ods.readConfigurations(file,
//				configurationSheetName);
//		System.out.print("Sheet name=" + conf.getSheetName() + "\n");
//
//		List<Service> services = conf.getServices();
//		Iterator<Service> it1 = services.iterator();
//		while (it1.hasNext()) {
//			Service service = it1.next();
//			System.out.print(service.getName() + "\n");
//			List<TestCase> testCases = service.getTestCases();
//			Iterator<TestCase> it2 = testCases.iterator();
//			while (it2.hasNext()) {
//				TestCase testCase = it2.next();
//				System.out.print("\t" + testCase.getName() + "\n");
//				List<Attribute> attributes = testCase.getAttributes();
//				Iterator<Attribute> it3 = attributes.iterator();
//				while (it3.hasNext()) {
//					Attribute attribute = it3.next();
//					System.out.print("\t\t" + attribute.getName() + "="
//							+ attribute.getValue() + "\n");
//				}
//			}
//		}
//		return conf;
//	}
//
//	private void readServiceByNameFromConfigurations() {
//		logger.info("readServiceByNameFromConfigurations");
//
//		SpreadSheet conf = readAllConfigurations();
//		Service login = conf.getServiceByName("Login");
//		TestCase tesCase = login.getTestCaseByName("With valid credential");
//		Attribute username = tesCase.getAttributeByName("Username");
//		logger.info(username.getName() + "=" + username.getValue());
//	}
//
//	private void readAttributeByNameFromProperties() {
//		logger.info("readAttributeByNameFromProperties");
//
//		Properties props = readAllProperties();
//		Attribute port = props.getAttributeByName("Server Port");
//		logger.info(port.getName() + "=" + port.getValue());
//	}
//
//	private Properties readAllProperties() {
//		logger.info("readAllProperties");
//
//		logger.info("Reading " + propertySheetName + " sheet");
//
//		SpreadsheetDocument spreadSheet = ods.getLastSpreadSheet();
//		Properties props = ods.readProperties(spreadSheet, propertySheetName);
//
//		List<Attribute> attributes = props.getAttributes();
//		Iterator<Attribute> it4 = attributes.iterator();
//
//		while (it4.hasNext()) {
//			Attribute attribute = it4.next();
//			System.out
//					.println(attribute.getName() + "=" + attribute.getValue());
//		}
//		return props;
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		TestODSReader testodsreader = new TestODSReader();
//		testodsreader.readAllConfigurations();
//		testodsreader.readServiceByNameFromConfigurations();
//		testodsreader.readAllProperties();
//		testodsreader.readAttributeByNameFromProperties();
//	}
// }
