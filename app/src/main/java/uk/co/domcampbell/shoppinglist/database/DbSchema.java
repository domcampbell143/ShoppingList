package uk.co.domcampbell.shoppinglist.database;

/**
 * Created by Dominic on 14/06/16.
 */
public class DbSchema {

    public static final class ListItemTable {
        public static final String NAME = "ListItems";

        public static final class Cols {
            public static final String LISTUUID = "listuuid";
            public static final String UUID = "uuid";
            public static final String ITEMNAME = "itemname";
            public static final String COMPLETED = "completed";
            public static final String CREATEDDATE = "createddate";
            public static final String COMPLETEDDATE = "completeddate";
        }
    }

    public static final class ShoppingListTable {
        public static final String NAME = "ShoppingLists";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
        }
    }
}
