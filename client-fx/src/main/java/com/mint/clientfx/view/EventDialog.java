package com.mint.clientfx.view;

import com.mint.clientfx.model.Event;
import com.mint.clientfx.model.EventType;
import com.mint.clientfx.model.Instructor;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class EventDialog extends Dialog<Event> {
    private Event event;
    private final Instructor instructor;
    private final LocalDate selectedDate;

    public EventDialog(Event event, Instructor instructor, LocalDate selectedDate) {
        super();
        this.setTitle("Add/Edit Event for" + instructor);
        this.event = event;
        this.instructor = instructor;
        this.selectedDate = selectedDate;
        render();
    }

    void render() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker startDate = new DatePicker();
        startDate.setValue(event != null ? event.getStartDate() : selectedDate);
        DatePicker endDate = new DatePicker();
        endDate.setValue(event != null ? event.getEndDate() : selectedDate);

        ComboBox<EventType> eventType = new ComboBox<>();
        eventType.getItems().addAll(EventType.values());
        eventType.getSelectionModel().select(event != null ? event.getEventType() : null);

        grid.add(new Label("Start date:"), 0, 0);
        grid.add(startDate, 1, 0);
        grid.add(new Label("End date:"), 0, 1);
        grid.add(endDate, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(eventType, 1, 2);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (event != null && StringUtils.isNotEmpty(event.getIdentifier())) {
                    event.setStartDate(startDate.getValue());
                    event.setEndDate(endDate.getValue());
                    event.setEventType(eventType.getValue());
                } else {
                    event = new Event(startDate.getValue(), endDate.getValue(), eventType.getValue(), instructor);
                }
                return event;
            }
            return null;
        });

        this.getDialogPane().setContent(grid);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
    }


}
