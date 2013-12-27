package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import util.json.parser.JSONParser;
import util.json.parser.JSONParserImpl;

import com.eclipsesource.json.ParseException;

public class Test_JSONParser {

    private static void checkWithCorrectJson() throws ParseException,
	    FileNotFoundException, IOException {

	// System.out.println("Started checking 1.txt.");
	//
	// JSONParser jp1 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/1.txt")));
	// System.out.println(jp1.parse("foo"));
	// System.out.println("Finished checking 1.txt.");
	//
	// System.out.println("Started checking 2.txt.");
	// JSONParser jp2 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/2.txt")));
	// System.out.println(jp2.parse("widget.text.onMouseUp"));
	// System.out.println("Finished checking 2.txt.");

	System.out.println("Started checking 3.txt.");
	JSONParser jp3 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/3.txt")));
	System.out.println(jp3
		.parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.para"));
	//
	// String aa = (String) jp3
	// .parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso");

	String aa = (String) jp3.parse("glossary.GlossDiv.title");
	System.out.println(aa);
	System.out
		.println(jp3
			.parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso[1]"));
	System.out.println("Finished checking 3.txt.");

	// System.out.println("Started checking 4.txt.");
	// JSONParser jp4 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/4.txt")));
	// System.out.println(jp4.parse("settings"));
	// System.out.println(jp4.parse("settings[0].displayName"));
	// System.out.println(jp4.parse("settings[0].value.$"));
	// System.out.println("Finished checking 4.txt.");
	//
	// System.out.println("Started checking 5.txt.");
	// JSONParser jp5 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/5.txt")));
	// System.out.println(jp5.parse("foo[1]"));
	// System.out.println(jp5.parse("mission"));
	// System.out.println("Finished checking 5.txt.");
	//
	// System.out.println("Started checking 6.txt.");
	// JSONParser jp6 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/6.txt")));
	// System.out.println(jp6.parse("settings[0].value.usertype"));
	// System.out.println(jp6.parse("settings[0].value.usertype[1]"));
	// System.out.println("Finished checking 6.txt.");
	//
	// System.out.println("Started checking 7.txt.");
	// JSONParser jp7 = new JSONParserImpl(new FileReader(new File(
	// "D:/ravi_backup/Project/JSON Parser/json/7.txt")));
	// System.out.println(jp7.parse("settings[0].value.usertype[1]"));
	// System.out.println(jp7.parse("settings[0].value.usertype[0].name"));
	// System.out.println("Finished checking 7.txt.");
    }

    private static void checkWithIncorrectJson() throws ParseException,
	    FileNotFoundException, IOException {

	System.out.println("Started checking 1.txt.");
	JSONParser jp1 = null;
	// try {
	jp1 = new JSONParserImpl(
		readFile("D:/ravi_backup/Project/JSON Parser/json/Copy of 1.txt"));
	// } catch (Exception e) {
	// System.out.println("Error while parsing json. " +
	// getStatckTrace(e));
	// System.out.println("Error while geting foo. " + e.getMessage());
	// }
	// try {
	// if (jp1 == null) {
	// System.out.println("jp1 is null");
	// } else {
	// System.out.println("jp1 is not null");
	// }
	System.out.println(jp1.parse("fooo"));
	// } catch (Exception e) {
	// System.out.println("Error while geting foo. " +
	// getStatckTrace(e));
	// System.out.println("Error while geting foo. " + e.getMessage());
	// }
	System.out.println("Finished checking 1.txt.");

	System.out.println("Started checking 2.txt.");
	JSONParser jp2 = null;
	// try {
	jp2 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 2.txt")));
	// } catch (Exception e) {
	// System.out.println("Error while parsing json. " + e);
	// }
	// try {
	System.out.println(jp2.parse("widget.text.onMouseUp"));
	// } catch (Exception e) {
	// System.out.println("Error while geting foo. " + e);
	// }
	System.out.println("Finished checking 2.txt.");

	System.out.println("Started checking 3.txt.");
	JSONParser jp3 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 3.txt")));
	System.out.println(jp3
		.parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.para"));
	System.out
		.println(jp3
			.parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso"));
	System.out
		.println(jp3
			.parse("glossary.GlossDiv.GlossList.GlossEntry.GlossDef.GlossSeeAlso[1]"));
	System.out.println("Finished checking 3.txt.");

	System.out.println("Started checking 4.txt.");
	JSONParser jp4 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 4.txt")));
	System.out.println(jp4.parse("settings"));
	System.out.println(jp4.parse("settings[0].displayName"));
	System.out.println(jp4.parse("settings[0].value.$"));
	System.out.println("Finished checking 4.txt.");

	System.out.println("Started checking 5.txt.");
	JSONParser jp5 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 5.txt")));
	System.out.println(jp5.parse("foo[1]"));
	System.out.println(jp5.parse("mission"));
	System.out.println("Finished checking 5.txt.");

	System.out.println("Started checking 6.txt.");
	JSONParser jp6 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 6.txt")));
	System.out.println(jp6.parse("settings[0].value.usertype"));
	System.out.println(jp6.parse("settings[0].value.usertype[1]"));
	System.out.println("Finished checking 6.txt.");

	System.out.println("Started checking 7.txt.");
	JSONParser jp7 = new JSONParserImpl(new FileReader(new File(
		"D:/ravi_backup/Project/JSON Parser/json/Copy of 7.txt")));
	System.out.println(jp7.parse("settings[0].value.usertype[1]"));
	System.out.println(jp7.parse("settings[0].value.usertype[0].name"));
	System.out.println("Finished checking 7.txt.");

    }

    public static String getStatckTrace(Throwable throwable) {
	if (throwable == null) {
	    return null;
	}

	Writer writer = new StringWriter();
	PrintWriter printWriter = new PrintWriter(writer);
	throwable.printStackTrace(printWriter);
	return writer.toString();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
	checkWithCorrectJson();
	checkWithIncorrectJson();
    }

    private static String readFile(String filename) {
	String content = null;
	File file = new File(filename);
	try {
	    FileReader reader = new FileReader(file);
	    char[] chars = new char[(int) file.length()];
	    reader.read(chars);
	    content = new String(chars);
	    reader.close();
	} catch (IOException e) {
	    System.out.println("Error while reading CONFIG_FILE. "
		    + e.getMessage());
	}
	return content;
    }

}
