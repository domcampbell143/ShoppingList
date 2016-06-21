package uk.co.domcampbell.shoppinglist.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dominic on 14/06/16.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME ="ShoppingListDB";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DbSchema.ShoppingListTable.NAME + "("
                        + " _id integer primary key autoincrement, " +
                        DbSchema.ShoppingListTable.Cols.UUID + ", " +
                        DbSchema.ShoppingListTable.Cols.NAME +
                        ")"
        );
        db.execSQL(
                "CREATE TABLE " + DbSchema.ListItemTable.NAME + "("
                        + " _id integer primary key autoincrement, " +
                        DbSchema.ListItemTable.Cols.UUID + ", " +
                        DbSchema.ListItemTable.Cols.LISTUUID + ", " +
                        DbSchema.ListItemTable.Cols.ITEMNAME + ", " +
                        DbSchema.ListItemTable.Cols.COMPLETED + ", " +
                        DbSchema.ListItemTable.Cols.CREATEDDATE + ", " +
                        DbSchema.ListItemTable.Cols.COMPLETEDDATE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}