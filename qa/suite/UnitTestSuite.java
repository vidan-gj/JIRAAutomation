package suite;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import unit.AdminTaskTest;
import unit.CloneDashboardTest;
import unit.CloneTicketTest;
import unit.CloseTicketErrorMsgTest;
import unit.CreateAgileSprintTest;
import unit.CreateFixVersionTest;
import unit.DefectWorkflowTest;
import unit.EscalatePSPTicketTest;
import unit.ExportIssuesTest;
import unit.JiraReIndexingTest;
import unit.LastCommentDSRTest;
import unit.PSPArgusWatcherTest;
import unit.ReleaseFieldValidationTest;
import unit.ResolveINFTicketTest;
import unit.ResolvedTicketBackToProgressTest;
import unit.ResolvedTicketDeployIssueTest;
import unit.ScheduleHealthQueryTest;
import unit.StructureResyncTest;
import unit.TicketChangeTypeTest;
import unit.TicketMissingIDTest;
import unit.TicketMoveToProjectTest;
import unit.TicketUpdateTest;
import unit.UserAccessReportTest;
import unit.UserAccessReportTest_MultipleUsers;
import unit.UserLoginTest;
import unit.ValidateTicketSourceTest;
import data.ExcelUtility;
import data.ReadPropertiesFile;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	UserLoginTest.class,
	TicketUpdateTest.class,
	//AdminTaskTest.class,  --> Jira BUG: current Jira version has Ajax bug (when switching between tabs)
	ExportIssuesTest.class,
	TicketMoveToProjectTest.class,
	DefectWorkflowTest.class,
	ResolvedTicketDeployIssueTest.class,
	PSPArgusWatcherTest.class,
	CloseTicketErrorMsgTest.class,
	TicketMissingIDTest.class,
	ResolvedTicketBackToProgressTest.class,
	ValidateTicketSourceTest.class,
	CloneTicketTest.class,
	TicketChangeTypeTest.class,
	//CreateFixVersionTest.class,  --> deletion of fix version is not automated
	CloneDashboardTest.class,
	StructureResyncTest.class,
	ReleaseFieldValidationTest.class,
	EscalatePSPTicketTest.class,
	//UserAccessReportTest.class,  ---> Jira BUG: username is not entered properly into the textbox
	//UserAccessReportTest_MultipleUsers.class, 	---> Jira BUG: username is not entered properly into the textbox
	LastCommentDSRTest.class,
	//CreateAgileSprintTest.class,   --> Cannot create new Agile Board - Need Browse Permission
	ScheduleHealthQueryTest.class,
	ResolveINFTicketTest.class
})
public class UnitTestSuite {

}
