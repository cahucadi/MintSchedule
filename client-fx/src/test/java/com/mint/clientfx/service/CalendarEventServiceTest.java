package com.mint.clientfx.service;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.Instructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class CalendarEventServiceTest {

    private static List<Event> eventList;
    private static List<Instructor> instructorList;
    private static CalendarEventService calendarEventService;

    @BeforeAll
    static void setUp() throws Exception {
        calendarEventService = new CalendarEventService();
        instructorList = calendarEventService.getInstructors();
    }

    @Test
    void getEventsByInstructor() {
        Instructor instructor = instructorList.getFirst();
        Assertions.assertFalse(calendarEventService.getEventsByInstructor(instructor).isEmpty());
        Assertions.assertTrue(calendarEventService.getEventsByInstructor(null).isEmpty());
    }

    @Test
    void getEventMap() {

        Instructor instructor = instructorList.getFirst();
        Event event = calendarEventService.getEventsByInstructor(instructor).getFirst();

        int startDayTest = event.getStartDate().getDayOfMonth();
        int endDayTest = event.getEndDate().getDayOfMonth();
        int inBetweenDayTest = endDayTest - startDayTest + 2;

        Map<Integer, Event> eventsAsMap = calendarEventService.getEventMap( instructor, event.getStartDate() );
        Assertions.assertEquals(eventsAsMap.get(startDayTest), event);
        Assertions.assertEquals(eventsAsMap.get(endDayTest), event);
        Assertions.assertEquals(eventsAsMap.get(inBetweenDayTest), event);
    }

    @Test
    void getOverallDuration() {
        Instructor instructor = instructorList.getFirst();
        List<Event> event = calendarEventService.getEventsByInstructor(instructor);
        Assertions.assertEquals( 58, calendarEventService.getOverallDuration(instructor));
    }

    @Test
    void eventHasDateConflicts() {
    }

    @Test
    void addEvent() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void deleteEvent() {
    }
}