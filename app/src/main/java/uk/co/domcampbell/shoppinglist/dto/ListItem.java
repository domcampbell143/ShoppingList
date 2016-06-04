package uk.co.domcampbell.shoppinglist.dto;

import java.util.Date;

/**
 * Created by Dominic on 3/06/16.
 */
public class ListItem {

    private String mItemName;
    private boolean completed;
    private Date mCreatedDate;
    private Date mCompletedDate;

    public ListItem(String itemName, boolean completed, Date createdDate, Date completedDate) {
        mItemName = itemName;
        this.completed = completed;
        mCreatedDate = createdDate;
        mCompletedDate = completedDate;
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
}
