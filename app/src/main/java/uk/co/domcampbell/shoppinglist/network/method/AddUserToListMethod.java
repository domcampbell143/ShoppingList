package uk.co.domcampbell.shoppinglist.network.method;

import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 31/12/2017.
 */

public class AddUserToListMethod implements NetworkMethod {

    ShoppingList mShoppingList;

    /***
     * No arg constructor for jackson
     */
    public AddUserToListMethod(){}

    public AddUserToListMethod(ShoppingList shoppingList){
        mShoppingList = shoppingList;
    }

    @Override
    public void validate() throws InvalidNetworkMethodException {
        if (mShoppingList == null) throw new InvalidNetworkMethodException("ShoppingList is null");
    }

    @Override
    public void executeWith(ListService listService) {
        listService.addUserToNetworkList(mShoppingList);
    }
}
