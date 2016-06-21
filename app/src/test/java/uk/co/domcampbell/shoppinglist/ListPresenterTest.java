package uk.co.domcampbell.shoppinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;


/**
 * Created by Dominic on 2/06/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ListPresenterTest {

    private ListPresenter mListPresenter;

    @Mock
    private ListView mView;


    @Mock
    private ShoppingList mShoppingList;

    @Mock
    private ListDatabase mListDatabase;

    @Mock
    private ListService mListService;

    @Before
    public void setUp() {
        List<ShoppingList> lists = new ArrayList<>();
        lists.add(new ShoppingList("dummy", new ArrayList<ListItem>()));
        Mockito.when(mListDatabase.getShoppingLists()).thenReturn(lists);
        Mockito.when(mListDatabase.getShoppingList(Matchers.any(UUID.class))).thenReturn(mShoppingList);

        mListPresenter = new ListPresenter(mListDatabase, mListService);
        mListPresenter.setView(mView);

    }

    @Test
    public void whenAddNewItemClickedThenCancelled() {
        //given
        String itemName="";
        //when
        mListPresenter.createNewItemClicked(itemName);
        mListPresenter.cancelNewItemClicked();

        //then
        Mockito.verify(mView).displayCreateItemView();
        Mockito.verify(mView).removeCreateItemView();
        Mockito.verifyZeroInteractions(mShoppingList);
    }

    @Test
    public void whenAddNewItemClickedAndTextIsEnteredAndSubmitted() {
        //given
        String itemName = "test item";
        //when
        mListPresenter.createNewItemClicked(itemName);
        mListPresenter.createNewItemClicked(itemName);

        //then
        Mockito.verify(mView).displayCreateItemView();
        Mockito.verify(mShoppingList).addItem(Matchers.isA(ListItem.class));
        Mockito.verify(mListDatabase).addItemToList(Matchers.isA(ListItem.class), Matchers.eq(mShoppingList));
        Mockito.verify(mListService).addItemToList(Matchers.isA(ListItem.class), Matchers.eq(mShoppingList));
        Mockito.verify(mView).notifyItemAdded(Matchers.isA(ListItem.class));
    }

    @Test
    public void whenAddNewItemClickedAndNoTextIsEnteredAndSubmitted() {
        //given
        String itemName = "";
        //when
        mListPresenter.createNewItemClicked(itemName);
        mListPresenter.createNewItemClicked(itemName);

        //then
        Mockito.verify(mView).displayCreateItemView();
        Mockito.verifyZeroInteractions(mShoppingList);
        Mockito.verify(mView).displayNoTextError();

    }

    @Test
    public void whenListItemLongClicked() {
        //given
        ListItem item = new ListItem("test", false, null, null);
        //when
        mListPresenter.onItemLongClicked(item);
        //then
        Mockito.verify(mView).displayDeleteListItemView(item);

    }

    @Test
    public void whenDeleteListItemClicked() {
        //given
        ListItem item = new ListItem("test", false, null, null);
        //when
        mListPresenter.deleteListItem(item);
        //then
        Mockito.verify(mShoppingList).removeItem(item);
        Mockito.verify(mListDatabase).removeItem(item);
        Mockito.verify(mListService).removeItemFromList(item, mShoppingList);
        Mockito.verify(mView).notifyItemRemoved(Matchers.anyInt());

    }


    @Test
    public void whenListItemSwiped() {
        //given
        ListItem item = new ListItem("test", false, null, null);
        //when
        mListPresenter.listItemSwiped(item);
        //then
        Mockito.verify(mShoppingList).sortList();
        Mockito.verify(mListDatabase).updateItem(item);
        Mockito.verify(mListService).updateItemInList(item, mShoppingList);
        Mockito.verify(mView).notifyItemMoved(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mView).notifyItemChanged(item);
    }

    @Test
    public void whenFetchListCalled() {
        //given

        //when
        ShoppingList shoppingList = mListPresenter.fetchList();
        //then
        Mockito.verify(mListService).fetchList(Matchers.eq(mShoppingList),Matchers.isA(ListService.Callback.class));
        assertNotNull(shoppingList);
    }

}