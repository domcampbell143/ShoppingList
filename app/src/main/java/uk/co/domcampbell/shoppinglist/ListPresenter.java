package uk.co.domcampbell.shoppinglist;

import java.util.Date;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.view.ListView;

/**
 * Created by Dominic on 2/06/16.
 */
public class ListPresenter {

    private static final String TAG = "ListPresenter";

    ListView mView;
    ListDatabase mListDatabase;
    ShoppingList mShoppingList;
    ListService mListService;

    boolean addEditTextVisible=false;

    public ListPresenter(ListDatabase listDatabase, ListService listService, UUID uuid) {
        mListDatabase = listDatabase;
        mListService = listService;
        mShoppingList = mListDatabase.getShoppingList(uuid);
    }

    public void setView(ListView listView){
        mView= listView;
    }


    public void createNewItemClicked(String itemName) {
        if (!addEditTextVisible) {
            mView.displayCreateItemView();
            addEditTextVisible=true;
        } else if (itemName.length() > 0){
            addEditTextVisible=false;
            ListItem item = new ListItem(itemName, false, new Date(), null);
            mShoppingList.addItem(item);
            mListDatabase.addItemToList(item, mShoppingList);
            mListService.addItemToList(item, mShoppingList);
            mView.notifyItemAdded(item);
            } else {
                mView.displayNoTextError();
        }
    }

    public void cancelNewItemClicked() {
        mView.removeCreateItemView();
        addEditTextVisible=false;
    }

    public void deleteListItem(ListItem item) {
        int index = mShoppingList.getList().indexOf(item);
        mShoppingList.removeItem(item);
        mListDatabase.removeItem(item);
        mListService.removeItemFromList(item, mShoppingList);
        mView.notifyItemRemoved(index);
    }

    public void listItemSwiped(ListItem item) {
        int from = mShoppingList.getList().indexOf(item);
        if (item.isCompleted()){
            item.setCompleted(false);
            item.setCompletedDate(null);
        } else {
            item.setCompleted(true);
            item.setCompletedDate(new Date());
        }
        mShoppingList.sortList();
        mListDatabase.updateItem(item);
        mListService.updateItemInList(item, mShoppingList);
        int to = mShoppingList.getList().indexOf(item);
        mView.notifyItemMoved(from, to);
        mView.notifyItemChanged(item);
    }


    public ShoppingList fetchList() {
        mListService.fetchListItems(mShoppingList, new ListService.ItemCallback() {
            @Override
            public void onItemAdded(ListItem item) {
                if (mShoppingList.getList().contains(item)){
                    onItemChanged(item);
                } else {
                    mShoppingList.addItem(item);
                    mListDatabase.addItemToList(item, mShoppingList);
                    mView.notifyItemAdded(item);
                }
            }

            @Override
            public void onItemChanged(ListItem item) {
                int index = mShoppingList.getList().indexOf(item);
                ListItem localItem = mShoppingList.getList().get(index);

                if (localItem.isCompleted() != item.isCompleted() || (localItem.getCompletedDate()==null?item.getCompletedDate()!=null:!localItem.getCompletedDate().equals(item.getCompletedDate()))){
                    localItem.setCompleted(item.isCompleted());
                    localItem.setCompletedDate(item.getCompletedDate());

                    mShoppingList.sortList();
                    mListDatabase.updateItem(item);
                    int to = mShoppingList.getList().indexOf(item);
                    mView.notifyItemMoved(index, to);
                    mView.notifyItemChanged(item);
                }
            }

            @Override
            public void onItemRemoved(ListItem item) {
                if (mShoppingList.getList().contains(item)) {
                    int index = mShoppingList.getList().indexOf(item);
                    mShoppingList.removeItem(item);
                    mListDatabase.removeItem(item);
                    mView.notifyItemRemoved(index);
                }
            }
        });
        return mShoppingList;
    }

    public void onItemLongClicked(ListItem listItem) {
        mView.displayDeleteListItemView(listItem);
    }

    public void onShareClicked() {
        mView.launchShareAction();
    }
}
