package com.example.todofx.data;

import java.time.LocalDate;

public class TaskEntity {
    private String name;
    private String description = "";
    private boolean isReady = false;

    private RegularRange regularRange = RegularRange.None;
    LocalDate date;

    public TaskEntity(
            String name_,
            String description_,
            RegularRange regularRange_,
            LocalDate date_
    ) {
        name = name_;
        if (description_ != null)
            description = description_;
        regularRange = regularRange_;
        date = date_;
    }
//todo название stage
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public RegularRange getRegularRange() {
        return regularRange;
    }

    public void setRegularRange(RegularRange regularRange) {
        this.regularRange = regularRange;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReady() {
        return isReady;
    }

    public enum RegularRange {
        None("Разовая"), Day("Ежедневная"), Week("Еженедельная"), Month("Ежемесячная");
        String range;
        RegularRange(String s) {
            range=s;
        }

        public String getRange() {
            return range;
        }
    }

}

