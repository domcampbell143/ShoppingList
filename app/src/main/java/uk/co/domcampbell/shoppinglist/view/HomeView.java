package uk.co.domcampbell.shoppinglist.view;

import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 23/06/16.
 */
public interface HomeView {
    void launchListActivity(ShoppingList shoppingList);

    void displayRenameDialog(ShoppingList shoppingList);

    void notifyListChanged(ShoppingList shoppingList);

    void displayContextDialog(ShoppingList shoppingList);

    void displayNewListDialog();

    void notifyListAdded(ShoppingList a);

    void notifyListDeleted(int index);
}
