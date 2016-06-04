package uk.co.domcampbell.shoppinglist.network;

import android.text.Editable;

/**
 * Created by Dominic on 2/06/16.
 */
public interface ListService {

    void textChanged(String text, TextChangedCallback callback);

    interface TextChangedCallback {
        void success();
        void failure();
    }
}
