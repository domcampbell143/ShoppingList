package uk.co.domcampbell.shoppinglist.view;

import android.content.Context;

import uk.co.domcampbell.shoppinglist.dto.ListItem;

/**
 * Created by Dominic on 2/06/16.
 */
public interface ListView {

    void displayCreateItemView();

    void removeCreateItemView();

    void displayNoTextError();

    void notifyItemRemoved(int index);

    void notifyItemChanged(ListItem item);

    /***
     * Notify the activity that the new item has been added. Should clean up the create item view
     * @param item The added list item
     */
    void notifyItemAdded(ListItem item);

    void notifyItemMoved(Integer from, Integer to);

    void displayDeleteListItemView(ListItem item);

    void launchShareAction();
}
