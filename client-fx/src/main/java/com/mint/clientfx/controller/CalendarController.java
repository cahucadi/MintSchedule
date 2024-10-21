package com.mint.clientfx.controller;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.Instructor;
import com.mint.clientfx.service.CalendarEventService;
import com.mint.clientfx.view.EventDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class CalendarController implements Initializable {

    private static final int CALENDAR_ROWS = 6;
    private static final int CALENDAR_COLUMNS = 7;

    LocalDate current;
    LocalDate today;

    CalendarEventService calendarEventService;

    Instructor selectedInstructor;
    List<Instructor> instructors;
    Map<Integer, Event> monthEvents;

    private int eventDaysCounter;

    @FXML
    private FlowPane calendarViewPane;

    @FXML
    private Text overallDurationLabel;

    @FXML
    private Text monthLabel;

    @FXML
    private Text yearLabel;

    @FXML
    private ComboBox<Instructor> instructorCombobox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        calendarEventService = new CalendarEventService();
        instructors = calendarEventService.getInstructors();

        current = LocalDate.now();
        today = LocalDate.now();
        populateComboBox();
        drawCalendar();
    }

    @FXML
    void previousMonth(ActionEvent ignoredEvent) {
        current = current.plusMonths(-1);
        drawCalendar();
    }

    @FXML
    void nextMonth(ActionEvent ignoredEvent) {
        current = current.plusMonths(1);
        drawCalendar();
    }

    private void drawCalendar() {
        calendarViewPane.getChildren().clear();
        populateCalendar();
        calculateOverallDuration();
    }

    private void populateComboBox() {
        instructors.forEach(i -> instructorCombobox.getItems().add(i));
        instructorCombobox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, selected) -> {
            selectedInstructor = selected;
            drawCalendar();
        });
    }

    private void populateCalendar() {

        monthEvents = new HashMap<>();

        eventDaysCounter = 0;
        current = current.withDayOfMonth(1);

        monthLabel.setText(current.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        yearLabel.setText(String.valueOf(current.getYear()));

        double cellHeight = (calendarViewPane.getPrefHeight() / CALENDAR_ROWS) - 6;
        double cellWidth = (calendarViewPane.getPrefWidth() / CALENDAR_COLUMNS) - 11;

        int daysInMonth = current.lengthOfMonth();

        int dayOfWeekStart = current.getDayOfWeek().getValue();

        int dayMonthCounter = 1;

        monthEvents = calendarEventService.getEventMap(selectedInstructor, current);

        for (int i = 0; i < CALENDAR_ROWS * CALENDAR_COLUMNS; i++) {

            int currentDate = dayMonthCounter - dayOfWeekStart + 1;

            Event event = monthEvents.getOrDefault(currentDate, null);

            StackPane stackPane = new StackPane();

            Rectangle dayCell = new Rectangle();
            dayCell.setFill(Color.TRANSPARENT);
            dayCell.setHeight(cellHeight);
            dayCell.setWidth(cellWidth);
            dayCell.setStroke(currentDate > 0 ? Color.BLACK : Color.TRANSPARENT);
            dayCell.setStrokeWidth(1);

            if (dayMonthCounter >= dayOfWeekStart) {

                if (currentDate == today.getDayOfMonth() && current.getMonth() == today.getMonth()) {
                    dayCell.setStrokeWidth(2);
                }

                Text dayText = new Text(String.valueOf(currentDate));
                dayText.setFont(new Font(14));
                dayText.setTranslateX((cellWidth) * 0.40);
                dayText.setTranslateY((cellHeight) * -0.40);

                if (event != null) {

                    dayCell.setStroke(Color.valueOf(event.getEventType().getColor()));
                    dayCell.setId(String.valueOf(currentDate));
                    Tooltip tooltip = new Tooltip("Click to edit this event\nPress shift+click to delete");
                    tooltip.setFont(new Font(12));
                    tooltip.setShowDelay(Duration.millis(100));
                    Tooltip.install(dayCell, tooltip);

                    Text eventText = new Text(event.getEventType().toString());
                    eventText.setFont(new Font(12));
                    eventText.setTranslateX((cellWidth) * 0.05);
                    eventText.setTranslateY((cellHeight) * 0.05);
                    stackPane.getChildren().add(eventText);

                    eventDaysCounter++;
                } else if (selectedInstructor != null) {
                    Tooltip tooltip = new Tooltip("Click to add an event");
                    tooltip.setFont(new Font(12));
                    tooltip.setShowDelay(Duration.millis(100));
                    Tooltip.install(dayCell, tooltip);
                }

                dayCell.setOnMouseClicked(mouseEvent -> {
                    Rectangle source = (Rectangle) mouseEvent.getSource();
                    int day = Integer.parseInt(source.getId());

                    if (mouseEvent.isShiftDown()) {
                        deleteEvent(day);
                    } else {
                        manageEvent(day);
                    }
                });

                dayCell.setId(String.valueOf(currentDate));
                stackPane.getChildren().add(dayText);
            }

            if (currentDate > daysInMonth) {
                break;
            }

            stackPane.getChildren().add(dayCell);

            dayMonthCounter++;
            calendarViewPane.getChildren().add(stackPane);
        }
    }

    private void calculateOverallDuration() {
        if (selectedInstructor != null) {
            int duration = calendarEventService.getOverallDuration(selectedInstructor);
            overallDurationLabel.setText(selectedInstructor != null ? "You have scheduled " + eventDaysCounter + " days this month and an overall " + duration : "");
        }
    }

    public void manageEvent(int day) {

        Event event = monthEvents.getOrDefault(day, null);
        Event newEvent;
        LocalDate selectedDate = LocalDate.of(current.getYear(), current.getMonth(), day);

        if (selectedInstructor != null) {

            EventDialog eventDialog = new EventDialog(event, selectedInstructor, selectedDate);
            newEvent = eventDialog.showAndWait().orElse(null);

            if (newEvent != null) {

                try {
                    if (event == null) {
                        calendarEventService.addEvent(newEvent, selectedInstructor);
                    } else {
                        calendarEventService.updateEvent(event, newEvent, selectedInstructor);
                    }
                    drawCalendar();
                }catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Validation");
                    alert.setHeaderText("Invalid dates");
                    alert.setContentText("Check the dates you enter, there are events with conflicts");
                    alert.showAndWait();
                }
            }
        }
    }

    public void deleteEvent(int day) {

        Event event = monthEvents.getOrDefault(day, null);

        if (event != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Delete event");
            confirmDialog.setHeaderText("Delete event confirmation");
            confirmDialog.setContentText(String.format("Are you sure you want to delete %s from %s to %s?", event.getEventType(), event.getStartDate(), event.getEndDate()));
            ButtonType result = confirmDialog.showAndWait().orElse(null);

            if (result == ButtonType.OK) {
                calendarEventService.deleteEvent(event);
                drawCalendar();
            }
        }
    }

}