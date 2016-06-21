package uk.co.domcampbell.shoppinglist.database;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.co.domcampbell.shoppinglist.BuildConfig;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

import static org.junit.Assert.*;

/**
 * Created by Dominic on 20/06/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SqLiteListDatabaseTest {

    private SQLiteListDatabase mSQLiteListDatabase;
    private ShoppingList mShoppingList;
    private ListItem mItem;

    @Before
    public void setUp(){
        mSQLiteListDatabase = new SQLiteListDatabase(RuntimeEnvironment.application);
        List<ShoppingList> list = mSQLiteListDatabase.getShoppingLists();
        if (list.size() > 0){
            for (ShoppingList shoppingList: list){
                mSQLiteListDatabase.deleteShoppingList(shoppingList);
            }
        }
        mItem =  new ListItem("test1", false, new Date(), null);
        mShoppingList =  new ShoppingList("TestList1", Collections.singletonList(mItem));
        mSQLiteListDatabase.addShoppingList(mShoppingList);
    }

    @Test
    public void addShoppingList(){
        ListItem item =  new ListItem("test2", false, new Date(), null);
        ShoppingList list =  new ShoppingList("TestList2", Collections.singletonList(item));

        mSQLiteListDatabase.addShoppingList(list);

        ShoppingList returnedList = mSQLiteListDatabase.getShoppingList(list.getUUID());
        assertTrue(returnedList.getListName().equals("TestList2"));
        assertTrue(returnedList.getList().get(0).getItemName().equals("test2"));
    }

    @Test
    public void addItemToList(){
        ListItem item = new ListItem("test3", false, new Date(), null);
        int before = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID()).getList().size();

        mSQLiteListDatabase.addItemToList(item, mShoppingList);

        ListItem returnedItem = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID()).getList().get(before);
        assertTrue(returnedItem.getItemName().equals("test3"));

    }

    @Test
    public void getShoppingLists(){
        List<ShoppingList> lists = mSQLiteListDatabase.getShoppingLists();

        assertNotNull(lists.get(0));
    }

    @Test
    public void getShoppingList(){
        ShoppingList shoppingList = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID());

        assertTrue(shoppingList.getListName().equals("TestList1"));
    }

    @Test
    public void updateItem(){
        mItem.setCompleted(true);
        mItem.setCompletedDate(new Date());

        mSQLiteListDatabase.updateItem(mItem);

        ListItem returnedItem = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID()).getList().get(0);
        assertTrue(returnedItem.isCompleted());
        assertNotNull(returnedItem.getCompletedDate());
    }

    @Test
    public void removeItem(){
        int before = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID()).getList().size();

        mSQLiteListDatabase.removeItem(mItem);

        int after = mSQLiteListDatabase.getShoppingList(mShoppingList.getUUID()).getList().size();
        assertTrue(before==after+1);

    }

    @Test
    public void deleteShoppingList(){
        int before = mSQLiteListDatabase.getShoppingLists().size();

        mSQLiteListDatabase.deleteShoppingList(mShoppingList);

        int after = mSQLiteListDatabase.getShoppingLists().size();
        assertTrue(before==after+1);
    }

}
