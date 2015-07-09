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
	JiraReIndexingTest.class
})
public class JiraReIndexTest {

}
