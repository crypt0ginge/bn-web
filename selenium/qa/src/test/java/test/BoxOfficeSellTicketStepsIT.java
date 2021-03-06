package test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import config.MailinatorEnum;
import model.Event;
import model.Purchase;
import model.User;
import pages.components.admin.TicketTypeRowComponent;
import pages.mailinator.MailinatorFactory;
import pages.mailinator.inbox.BOSellPagePurchaseWithCashMailinatorPage;
import test.facade.AdminBoxOfficeFacade;
import test.facade.AdminEventStepsFacade;
import test.facade.LoginStepsFacade;
import test.facade.OrganizationStepsFacade;
import utils.DataConstants;
import utils.ProjectUtils;

public class BoxOfficeSellTicketStepsIT extends BaseSteps {

	@Test(dataProvider = "box_office_sell_ticket_cash_payment", dependsOnMethods = {"prepareEventDataForBoxOfficeOrg"},
			priority = 16, retryAnalyzer = utils.RetryAnalizer.class)
	public void boxOfficeSellTicketCashPayment(User boxOfficeUser, Purchase purchase, User receiverOfTickets) {
		int ticketNumAdd = purchase.getNumberOfTickets();
		int ticketNumRemove = purchase.getRemoveNumberOfTickets();
		int addToTendered = purchase.getAdditionalTenderedAmount();
		maximizeWindow();
		LoginStepsFacade loginStepsFacade = new LoginStepsFacade(driver);
		AdminBoxOfficeFacade boxOfficeFacade = new AdminBoxOfficeFacade(driver);

		loginStepsFacade.givenUserIsLogedIn(boxOfficeUser);
		
		boxOfficeFacade.givenUserIsOnBoxOfficePage();
		boxOfficeFacade.givenBoxOfficeEventIsSelected(purchase.getEvent().getEventName());
		boxOfficeFacade.thenUserIsAtSellPage();

		TicketTypeRowComponent ticketTypeRow = boxOfficeFacade.whenUserSelectsTicketType();
		boxOfficeFacade.whenUserAddsQuantityAndClicksCheckout(ticketTypeRow, ticketNumAdd);
		boxOfficeFacade.thenCheckoutDialogIsVisible();
		boxOfficeFacade.whenUserClicksOnChangeTicketOnCheckoutDialog();
		boxOfficeFacade.whenUserRemovesQuantityAndClicksCheckout(ticketTypeRow, ticketNumRemove);
		boxOfficeFacade.whenUserPicksCashOption();
		
		boolean isChangeDueCorrect = boxOfficeFacade.whenUserEntersTenderedAndChecksChangeDueIsCorrect(ticketTypeRow, addToTendered);
		Assert.assertTrue(isChangeDueCorrect);
		Double totalAmount = boxOfficeFacade.whenUserChecksOrderTotal();
		boxOfficeFacade.whenUserEntersGuestInformationAndClicksOnCompleteOrder(receiverOfTickets, purchase.getOrderNote());
		boxOfficeFacade.thenUserShouldSeeOrderCompleteDialogAndGetOrderNumber();
		
		BOSellPagePurchaseWithCashMailinatorPage mailPage = (BOSellPagePurchaseWithCashMailinatorPage) MailinatorFactory
				.getInboxPage(MailinatorEnum.BO_SELL_WITH_CASH, driver, receiverOfTickets.getEmailAddress());
		boolean isCorrectAmountInMail = mailPage.openMailAndCheckValidity(totalAmount);
		Assert.assertTrue(isCorrectAmountInMail);
	}

	@Test(dataProvider = "prepare_event_for_box_office_cash_payment_data", priority = 16)
	public void prepareEventDataForBoxOfficeOrg(Event event, User superuser) throws Exception {
		maximizeWindow();
		LoginStepsFacade loginStepsFacade = new LoginStepsFacade(driver);
		OrganizationStepsFacade organizationFacade = new OrganizationStepsFacade(driver);
		AdminEventStepsFacade adminEventFacade = new AdminEventStepsFacade(driver);
		loginStepsFacade.givenAdminUserIsLogedIn(superuser);
		organizationFacade.givenOrganizationExist(event.getOrganization());
		adminEventFacade.givenUserIsOnAdminEventsPage();
		adminEventFacade.givenEventWithNameAndPredicateExists(event,
				comp -> comp.isEventPublished() && comp.isEventOnSale());
		loginStepsFacade.logOut();
	}

	@DataProvider(name = "prepare_event_for_box_office_cash_payment_data")
	public static Object[][] prepareData() throws Exception {
		Event event = Event.generateEventFromJson(DataConstants.BOX_OFFICE_USER_EVENTS_KEY, false, 1, 4);
		User user = User.generateUserFromJson(DataConstants.SUPERUSER_DATA_KEY);
	
		return new Object[][] {{event, user}};
	}

	@DataProvider(name = "box_office_sell_ticket_cash_payment")
	public static Object[][] prepareEvent() throws Exception {
		Event event = (Event) Event.generateEventFromJson(DataConstants.BOX_OFFICE_USER_EVENTS_KEY, false, 1, 4);
		Purchase purchase = Purchase.generatePurchaseFromJson(DataConstants.BOX_OFFICE_SELL_PURCHASE_STD_KEY);
		purchase.setEvent(event);
		User boxOfficeUser = User.generateUserFromJson(DataConstants.BOX_OFFICE_USER_KEY);
		User receiver = User.generateUserFromJson(DataConstants.USER_STANDARD_KEY);
		return new Object[][] {{boxOfficeUser,purchase,receiver}};
	}

}