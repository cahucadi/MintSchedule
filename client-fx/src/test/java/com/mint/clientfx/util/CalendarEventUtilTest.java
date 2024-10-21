package com.mint.clientfx.util;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.EventType;
import com.mint.clientfx.model.Instructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class CalendarEventUtilTest {

    private static List<Event> eventList;
    private static List<Instructor> instructorList;

    @BeforeAll
    static void setUp() throws Exception {
        instructorList = CalendarDataGenerator.generateInstructorData();
        eventList = CalendarDataGenerator.generateData(instructorList);
    }


    @Test
    void isDateOnRange() {
        Event projectEvent = eventList.stream().filter(event -> event.getEventType() == EventType.PROJECT).toList().getFirst();
        LocalDate outOfDateRange = projectEvent.getEndDate().plusDays(3);
        LocalDate inDateRange = projectEvent.getEndDate().minusDays(3);
        LocalDate sameStartDate = projectEvent.getStartDate();
        LocalDate sameEndDate = projectEvent.getEndDate();

        Assertions.assertFalse(CalendarEventUtil.isDateOnRange(projectEvent, outOfDateRange));
        Assertions.assertTrue(CalendarEventUtil.isDateOnRange(projectEvent, inDateRange));
        Assertions.assertTrue(CalendarEventUtil.isDateOnRange(projectEvent, sameStartDate));
        Assertions.assertTrue(CalendarEventUtil.isDateOnRange(projectEvent, sameEndDate));
    }

    @Test
    void hasValidDateRange() {

        Event projectEvent = eventList.stream().filter(event -> event.getEventType() == EventType.PROJECT).toList().getFirst();
        Event validEventAfter = cloneEvent(projectEvent);
        validEventAfter.setStartDate(validEventAfter.getEndDate().plusDays(2));
        validEventAfter.setEndDate(validEventAfter.getStartDate().plusDays(3));
        Event sameDatesEvent = cloneEvent(projectEvent);
        Event startDateOverlaps = cloneEvent(projectEvent);
        startDateOverlaps.setStartDate(startDateOverlaps.getStartDate().plusDays(2));
        startDateOverlaps.setEndDate(startDateOverlaps.getEndDate().plusDays(2));
        Event endDateOverlaps = cloneEvent(projectEvent);
        endDateOverlaps.setEndDate(endDateOverlaps.getStartDate().plusDays(2));
        endDateOverlaps.setStartDate(endDateOverlaps.getStartDate().minusDays(2));

        Assertions.assertFalse(CalendarEventUtil.eventsHasOverlapDate(projectEvent, validEventAfter));
        Assertions.assertTrue(CalendarEventUtil.eventsHasOverlapDate(projectEvent, sameDatesEvent));
        Assertions.assertTrue(CalendarEventUtil.eventsHasOverlapDate(projectEvent, startDateOverlaps));
        Assertions.assertTrue(CalendarEventUtil.eventsHasOverlapDate(projectEvent, endDateOverlaps));
    }

    @Test
    void generateEventMap() {

        Instructor instructor = instructorList.getFirst();
        List<Event> filteredEvents = CalendarEventUtil.filteredEventsByInstructor(eventList, instructor);
        Event projectEvent = filteredEvents.stream().filter(event -> event.getEventType() == EventType.PROJECT).toList().getFirst();

        int startDayTest = projectEvent.getStartDate().getDayOfMonth();
        int endDayTest = projectEvent.getEndDate().getDayOfMonth();
        int inBetweenDayTest = endDayTest - startDayTest + 2;

        Map<Integer, Event> eventsAsMap = CalendarEventUtil.generateEventMap(filteredEvents, projectEvent.getStartDate());
        Assertions.assertEquals(eventsAsMap.get(startDayTest), projectEvent);
        Assertions.assertEquals(eventsAsMap.get(endDayTest), projectEvent);
        Assertions.assertEquals(eventsAsMap.get(inBetweenDayTest), projectEvent);
    }

    @Test
    void filteredEventsByInstructor() {
        Instructor instructor = instructorList.getFirst();
        Assertions.assertFalse(CalendarEventUtil.filteredEventsByInstructor(eventList, instructor).isEmpty());
        Assertions.assertTrue(CalendarEventUtil.filteredEventsByInstructor(eventList, null).isEmpty());
    }

    private Event cloneEvent(Event event) {
        return new Event(event.getStartDate(), event.getEndDate(), event.getEventType(), event.getParticipants());
    }
}