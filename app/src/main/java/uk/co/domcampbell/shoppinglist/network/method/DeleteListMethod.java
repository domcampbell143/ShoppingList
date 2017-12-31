package uk.co.domcampbell.shoppinglist.network.method;

import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

public class DeleteListMethod implements NetworkMethod {

    ShoppingList mShoppingList;

    /***
     * No arg constructor for jackson
     */
    public DeleteListMethod(){}

    public DeleteListMethod(ShoppingList shoppingList){
        mShoppingList = shoppingList;
    }

    @Override
    public void executeWith(ListService listService) {
        listService.deleteShoppingList(mShoppingList);
    }
}
