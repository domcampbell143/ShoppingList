package uk.co.domcampbell.shoppinglist.network.method;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

public class UpdateItemInListMethod implements NetworkMethod {

    ShoppingList mShoppingList;
    ListItem mListItem;

    /***
     * No arg constructor for jackson
     */
    public UpdateItemInListMethod(){}

    public UpdateItemInListMethod(ListItem listItem, ShoppingList shoppingList){
        mShoppingList = shoppingList;
        mListItem = listItem;
    }

    @Override
    public void executeWith(ListService listService) {
        listService.updateItemInList(mListItem, mShoppingList);
    }
}
