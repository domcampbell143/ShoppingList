package uk.co.domcampbell.shoppinglist;

import android.support.v4.app.Fragment;

import uk.co.domcampbell.shoppinglist.SingleFragmentActivity;
import uk.co.domcampbell.shoppinglist.view.HomeFragment;

/**
 * Created by Dominic on 23/06/16.
 */
public class HomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }
}
