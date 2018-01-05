package com.databasefrontend;

import java.util.Comparator;

/**
 * Created by adityasahai on 16/04/17.
 */
public class TableIntComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Integer i1 = Integer.parseInt((String)o1);
        Integer i2 = Integer.parseInt((String)o2);
        return i1.compareTo(i2);
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj);
    }
}
