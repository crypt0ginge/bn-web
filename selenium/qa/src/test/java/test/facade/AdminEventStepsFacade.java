package test.facade;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import model.Event;
import pages.admin.events.AdminEventsPage;
import pages.admin.events.CreateEventPage;
import pages.components.admin.AdminEventComponent;
import pages.components.admin.AdminSideBar;
import pages.components.dialogs.DeleteEventDialog;
import utils.MsgConstants;
import utils.ProjectUtils;
import utils.SeleniumUtils;

public class AdminEventStepsFacade extends BaseFacadeSteps {

	private CreateEventPage createEventPage;
	private AdminEventsPage adminEvents;
	private AdminSideBar adminSideBar;
	
	private final String MANAGE_ORDER_FIRST_NAME_KEY = "mange_order_first_name";
	private final String MANAGE_ORDER_LAST_NAME_KEY = "manage_order_last_name";
	private final String MANAGE_ORDER_TICKET_NUMBER_KEY = "manage_order_ticket_number";
	
	private Map<String, Object> dataMap;

	public AdminEventStepsFacade(WebDriver driver) {
		super(driver);
		this.createEventPage = new CreateEventPage(driver);
		this.adminEvents = new AdminEventsPage(driver);
		this.adminSideBar = new AdminSideBar(driver);
		this.dataMap = new HashMap<>();
	}

	public void givenUserIsOnAdminEventsPage() {
		adminSideBar.clickOnEvents();
		adminEvents.isAtPage();
	}
	
	public AdminEventComponent givenEventExistAndIsNotCanceled(Event event) throws URISyntaxException {
		return givenEventWithNameAndPredicateExists(event, comp -> !comp.isEventCanceled());
	}
	
	public AdminEventComponent findEventIsOpenedAndHasSoldItem(Event event) throws URISyntaxException {
		AdminEventComponent selectedEvent =  adminEvents.findEvent(event.getEventName(),
				comp -> comp.isEventPublished() && comp.isEventOnSale() && comp.isSoldToAmountGreaterThan(0));
		return selectedEvent;
	}
	
	public AdminEventComponent givenEventWithNameAndPredicateExists(Event event,
			Predicate<AdminEventComponent> predicate) throws URISyntaxException {
		AdminEventComponent selectedEvent = adminEvents.findEvent(event.getEventName(), predicate);
		if (selectedEvent == null) {
			createNewRandomEvent(event);
			selectedEvent = adminEvents.findEvent(event.getEventName(), predicate);
		}
		return selectedEvent;
	}
	
	private Event createNewRandomEvent(Event event) throws URISyntaxException {
		event.setEventName(event.getEventName() + ProjectUtils.generateRandomInt(10000000));
		boolean retVal = createEvent(event);

		Assert.assertTrue(retVal,
				"Event with name: " + event.getEventName() + " does not exist and could not be created");
		String path = SeleniumUtils.getUrlPath(driver);
		retVal = retVal && path.contains("edit");
		Assert.assertTrue(retVal);
		adminSideBar.clickOnEvents();
		adminEvents.isAtPage();
		return event;
	}
	
	public boolean whenUserDeletesEvent(Event event) {
		AdminEventComponent component = adminEvents.findEventByName(event.getEventName());
		DeleteEventDialog deleteDialog = component.deleteEvent(event);
		if (adminEvents.isNotificationDisplayedWithMessage(MsgConstants.EVENT_DELETION_FAILED, 4)) {
			deleteDialog.clickOnKeepEvent();
			return false;
		}
		return true;
	}
	
	public void whenUserUpdatesDataOfEvent(Event event) {
		createEventPage.enterEventName(event.getEventName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), null, null, null);
	}
	
	public void whenUserClicksOnUpdateEvent() {
		createEventPage.clickOnUpdateButton();
	}

	public boolean whenUserEntesDataAndClicksOnSaveDraft(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
		createEventPage.clickOnSaveDraft();
		boolean retVal = createEventPage.checkSaveDraftMessage();
		return retVal;

	}
	
	public boolean thenEventShouldBeCanceled(Event event) {
		AdminEventComponent componentEvent = adminEvents.findEventByName(event.getEventName());
		if (componentEvent != null) {
			return componentEvent.isEventCanceled();
		} else {
			return false;
		}
	}
	
	public boolean thenUpdatedEventShoudExist(Event event) {
		AdminEventComponent component = this.adminEvents.findEventByName(event.getEventName());
		if (component != null ) {
			return component.checkIfDatesMatch(event.getStartDate());
		} else {
			return false;
		}
	}
	
	public boolean thenMessageNotificationShouldAppear(String msg) {
		return createEventPage.isNotificationDisplayedWithMessage(msg);
	}
	
	public boolean thenEventShouldBeDrafted(Event event) {
		AdminEventComponent component = adminEvents.findEventByName(event.getEventName());
		return component.isEventDrafted();
	}

	public boolean createEvent(Event event) {
		adminEvents.clickCreateEvent();
		createEventPage.isAtPage();
		createEventFillData(event);
		createEventPage.clickOnPublish();
		boolean retVal = createEventPage.checkMessage();
		return retVal;
	}
	
	private void createEventFillData(Event event) {
		createEventPage.clickOnImportSettingDialogNoThanks();
		createEventPage.enterArtistName(event.getArtistName());
		createEventPage.enterEventName(event.getEventName());
		createEventPage.selectVenue(event.getVenueName());
		createEventPage.enterDatesAndTimes(event.getStartDate(), event.getEndDate(), event.getStartTime(),
				event.getEndTime(), event.getDoorTime());
		createEventPage.addTicketTypes(event.getTicketTypes());
	}
	
	private void setData(String key, Object value) {
		dataMap.put(key, value);
	}

	private Object getData(String key) {
		return dataMap.get(key);
	}
}
