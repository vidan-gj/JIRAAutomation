package suite;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*	----------	TEST SUITES AVAILABLE:   ---------- *
 * 		- UnitTestSuite								*
 * 		- DemoTestSuite								*
 *  ----------   ----------   ----------  ----------*  
 */
public class TestRunner {
	public static void main(String[] args) throws Exception {
		
		Result result = JUnitCore.runClasses(UnitTestSuite.class);
		
		for(Failure failure : result.getFailures()) {
			System.out.println("Failure Reason: " + failure.toString());
			System.out.println("|---------------------------------------------------------------------------|");
		}
		System.out.println("Test Suite Execution Result: " + result.wasSuccessful());
	}

}
