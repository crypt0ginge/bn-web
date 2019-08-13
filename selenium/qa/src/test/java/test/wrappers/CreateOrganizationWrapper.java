package test.wrappers;

import org.openqa.selenium.WebDriver;

import pages.admin.events.AdminEventsPage;
import pages.admin.organizations.AdminOrganizationsPage;
import pages.admin.organizations.CreateOrganizationPage;
import pages.components.AdminSideBar;
import pages.components.Header;

public class CreateOrganizationWrapper {

	public boolean createOrganization(WebDriver driver, String name, String phone, String tz, String location) {
		AdminEventsPage eventPage = new AdminEventsPage(driver);
		boolean retVal = eventPage.isAtPage();
		Header header = new Header(driver);
		header.clickOnBoxOfficeLink();
		header.clickOnToStudioLink();
		AdminSideBar sideBar = new AdminSideBar(driver);
		AdminOrganizationsPage organizationPage = sideBar.clickOnOrganizations();
		CreateOrganizationPage createOrganization = organizationPage.clickOnCreateOrganizationButton();
		createOrganization.fillFormAndConfirm(name, phone, tz, location);
		retVal = retVal && createOrganization.checkPopupMessage();
		return retVal;
	}

}