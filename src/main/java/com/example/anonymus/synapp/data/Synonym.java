package com.example.anonymus.synapp.data;

/**
 * Created by anonymus on 06.03.17.
 */

public class Synonym {

    private int id;

    private String name = null;
    private String alt1 = null;
    private String alt2 = null;
    private String alt3 = null;
    private String desc = null;

    private static int count_instances = 0;

    public Synonym(String name, String alt1, String alt2, String alt3, String desc) {

        this.id = count_instances;

        this.name = name;
        this.alt1 = alt1;
        this.alt2 = alt2;
        this.alt3 = alt3;
        this.desc = desc;

        count_instances = count_instances + 1;
    }

    /*
    *   Getter and Setter methods
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlt1() {
        return alt1;
    }

    public void setAlt1(String alt1) {
        this.alt1 = alt1;
    }

    public String getAlt2() {
        return alt2;
    }

    public void setAlt2(String alt2) {
        this.alt2 = alt2;
    }

    public String getAlt3() {
        return alt3;
    }

    public void setAlt3(String alt3) {
        this.alt3 = alt3;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
