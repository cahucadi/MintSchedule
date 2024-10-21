package com.mint.clientfx.util;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.Instructor;

import java.time.LocalDate;
import java.util.*;

public class CalendarEventUtil {

    /**
     * Returns true is an specific event has conflict dates with a reference event
     *
     * @param event        event used as reference
     * @param eventToCheck event to validate
     * @return true if the eventToCheck is between the start and end date of an event (inclusive)
     */
    public static boolean eventsHasOverlapDate(Event event, Event eventToCheck) {
        boolean overlapDay = event.getStartDate() == eventToCheck.getStartDate() && event.getEndDate() == eventToCheck.getEndDate();
        return overlapDay || (event.getStartDate().isBefore(eventToCheck.getEndDate()) && eventToCheck.getStartDate().isBefore(event.getEndDate()));
    }

    /**
     * Returns true is an specific date is between the start and end date of an event (inclusive)
     *
     * @param event       event used as reference
     * @param dateToCheck date to validate
     * @return true if the dateToCheck is between the start and end date of an event (inclusive)
     */
    public static boolean isDateOnRange(Event event, LocalDate dateToCheck) {
        return (event.getStartDate().equals(dateToCheck) || event.getStartDate().isBefore(dateToCheck))
                && (event.getEndDate().equals(dateToCheck) || event.getEndDate().isAfter(dateToCheck));
    }

    /**
     * Returns true is there is no date conflicts in an event start and end dates
     *
     * @param events A list of events to search for date conflicts
     * @param event  event to validate
     * @return true if there is not date conflicts
     */
    public static boolean hasValidDateRange(List<Event> events, Event event) {
        LocalDate startDate = event.getStartDate();
        LocalDate endDate = event.getEndDate();

        boolean validStartDate = !startDate.isAfter(endDate);
        List<Event> eventsInRange = events.stream().filter(ev -> eventsHasOverlapDate(ev, event)).toList();

        boolean isExisting = eventsInRange.size() == 1 && eventsInRange.getFirst().getIdentifier().equals(event.getIdentifier());
        return (eventsInRange.isEmpty() || isExisting) && validStartDate;
    }

    /**
     * Returns a Map of events in a month with the number of the day as key and the event as value
     *
     * @param events      a list of events to transform into a map
     * @param currentDate the current date used to define which month's events will converted into a map
     * @return a Map of events
     */
    public static Map<Integer, Event> generateEventMap(List<Event> events, LocalDate currentDate) {

        Map<Integer, Event> monthEvents = new HashMap<>();

        for (Event event : events) {
            for (int i = 1; i <= currentDate.lengthOfMonth(); i++) {
                LocalDate dateToCheck = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), i);

                if (CalendarEventUtil.isDateOnRange(event, dateToCheck)) {
                    monthEvents.put(i, event);
                }
            }
        }
        return monthEvents;
    }

    /**
     * Returns a List of events assigned to a specific instructor
     *
     * @param events     a list of events to filter
     * @param instructor the current instructor to filter
     * @return a List of events assigned to a specific instructor
     */
    public static List<Event> filteredEventsByInstructor(List<Event> events, Instructor instructor) {

        if (instructor != null)
            return events.stream().filter(ins -> ins.getParticipants().contains(instructor)).toList();

        return new ArrayList<>();
    }

}
