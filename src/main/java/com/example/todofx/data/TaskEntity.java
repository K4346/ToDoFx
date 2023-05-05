package com.example.todofx.data;

public class TaskEntity {
    private String name;
    private  String description = "";
    private boolean regular = false;
    private boolean isReady = false;
    String date;

    public TaskEntity(
            String name_,
            String description_,
            boolean regular_,
            String date_
    ) {
        name = name_;
        if (description_ != null)
            description = description_;
        regular = regular_;
        date = date_;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReady() {
        return isReady;
    }
}
