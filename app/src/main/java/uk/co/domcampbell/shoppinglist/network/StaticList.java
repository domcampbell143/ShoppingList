package uk.co.domcampbell.shoppinglist.network;

/**
 * Created by Dominic on 2/06/16.
 */
public class StaticList implements ListService{

    @Override
    public void textChanged(String text, TextChangedCallback callback) {
        callback.failure();
    }
}
