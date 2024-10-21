package com.mint.clientfx.util;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.EventType;
import com.mint.clientfx.model.Instructor;
import net.datafaker.Faker;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class CalendarDataGenerator {

    private static final int TOTAL_INSTRUCTORS = 5;

    private static final Faker faker = new Faker();

    public static List<Event> generateData(List<Instructor> instructorList) {

        List<Event> events = new ArrayList<>();

        events.add(generateEvent(generateDateRange(0, 0), instructorList.getFirst(), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(1, 1), instructorList.getFirst(), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(-3, 0), instructorList.getFirst(), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(3, 0), instructorList.getFirst(), EventType.TIME_OFF));
        events.add(generateEvent(generateFixedDate(23, 10), instructorList.getFirst(), EventType.APPOINTMENT));

        events.add(generateEvent(generateDateRange(3, 0), instructorList.get(1), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(0, 1), instructorList.get(1), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(-2, 0), instructorList.get(1), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(3, 1), instructorList.get(1), EventType.TIME_OFF));
        events.add(generateEvent(generateFixedDate(17, 10), instructorList.get(1), EventType.APPOINTMENT));

        events.add(generateEvent(generateDateRange(1, 1), instructorList.get(2), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(1, -1), instructorList.get(2), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(-2, 0), instructorList.get(2), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(3, 1), instructorList.get(2), EventType.TIME_OFF));
        events.add(generateEvent(generateFixedDate(12, 9), instructorList.get(2), EventType.APPOINTMENT));

        events.add(generateEvent(generateDateRange(2, 1), instructorList.get(3), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(0, 0), instructorList.get(3), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(2, -1), instructorList.get(3), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(3, 0), instructorList.get(3), EventType.TIME_OFF));
        events.add(generateEvent(generateFixedDate(5, 11), instructorList.get(3), EventType.APPOINTMENT));

        events.add(generateEvent(generateDateRange(3, 1), instructorList.get(4), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(0, -1), instructorList.get(4), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(-2, 0), instructorList.get(4), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(2, 0), instructorList.get(4), EventType.TIME_OFF));
        events.add(generateEvent(generateFixedDate(7, 10), instructorList.get(4), EventType.APPOINTMENT));

        // Extra scheduled
        events.add(generateEvent(generateDateRange(0, -2), instructorList.get(0), EventType.SEMINAR));
        events.add(generateEvent(generateDateRange(0, 2), instructorList.get(1), EventType.PROJECT));
        events.add(generateEvent(generateDateRange(3, -2), instructorList.get(0), EventType.APPOINTMENT));

        return events;
    }


    public static List<Instructor> generateInstructorData() {

        List<Instructor> instructors = new ArrayList<>();

        for (int i = 0; i < TOTAL_INSTRUCTORS; i++) {
            instructors.add(new Instructor(faker.name().firstName(), faker.name().firstName(), faker.timeAndDate().birthday()));
        }

        return instructors;
    }

    private static LocalDate generateDateRange(int weeks, int months) {
        LocalDate date = LocalDate.now();

        // For simplicity lets use the first monday as reference
        // Select the month, find the first monday and then decide the week
        return date.plusMonths(months).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)).plusWeeks(weeks);
    }

    private static LocalDate generateFixedDate(int day, int month) {
        return LocalDate.of(LocalDate.now().getYear(), month, day);
    }

    private static Event generateEvent(LocalDate startDate, Instructor instructor, EventType eventType) {
        LocalDate endDate = startDate.plusDays(0);

        switch (eventType) {
            case PROJECT, TIME_OFF -> endDate = endDate.plusDays(6);
            case SEMINAR -> endDate = endDate.plusDays(13);
            case APPOINTMENT -> endDate = endDate.plusDays(0);
        }
        return new Event(startDate, endDate, eventType, instructor);
    }

}
