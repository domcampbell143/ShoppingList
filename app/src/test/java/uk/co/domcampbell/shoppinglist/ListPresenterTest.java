package uk.co.domcampbell.shoppinglist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;


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

    @Before
    public void setUp() {
        mListPresenter = new ListPresenter(mView, mShoppingList);
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
        Mockito.verify(mView).displayNoTextError();

    }

    @Test
    public void whenDeleteListItemClicked() {
        //given
        ListItem item = new ListItem("test", false, null, null);
        //when
        mListPresenter.deleteListItem(item);
        //then
        Mockito.verify(mShoppingList).removeItem(Matchers.isA(ListItem.class));
        Mockito.verify(mView).notifyItemRemoved(Matchers.isA(ListItem.class));

    }


    @Test
    public void whenListItemSwiped() {
        //given
        ListItem item = new ListItem("test", false, null, null);
        //when
        mListPresenter.listItemSwiped(item);
        //then
        Mockito.verify(mView).notifyItemChanged(Matchers.isA(ListItem.class));


    }


}