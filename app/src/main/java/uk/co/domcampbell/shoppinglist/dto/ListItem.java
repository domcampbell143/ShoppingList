package uk.co.domcampbell.shoppinglist.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dominic on 3/06/16.
 */
public class ListItem {

    private UUID mUUID;
    private String mItemName;
    private boolean completed;
    private Date mCreatedDate;
    private Date mCompletedDate;

    public ListItem(String itemName, boolean completed, Date createdDate, Date completedDate) {
        this(UUID.randomUUID(), itemName, completed, createdDate, completedDate);
    }

    public ListItem(UUID uuid, String itemName, boolean completed, Date createdDate, Date completedDate) {
        mUUID = uuid;
        mItemName = itemName;
        this.completed = completed;
        mCreatedDate = createdDate;
        mCompletedDate = completedDate;
    }


    public UUID getUUID() {
        return mUUID;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        mCreatedDate = createdDate;
    }

    public Date getCompletedDate() {
        return mCompletedDate;
    }

    public void setCompletedDate(Date completedDate) {
        mCompletedDate = completedDate;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof ListItem){
            return mUUID.equals(((ListItem) object).getUUID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mUUID.hashCode();
    }
}
