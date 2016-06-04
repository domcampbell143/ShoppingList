package uk.co.domcampbell.shoppinglist;

import uk.co.domcampbell.shoppinglist.dto.ListItem;

/**
 * Created by Dominic on 2/06/16.
 */
public interface ListView {

    void displayCreateItemView();

    void removeCreateItemView();

    void displayNoTextError();

    void notifyItemRemoved(ListItem item);

    void notifyItemChanged(ListItem item);

    /***
     * Notify the activity that the new item hsa been added. Should clean up the create item view
     * @param item The added list item
     */
    void notifyItemAdded(ListItem item);
}
