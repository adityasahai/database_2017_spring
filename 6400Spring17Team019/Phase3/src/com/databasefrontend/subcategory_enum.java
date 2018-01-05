package com.databasefrontend;

/**
 * Created by Goutham on 3/18/2017.
 */
public enum subcategory_enum {
    personal_hygiene(0, "personal_hygiene"),
    clothing(1, "clothing"),
    shelter(2, "shelter"),
    other(3, "other"),
    vegetables(4, "vegetables"),
    nuts_grains_beans(5, "nuts/grains/beans"),
    meat_seafood(6, "meat/seafood"),
    dairy_eggs(7, "dairy/eggs"),
    sauce_condiments_seasoning(8, "sauce/condiments/seasoning"),
    juice_drink(9, "juice/drink");

    private final int value;
    private final String type;

    subcategory_enum(int value, String type) {
        this.value = value;
        this.type = type;
    }
    public String getString() {
return this.type;
}

    public static subcategory_enum getValue(String st) {
        for (subcategory_enum x: subcategory_enum.values()) {
            if (x.getValue().equalsIgnoreCase(st)) {
                return x;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getValue() {
        return type;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public int enumSize() {
        return this.enumSize();
    }
}
