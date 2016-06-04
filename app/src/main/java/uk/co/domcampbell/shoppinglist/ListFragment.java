package uk.co.domcampbell.shoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 4/06/16.
 */
public class ListFragment extends Fragment implements ListView {

    private RecyclerView mRecyclerView;
    private ListItemAdapter mAdapter;

    private View mAddListItemView;
    private EditText mEditText;
    private ImageButton mAddButton;
    private ImageButton mCancelAddButton;

    private ShoppingList mShoppingList;
    private ListPresenter mPresenter;

    public static ListFragment newInstance(){
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShoppingList = new ShoppingList("Test list", new ArrayList<ListItem>());
        mPresenter = new ListPresenter(this, mShoppingList);

        getActivity().setTitle(mShoppingList.getListName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListItemAdapter(mShoppingList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                removeCreateItemView();
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback(){
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof ListItemHolder) {
                    return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT)
                            | makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
                } else {
                    return 0;
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(viewHolder instanceof ListItemHolder) {
                    ((ListItemHolder) viewHolder).onSwiped();
                }
            }
        });
        helper.attachToRecyclerView(mRecyclerView);

        mAddListItemView = inflater.inflate(R.layout.add_list_item, null, false);
        mEditText = (EditText) mAddListItemView.findViewById(R.id.fragment_list_edittext_add);
        mAddButton = (ImageButton) mAddListItemView.findViewById(R.id.fragment_list_button_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.createNewItemClicked(mEditText.getText().toString());
            }
        });
        mCancelAddButton = (ImageButton) mAddListItemView.findViewById(R.id.fragment_list_button_add_cancel);
        mCancelAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.cancelNewItemClicked();
            }
        });

        return v;
    }

    @Override
    public void notifyItemRemoved(ListItem item) {
        mAdapter.notifyItemRemoved(mShoppingList.getList().indexOf(item));
    }

    @Override
    public void notifyItemChanged(ListItem item) {
        mAdapter.notifyItemChanged(mShoppingList.getList().indexOf(item));
    }

    @Override
    public void notifyItemAdded(ListItem item) {
        mAdapter.notifyItemInserted(mShoppingList.getList().indexOf(item));
    }

    @Override
    public void displayCreateItemView() {
        mEditText.setVisibility(View.VISIBLE);
        mAddButton.setImageResource(R.drawable.ic_done_white_24dp);
        mCancelAddButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeCreateItemView() {
        mEditText.setText("");
        mEditText.setVisibility(View.GONE);
        mCancelAddButton.setVisibility(View.GONE);
        mAddButton.setImageResource(R.drawable.ic_add_white_24dp);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void displayNoTextError() {
        Toast.makeText(getActivity(), getString(R.string.empty_edit_text), Toast.LENGTH_SHORT).show();
    }


    private class ListItemHolder extends RecyclerView.ViewHolder {

        private ListItem mListItem;
        private TextView mTextView;

        public ListItemHolder(View itemView){
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.list_item_name);
        }

        public void bindListItem(ListItem item){
            mListItem = item;
            mTextView.setText(item.getItemName());
            if (item.isCompleted()) {
                mTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mTextView.setPaintFlags(0);
            }
        }

        public void onSwiped(){
            mPresenter.listItemSwiped(mListItem);
        }

    }


    private class ListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LIST_ITEM = 0;
        private static final int ADD_NEW_BUTTON = 1;

        private ShoppingList mShoppingList;

        public ListItemAdapter (ShoppingList shoppingList) {
            mShoppingList = shoppingList;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mShoppingList.getList().size()){
                return ADD_NEW_BUTTON;
            } else {
                return LIST_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ADD_NEW_BUTTON) {
                return new RecyclerView.ViewHolder(mAddListItemView){};
            } else {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.list_item, parent, false);
                return new ListItemHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ListItemHolder) {

                ListItem item = mShoppingList.getList().get(position);
                ((ListItemHolder)holder).bindListItem(item);
            }
        }

        @Override
        public int getItemCount() {
            return mShoppingList.getList().size() + 1;
        }
    }
}
