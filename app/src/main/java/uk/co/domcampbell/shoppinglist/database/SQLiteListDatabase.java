package uk.co.domcampbell.shoppinglist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 14/06/16.
 */
public class SQLiteListDatabase implements ListDatabase {

    private SQLiteDatabase mDatabase;

    public SQLiteListDatabase(Context context){
        mDatabase = new DbHelper(context.getApplicationContext()).getWritableDatabase();
    }

    @Override
    public void addItemToList(ListItem item, ShoppingList shoppingList) {
        ContentValues values = getContentValues(item);
        values.put(DbSchema.ListItemTable.Cols.LISTUUID, shoppingList.getUUID().toString());

        mDatabase.insert(DbSchema.ListItemTable.NAME, null, values);
    }

    @Override
    public void removeItem(ListItem item) {
        mDatabase.delete(DbSchema.ListItemTable.NAME, DbSchema.ListItemTable.Cols.UUID + " = ?", new String[]{item.getUUID().toString()});
    }

    @Override
    public void updateItem(ListItem item) {
        ContentValues values = getContentValues(item);
        mDatabase.update(DbSchema.ListItemTable.NAME,values, DbSchema.ListItemTable.Cols.UUID + " = ?", new String[]{item.getUUID().toString()});
    }

    @Override
    public ShoppingList getShoppingList(UUID uuid) {
        List<ListItem> listItems= new ArrayList<>();
        String[] whereArgs = new String[]{uuid.toString()};
        ListItemWrapper listItemWrapper = queryListItems(DbSchema.ListItemTable.Cols.LISTUUID + " = ?", whereArgs);
        listItemWrapper.moveToFirst();

        while (!listItemWrapper.isAfterLast()){
            listItems.add(listItemWrapper.getListItem());
            listItemWrapper.moveToNext();
        }
        listItemWrapper.close();

        ShoppingListWrapper shoppingListWrapper = queryShoppingLists(DbSchema.ShoppingListTable.Cols.UUID + " = ?", whereArgs);

        ShoppingList shoppingList;
        try {
            if(shoppingListWrapper.getCount()==0) {
                return null;
            }

            shoppingListWrapper.moveToFirst();
            shoppingList = shoppingListWrapper.getShoppingList(listItems);
        } finally {
            shoppingListWrapper.close();
        }

        return shoppingList;
    }

    @Override
    public void addShoppingList(ShoppingList shoppingList) {
        ContentValues values = getContentValues(shoppingList);
        mDatabase.insert(DbSchema.ShoppingListTable.NAME, null, values);

        for (ListItem item : shoppingList.getList()){
            addItemToList(item, shoppingList);
        }
    }

    @Override
    public void deleteShoppingList(ShoppingList shoppingList) {
        String[] whereArgs = new String[]{shoppingList.getUUID().toString()};
        mDatabase.delete(DbSchema.ListItemTable.NAME, DbSchema.ListItemTable.Cols.LISTUUID + " = ?", whereArgs);
        mDatabase.delete(DbSchema.ShoppingListTable.NAME, DbSchema.ShoppingListTable.Cols.UUID + " = ?", whereArgs);
    }

    /***
     * Returns a list of all ShoppingLists in the database. Each will have null mList. Use getShoppingList(UUID uuid) for the mList.
     *
     * @return a list of all ShoppingLists in the db
     */
    @Override
    public List<ShoppingList> getShoppingLists() {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        ShoppingListWrapper wrapper = queryShoppingLists(null, null);
        wrapper.moveToFirst();

        while (!wrapper.isAfterLast()){
            shoppingLists.add(wrapper.getShoppingList(null));
            wrapper.moveToNext();
        }
        return shoppingLists;
    }

    private ContentValues getContentValues(ListItem item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.ListItemTable.Cols.UUID, item.getUUID().toString());
        contentValues.put(DbSchema.ListItemTable.Cols.ITEMNAME, item.getItemName());
        contentValues.put(DbSchema.ListItemTable.Cols.COMPLETED, item.isCompleted()?1:0);
        contentValues.put(DbSchema.ListItemTable.Cols.CREATEDDATE, (item.getCreatedDate() != null?item.getCreatedDate().getTime():null));
        contentValues.put(DbSchema.ListItemTable.Cols.COMPLETEDDATE, (item.getCompletedDate() != null?item.getCompletedDate().getTime():null));
        return contentValues;
    }

    private ContentValues getContentValues(ShoppingList shoppingList){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.ShoppingListTable.Cols.UUID, shoppingList.getUUID().toString());
        contentValues.put(DbSchema.ShoppingListTable.Cols.NAME, shoppingList.getListName());
        return contentValues;
    }

    private ListItemWrapper queryListItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.ListItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ListItemWrapper(cursor);
    }

    private ShoppingListWrapper queryShoppingLists(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DbSchema.ShoppingListTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ShoppingListWrapper(cursor);
    }
}
