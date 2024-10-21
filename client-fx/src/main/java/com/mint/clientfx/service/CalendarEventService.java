package com.mint.clientfx.service;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.Instructor;
import com.mint.clientfx.util.CalendarDataGenerator;
import com.mint.clientfx.util.CalendarEventUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class CalendarEventService {

    List<Instructor> instructors;
    List<Event> events;


    public CalendarEventService() {
        instructors = CalendarDataGenerator.generateInstructorData();
        events = CalendarDataGenerator.generateData(instructors);
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public List<Event> getEventsByInstructor(Instructor instructor) {
        return CalendarEventUtil.filteredEventsByInstructor(events, instructor);
    }

    public Map<Integer, Event> getEventMap(Instructor instructor, LocalDate current) {
        return CalendarEventUtil.generateEventMap(getEventsByInstructor(instructor), current);
    }

    public int getOverallDuration(Instructor instructor) {
        List<Event> filteredEvents = CalendarEventUtil.filteredEventsByInstructor(events, instructor);
        return filteredEvents.stream().mapToInt(e -> (int) e.getStartDate().until(e.getEndDate(), ChronoUnit.DAYS) + 1).sum();
    }

    private void validateDateConflicts(Event newEvent, Instructor instructor) throws IllegalArgumentException {
        List<Event> filteredEvents = CalendarEventUtil.filteredEventsByInstructor(events, instructor);
        if (!CalendarEventUtil.hasValidDateRange(filteredEvents, newEvent)) {
            throw new IllegalArgumentException("Error: event dates have schedule conflicts");
        }
    }

    public void addEvent(Event event, Instructor instructor) throws IllegalArgumentException {
        validateDateConflicts(event, instructor);
        events.add(event);
    }

    public void updateEvent(Event event, Event newEvent, Instructor instructor) throws IllegalArgumentException {
        validateDateConflicts(event, instructor);
        events.set(events.indexOf(event), newEvent);
    }

    public void deleteEvent(Event event) {
        events.remove(event);
    }
}
