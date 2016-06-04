package uk.co.domcampbell.shoppinglist;

import java.util.Date;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 2/06/16.
 */
public class ListPresenter {

    ListView mView;
    ShoppingList mShoppingList;

    boolean addEditTextVisible=false;

    public ListPresenter(ListView view, ShoppingList shoppingList) {
        mView = view;
        mShoppingList = shoppingList;
    }


    public void createNewItemClicked(String itemName) {
        if (!addEditTextVisible) {
            mView.displayCreateItemView();
            addEditTextVisible=true;
        } else if (itemName.length() > 0){
            addEditTextVisible=false;
            ListItem item = new ListItem(itemName, false, new Date(), null);
            mShoppingList.addItem(item);
            mView.notifyItemAdded(item);
        } else {
            mView.displayNoTextError();
        }
    }

    public void cancelNewItemClicked() {
        mView.removeCreateItemView();
        addEditTextVisible=false;
    }

    public void deleteListItem(ListItem item) {
    }

    public void listItemSwiped(ListItem item) {
        item.setCompleted(!item.isCompleted());
        mView.notifyItemChanged(item);
    }


}
