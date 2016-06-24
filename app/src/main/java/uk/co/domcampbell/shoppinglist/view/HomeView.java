package uk.co.domcampbell.shoppinglist.view;

import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 23/06/16.
 */
public interface HomeView {
    void launchListActivity(ShoppingList shoppingList);

    void displayRenameView(ShoppingList shoppingList);

    void notifyListChanged(ShoppingList shoppingList);

    void displayContextView(ShoppingList shoppingList);

    void displayNewListView();

    void notifyListAdded(ShoppingList a);

    void notifyListDeleted(int index);
}
