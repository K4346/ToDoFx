package com.example.todofx.data;

import java.io.Serializable;
import java.time.LocalDate;

public class TaskEntity implements Serializable {
    private String name;
    private String description = "";

    public boolean isOutOfTime() {
        return outOfTime;
    }

    private boolean isReady = false;

    private RegularRange regularRange = RegularRange.None;

    private Comment comment = Comment.in_progress;
    LocalDate date;

    private String reasonDateChange;
    private boolean outOfTime = false;

    public TaskEntity(
            String name_,
            String description_,
            RegularRange regularRange_,
            LocalDate date_,
            String reasonDateChange_
    ) {
        name = name_;
        if (description_ != null)
            description = description_;
        regularRange = regularRange_;
        date = date_;
        reasonDateChange = reasonDateChange_;
    }

    public boolean getOutOfTime() {
        return outOfTime;
    }

    public void setOutOfTime(boolean outOfTime) {
        this.outOfTime = outOfTime;
    }

    public String getReasonDateChange() {
        return reasonDateChange;
    }

    public void setReasonDateChange(String reasonDateChange) {
        this.reasonDateChange = reasonDateChange;
    }

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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public enum RegularRange {
        None("Разовая"), Day("Ежедневная"), Week("Еженедельная"), Month("Ежемесячная");
        private String range;

        RegularRange(String s) {
            range = s;
        }

        public String getRange() {
            return range;
        }
    }

    public enum Comment {
        in_progress(""), ok("Выполено в срок"), not_quit_ok("Выполнено не в срок"), no_ok("Не выполнено в срок"), range_ok("Выполнено в текущем спринте"), range_no_ok("Не выполнено в прошлом спринте");
        String value;

        Comment(String c) {
            value = c;
        }

        public String getValue() {
            return value;
        }
    }

}

