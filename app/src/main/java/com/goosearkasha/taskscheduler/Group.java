package com.goosearkasha.taskscheduler;
import java.io.Serializable;


public class Group implements Serializable{
    private int ID;
    private String title;
    private String description;

    public Group() {
        this.ID = 0;
        this.title = "Нет значения";
        this.description = "Нет значения";
    }

    public Group(int ID, String title, String description) {
        this.ID = ID;
        this.title = title;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
