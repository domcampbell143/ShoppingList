package uk.co.domcampbell.shoppinglist.dto;

import java.util.List;

/**
 * Created by Dominic on 2/06/16.
 */
public class ShoppingList {

    private String mListName;
    private List<ListItem> mList;

    public ShoppingList(String listName, List<ListItem> list) {
        mListName = listName;
        mList = list;
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

    public void setList(List<ListItem> list) {
        mList = list;
    }

    public void addItem(ListItem item) {
        mList.add(item);
    }

    public void removeItem(ListItem a) {
    }
}
