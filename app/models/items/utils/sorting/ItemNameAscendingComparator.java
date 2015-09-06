package models.items.utils.sorting;

import models.items.Item;

import java.util.Comparator;

class ItemNameAscendingComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
