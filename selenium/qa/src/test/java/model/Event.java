package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import utils.DataConstants;
import utils.DataReader;
import utils.ProjectUtils;

public class Event implements Serializable {

	private static final long serialVersionUID = 6081396679346519203L;
	@JsonProperty("organization")
	private Organization organization;
	@JsonProperty("artist_name")
	private String artistName;
	@JsonProperty("event_name")
	private String eventName;
	@JsonProperty("venue_name")
	private String venueName;
	@JsonProperty("start_date")
	private String startDate;
	@JsonProperty("end_date")
	private String endDate;
	@JsonProperty("start_time")
	private String startTime;
	@JsonProperty("end_time")
	private String endTime;
	@JsonProperty("door_time")
	private String doorTime;
	@JsonProperty("ticket_types")
	private List<TicketType> ticketTypes = new ArrayList<>();

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDoorTime() {
		return doorTime;
	}

	public void setDoorTime(String doorTime) {
		this.doorTime = doorTime;
	}

	public List<TicketType> getTicketTypes() {
		return ticketTypes;
	}

	public void setTicketTypes(List<TicketType> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}

	public void addTicketType(TicketType ticketType) {
		this.ticketTypes.add(ticketType);
	}
	
	public void setDates(int offset, int range) {
		String[] dateSpan = ProjectUtils.getDatesWithSpecifiedRangeInDaysWithStartOffset(offset, range);
		this.startDate = dateSpan[0];
		this.endDate = dateSpan[1];
	}
	
	public void randomizeName() {
		this.eventName = this.eventName + ProjectUtils.generateRandomInt(DataConstants.RANDOM_NUMBER_SIZE_10M);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String[] fields = { this.eventName, this.artistName, this.venueName, this.startDate, this.endDate,
				this.startTime, this.endDate,
				this.getOrganization() != null ? this.getOrganization().getName() : null };
		ProjectUtils.appendFields(fields, sb);
		return sb.toString();

	}
	
	public static TypeReference<List<Event>> getListTypeReference() {
		return new TypeReference<List<Event>>() {
		};
	}
	
	public static TypeReference<Event> getTypeReference(){
		return new TypeReference<Event>() {
		};
	}

	public static Object[] generateEventsFromJson(String key, boolean randomizeName, int dateOffset, int dateRangeInDays) {
		Object[] events = DataReader.getInstance().getObjects(key, Event.getListTypeReference());
		for(Object e : events) {
			if(e instanceof Event) {
				Event event =(Event)e;
				event.setDates(dateOffset, dateRangeInDays);
				if(randomizeName)
					event.randomizeName();
			}
		}
		return events;
	}
	
	public static Event generateEventFromJson(String key, boolean randomizeName, int dateOffset, int dateRangeInDays) {
		Event event = (Event) DataReader.getInstance().getObject(key, Event.getTypeReference());
		event.setDates(dateOffset, dateRangeInDays);
		if(randomizeName) {
			event.randomizeName();
		}
		return event;
	}
	
	public static Event generateEventFromJson(String key, String replaceName ,
			boolean randomizeName, int dateOffset, int dateRangeInDays) {
		Event event = (Event) DataReader.getInstance().getObject(key, Event.getTypeReference());
		event.setEventName(replaceName);
		event.setDates(dateOffset, dateRangeInDays);
		if(randomizeName) {
			event.randomizeName();
		}
		return event;
	}

	public static Event generatedEvent(int daysOffsetStart, int dateRange, String eventName, boolean addRandomToName) {
		Event event = new Event();
		Organization organization = Organization.generateOrganization();
		organization.setName("Auto Test12");
		event.setOrganization(organization);
		event.setArtistName("TheTestArtistAuto");
		event.setEventName(eventName + (addRandomToName? ProjectUtils.generateRandomInt(10000000) : ""));
		event.setVenueName("MSG");
		String[] dateSpan = ProjectUtils.getDatesWithSpecifiedRangeInDaysWithStartOffset(daysOffsetStart, dateRange);
		String startDate = dateSpan[0];
		String endDate = dateSpan[1];
		event.setStartDate(startDate);
		event.setEndDate(endDate);
		event.setStartTime("08:30 PM");
		event.setEndTime("10:00 PM");
		event.setDoorTime("1");
		TicketType ticketType1 = new TicketType("GA", "100", "1");
		TicketType ticketType2 = new TicketType("VIP", "70", "2");
		event.addTicketType(ticketType1);
		event.addTicketType(ticketType2);

		return event;
	}
	
}
