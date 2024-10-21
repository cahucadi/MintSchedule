package com.mint.clientfx.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Event {

    private String identifier;
    private LocalDate startDate;
    private LocalDate endDate;
    private EventType eventType;
    private List<Instructor> participants; // Define as list for future implementation

    public Event(LocalDate startDate, LocalDate endDate, EventType eventType, Instructor instructor) {
        this( startDate, endDate,eventType, List.of(instructor));
    }

    public Event(LocalDate startDate, LocalDate endDate, EventType eventType, List<Instructor> participants) {
        this.identifier = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventType = eventType;
        this.participants = participants;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<Instructor> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Instructor> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Event{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", eventType=" + eventType +
                ", participants=" + participants +
                '}';
    }
}
