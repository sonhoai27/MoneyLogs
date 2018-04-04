package com.sonhoai.sonho.restful.Models;

/**
 * Created by sonho on 4/4/2018.
 */

public class Week {
    private int id;
    private String Name;

    public Week(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
