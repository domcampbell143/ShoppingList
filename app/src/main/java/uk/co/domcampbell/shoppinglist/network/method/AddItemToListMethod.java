package uk.co.domcampbell.shoppinglist.network.method;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

public class AddItemToListMethod implements NetworkMethod {

    ShoppingList mShoppingList;
    ListItem mListItem;

    /***
     * No arg constructor for jackson
     */
    public AddItemToListMethod(){}

    public AddItemToListMethod(ListItem listItem, ShoppingList shoppingList){
        mShoppingList = shoppingList;
        mListItem = listItem;
    }

    @Override
    public void validate() throws InvalidNetworkMethodException {
        if (mShoppingList == null) throw new InvalidNetworkMethodException("ShoppingList is null");
        if (mListItem == null) throw new InvalidNetworkMethodException("ListItem is null");
    }

    @Override
    public void executeWith(ListService listService) {
        listService.addItemToList(mListItem, mShoppingList);
    }
}
