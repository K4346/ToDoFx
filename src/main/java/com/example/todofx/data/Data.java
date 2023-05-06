package com.example.todofx.data;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Data {

    public ArrayList<TaskEntity> tasks = new ArrayList<>();

    String fileName = "tasks.txt";

    public void loadData() {

        try {

            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            tasks = (ArrayList<TaskEntity>) in.readObject();

            in.close();
            fileIn.close();

            sortDataByDate();

            checkOutOfTime();
            checkProgress();

            changeRegularDate();
            changeRegularCheckBoxes();

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("ошибка десериализации");
        }
    }

    private void changeRegularCheckBoxes() {
        for (TaskEntity task : tasks) {
            if (task.isReady() && task.isOutOfTime() && task.getRegularRange() != TaskEntity.RegularRange.None) {
                task.setReady(false);
            }
        }

    }

    private void changeRegularDate() {
        LocalDate today = LocalDate.now();
        for (TaskEntity task : tasks) {
            LocalDate date = task.getDate();
            if (today.isAfter(task.getDate())) {
                switch (task.getRegularRange()) {
                    case None -> {
                        break;
                    }
                    case Day -> {
                        task.setDate(today);
                        break;
                    }
                    case Week -> {
                        while (today.isAfter(date)) {
                            date = date.plusWeeks(1);
                        }
                        task.setDate(date);
                        break;
                    }
                    case Month -> {
                        while (today.isAfter(date)) {
                            date = date.plusMonths(1);
                        }
                        task.setDate(date);
                        break;
                    }
                }

            }
        }
    }

    private void checkOutOfTime() {
        for (TaskEntity task : tasks) {
            task.setOutOfTime(checkOutOfTimeByItem(task));
        }
    }

    private boolean checkOutOfTimeByItem(TaskEntity task) {
        LocalDate today = LocalDate.now();
        if (!task.isReady()) {
            if (today.isAfter(task.getDate())) {
                return true;
            } else {
                return false;
            }
        }

        return task.getOutOfTime();

    }

    private void checkProgress() {
        for (TaskEntity task : tasks) {
            task.setComment(checkProgressByItem(task));
        }
    }

    public TaskEntity.Comment checkProgressByItem(TaskEntity task) {
        if (task.getRegularRange() == TaskEntity.RegularRange.None) {
            if (task.isReady() && task.isOutOfTime()) {
                return TaskEntity.Comment.not_quit_ok;
            } else if (task.isReady() && !task.isOutOfTime()) {
                return TaskEntity.Comment.ok;
            } else if (!task.isReady() && !task.isOutOfTime()) {
                return TaskEntity.Comment.in_progress;
            } else if (!task.isReady() && task.isOutOfTime()) {
                return TaskEntity.Comment.no_ok;
            }
        } else {
            if (task.isReady() && task.isOutOfTime()) {
                return TaskEntity.Comment.range_no_ok;
            } else if (task.isReady() && !task.isOutOfTime()) {
                return TaskEntity.Comment.range_ok;
            } else if (!task.isReady() && !task.isOutOfTime()) {
                return TaskEntity.Comment.in_progress;
            } else if (!task.isReady() && task.isOutOfTime()) {
                return TaskEntity.Comment.range_no_ok;
            }
        }
        return TaskEntity.Comment.in_progress;
    }

    private void sortDataByDate() {
        Comparator<TaskEntity> comparator = Comparator.comparing(TaskEntity::getDate);
        tasks.sort(comparator);
    }

    public void saveData(ArrayList<TaskEntity> tasks) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(tasks);

            out.close();
            fileOut.close();

            System.out.println("Список задач сохранен");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public ArrayList<TaskEntity> getTasks() {
        if (tasks.isEmpty())
            loadData();
        return tasks;
    }

    public void setTasks(ArrayList<TaskEntity> tasks) {
        this.tasks = tasks;
        saveData(tasks);
    }
}
