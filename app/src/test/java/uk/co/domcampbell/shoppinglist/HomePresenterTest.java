package uk.co.domcampbell.shoppinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.view.HomeView;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dominic on 23/06/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomePresenterTest {
    private HomePresenter mHomePresenter;

    @Mock
    private ListDatabase mListDatabase;

    @Mock
    private ListService mListService;

    @Mock
    private HomeView mHomeView;

    private ShoppingList mShoppingList;

    @Before
    public void setUp(){
        mShoppingList = new ShoppingList("Test list", null);
        Mockito.when(mListDatabase.getShoppingLists()).thenReturn(new ArrayList<ShoppingList>(Arrays.asList(mShoppingList)));
        mHomePresenter = new HomePresenter(mListDatabase, mListService);
        mHomePresenter.setView(mHomeView);
    }

    @Test
    public void onListClicked(){

        mHomePresenter.onListClicked(mShoppingList);

        Mockito.verify(mHomeView).launchListActivity(mShoppingList);
    }

    @Test
    public void onListLongClicked(){

        mHomePresenter.onListLongClicked(mShoppingList);

        Mockito.verify(mHomeView).displayContextView(mShoppingList);
    }

    @Test
    public void onListRenameClicked(){

        mHomePresenter.onListRenameClicked(mShoppingList);

        Mockito.verify(mHomeView).displayRenameView(mShoppingList);
    }

    @Test
    public void onListRenameSubmitted(){
        String newName = "test2";

        mHomePresenter.renameList(mShoppingList, newName);

        assertTrue(mShoppingList.getListName().equals(newName));
        Mockito.verify(mListDatabase).updateList(mShoppingList);
        Mockito.verify(mListService).updateListName(mShoppingList);
        Mockito.verify(mHomeView).notifyListChanged(mShoppingList);
    }

    @Test
    public void onCreateNewListClicked(){

        mHomePresenter.onCreateNewListClicked();

        Mockito.verify(mHomeView).displayNewListView();
    }

    @Test
    public void onCreateNewListSubmitted(){

        String newName = "newList";

        mHomePresenter.createNewList(newName);

        Mockito.verify(mListDatabase).addShoppingList(Matchers.isA(ShoppingList.class));
        Mockito.verify(mListService).addList(Matchers.isA(ShoppingList.class));
        Mockito.verify(mHomeView).notifyListAdded(Matchers.isA(ShoppingList.class));
    }

    @Test
    public void onDeleteListClicked(){

        mHomePresenter.deleteList(mShoppingList);

        Mockito.verify(mListDatabase).deleteShoppingList(mShoppingList);
        Mockito.verify(mListService).deleteShoppingList(mShoppingList);
        Mockito.verify(mHomeView).notifyListDeleted(Matchers.anyInt());
    }

}
