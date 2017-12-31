package uk.co.domcampbell.shoppinglist.network.method;

import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

public class AddListMethod implements NetworkMethod {

    ShoppingList mShoppingList;

    /***
     * No arg constructor for jackson
     */
    public AddListMethod(){}

    public AddListMethod(ShoppingList shoppingList){
        mShoppingList = shoppingList;
    }

    @Override
    public void executeWith(ListService listService) {
        listService.addList(mShoppingList);
    }
}
