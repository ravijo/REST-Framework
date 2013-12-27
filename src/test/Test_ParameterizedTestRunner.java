package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import util.common.Util;
import util.junit.runners.ParameterizedTestRunner;
import util.junit.runners.ParameterizedTestRunner.Parameters;

/**
 * JUnit Parameterized Test
 * 
 * @author ravijo
 * 
 */
@RunWith(value = ParameterizedTestRunner.class)
public class Test_ParameterizedTestRunner {
    private static Logger logger;

    @Parameters(name = "{0}")
    public static Collection<Person[]> getData() {
	Util.initLogger();

	System.out.println("hahahahahahhhahahahahaha");

	final List<Person[]> persons = new ArrayList<Person[]>();

	for (int i = 0; i <= 10; i++) {
	    Person person = new Person("P" + (i + 1), i + 10);
	    persons.add(new Person[] { person });
	}
	return persons;
    }

    @BeforeClass
    public static void oneTimeSetUp() {
	logger = Logger.getLogger(Test_ParameterizedTestRunner.class);
	logger.info("one time setup is done");
    }

    private Person person;

    public Test_ParameterizedTestRunner(Person person) {
	this.person = person;
    }

    @Test
    public void justDoIt() {

	String ans = sayHello(person);
	String name = person.getName();
	String testName = "Person Test " + name;
	int age = person.getAge();
	logger.info("ans=" + ans + ", Test Name=" + testName
		+ ", Person: name=" + name + ", age=" + age);

	assertTrue("Checking " + name, "P3".equals(name));
    }

    private String sayHello(Person person) {
	// TODO Auto-generated method stub
	return "hello " + person.getName();

    }
}
