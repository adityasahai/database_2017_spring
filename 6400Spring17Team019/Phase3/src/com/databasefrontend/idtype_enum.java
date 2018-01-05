package com.databasefrontend;

/**
 * Created by Goutham on 3/18/2017.
 */
public enum idtype_enum {
    driving_license(1, "driving_license"),
    ssn(2, "ssn"),
    passport(3, "passport"),
    birth_certificate(4, "birth_certificate"),
    refugee_travel_document(5, "refugee_travel_document"),
    visa(6, "visa"),
    invalid(100, "always_the_last_one");

    private final int value;
    private final String name;
    private idtype_enum(int value, String name) {
        this.value = value;
        this.name = name;

    }
    public String getString(int value) {
        return this.name;
    }
    public int  getvalueof(String st) {
        for (idtype_enum x: idtype_enum.values()) {
            if (x.name.equals(st)) {
                return x.value;
            }
        }
        return -1;
    }
};
