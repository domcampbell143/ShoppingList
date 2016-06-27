package uk.co.domcampbell.shoppinglist.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dominic on 2/06/16.
 */
public class ShoppingList {

    private String mListName;
    private List<ListItem> mList;
    private UUID mUUID;

    public ShoppingList(UUID uuid, String listName, List<ListItem> list){
        mUUID = uuid;
        mListName = listName;
        mList = list;
    }

    public ShoppingList(String listName, List<ListItem> list) {
        this(UUID.randomUUID(), listName, list);
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String listName) {
        mListName = listName;
    }

    public List<ListItem> getList() {
        return mList;
    }

    public void addItem(ListItem item) {
        mList.add(item);
    }

    public void removeItem(ListItem item) { mList.remove(item);
    }

    public void sortList() {
        Collections.sort(mList, new ListItemComparator());
    }

    public static class ListItemComparator implements Comparator<ListItem> {
        @Override
        public int compare(ListItem lhs, ListItem rhs) {
            if (lhs.isCompleted()){
                if (rhs.isCompleted()){
                    return lhs.getCompletedDate().compareTo(rhs.getCompletedDate());
                } else {
                    return -1;
                }
            } else {
                if (rhs.isCompleted()){
                    return 1;
                } else {
                    return lhs.getCreatedDate().compareTo(rhs.getCreatedDate());
                }
            }
        }
    }
}
