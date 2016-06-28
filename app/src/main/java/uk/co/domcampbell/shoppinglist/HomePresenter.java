package uk.co.domcampbell.shoppinglist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import uk.co.domcampbell.shoppinglist.dagger.ActivityScope;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.view.HomeView;

/**
 * Created by Dominic on 23/06/16.
 */
@ActivityScope
public class HomePresenter {
    
    private ListDatabase mListDatabase;
    private ListService mListService;
    private HomeView mHomeView;

    private List<ShoppingList> mShoppingLists;

    @Inject
    public HomePresenter(ListDatabase db, ListService service){
        mListDatabase= db;
        mListService=service;

        mShoppingLists = mListDatabase.getShoppingLists();
    }

    public void setView(HomeView homeView){
        mHomeView = homeView;
    }

    public void onListClicked(ShoppingList shoppingList) {
        mHomeView.launchListActivity(shoppingList);
    }

    public void onListRenameClicked(ShoppingList shoppingList) {
        mHomeView.displayRenameView(shoppingList);
    }

    public void renameList(ShoppingList shoppingList, String newName) {
        shoppingList.setListName(newName);
        mListDatabase.updateList(shoppingList);
        mListService.updateListName(shoppingList);
        mHomeView.notifyListChanged(shoppingList);
    }

    public void onListLongClicked(ShoppingList shoppingList) {
        mHomeView.displayContextView(shoppingList);
    }

    public void onCreateNewListClicked() {
        mHomeView.displayNewListView();
    }

    public void createNewList(String newName) {
        ShoppingList shoppingList = new ShoppingList(newName, new ArrayList<ListItem>());
        mShoppingLists.add(shoppingList);
        mListDatabase.addShoppingList(shoppingList);
        mListService.addList(shoppingList);
        mHomeView.notifyListAdded(shoppingList);
    }

    public void deleteList(ShoppingList shoppingList) {
        mListDatabase.deleteShoppingList(shoppingList);
        mListService.deleteShoppingList(shoppingList);
        int index = mShoppingLists.indexOf(shoppingList);
        mShoppingLists.remove(index);
        mHomeView.notifyListDeleted(index);
    }

    public List<ShoppingList> getShoppingLists() {
        for (final ShoppingList shoppingList:mShoppingLists){
            mListService.fetchListName(shoppingList, new ListService.NameCallback() {
                @Override
                public void onNameReceived(String name) {
                    if (!shoppingList.getListName().equals(name)){
                        shoppingList.setListName(name);
                        mListDatabase.updateList(shoppingList);
                        mHomeView.notifyListChanged(shoppingList);
                    }
                }
            });
        }

        return mShoppingLists;
    }

}
