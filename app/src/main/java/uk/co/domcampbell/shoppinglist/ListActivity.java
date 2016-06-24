package uk.co.domcampbell.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.view.ListFragment;

public class ListActivity extends SingleFragmentActivity {

    private static final String TAG = "ListActivity";
    private static final String UUID_EXTRA = "list_uuid";
    private static final String ID_PARAMETER = "id";
    private static final String NAME_PARAMETER = "name";

    @Inject
    ListService mListService;
    @Inject
    ListDatabase mListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ShoppingListApplication)getApplication()).getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent(Context context, ShoppingList shoppingList){
        Intent intent = new Intent(context,ListActivity.class);
        intent.putExtra(UUID_EXTRA, shoppingList.getUUID());
        return intent;
    }

    public static Intent shareIntent(Context context,ShoppingList shoppingList) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority("domcampbell.co.uk")
                .path("shoppinglist")
                .appendQueryParameter(ID_PARAMETER, shoppingList.getUUID().toString())
                .appendQueryParameter(NAME_PARAMETER, shoppingList.getListName())
                .build();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,
                new StringBuilder().append(context.getString(R.string.share_message))
                .append(uri.toString())
                .toString());
        intent.setType("text/plain");
        return intent;
    }



    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(UUID_EXTRA);
        if (uuid == null) {
            Uri data = this.getIntent().getData();
            String uuidString = data.getQueryParameter(ID_PARAMETER);
            uuid=UUID.fromString(uuidString);
            if (mListDatabase.getShoppingList(uuid)==null) {
                String name = data.getQueryParameter(NAME_PARAMETER);
                ShoppingList shoppingList = new ShoppingList(uuid, name, new ArrayList<ListItem>());
                mListDatabase.addShoppingList(shoppingList);
                mListService.addUserToNetworkList(shoppingList);
            }
        }
        return ListFragment.newInstance(uuid);
    }
}
