package uk.co.domcampbell.shoppinglist.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.UUID;

import uk.co.domcampbell.shoppinglist.SingleFragmentActivity;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

public class ListActivity extends SingleFragmentActivity {

    private static final String UUID_EXTRA = "list_uuid";

    public static Intent newIntent(Context context, ShoppingList shoppingList){
        Intent intent = new Intent(context,ListActivity.class);
        intent.putExtra(UUID_EXTRA, shoppingList.getUUID());
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(UUID_EXTRA);
        return ListFragment.newInstance(uuid);
    }
}
