package uk.co.domcampbell.shoppinglist.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.UUID;

import javax.inject.Inject;

import uk.co.domcampbell.shoppinglist.ListActivity;
import uk.co.domcampbell.shoppinglist.ListPresenter;
import uk.co.domcampbell.shoppinglist.R;
import uk.co.domcampbell.shoppinglist.ShoppingListApplication;
import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;


/**
 * Created by Dominic on 4/06/16.
 */
public class ListFragment extends Fragment implements ListView {
    private static final String ARG_UUID = "list_uuid";

    private RecyclerView mRecyclerView;
    private ListItemAdapter mAdapter;

    private View mAddListItemView;
    private EditText mEditText;
    private ImageButton mAddButton;
    private ImageButton mCancelAddButton;

    private ShoppingList mShoppingList;
    @Inject
    ListPresenter mPresenter;

    public static ListFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID, uuid);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID);

        ((ShoppingListApplication) getActivity().getApplication()).getListPresenterComponent(uuid).inject(this);
        mPresenter.setView(this);
        mShoppingList = mPresenter.fetchList();
        getActivity().setTitle(mShoppingList.getListName());
        setHasOptionsMenu(true);
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
                mPresenter.cancelNewItemClicked();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
                mPresenter.onShareClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void notifyItemRemoved(int index) {
        mAdapter.notifyItemRemoved(index);
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
    public void notifyItemMoved(Integer from, Integer to) {
        mAdapter.notifyItemMoved(from,to);
    }

    @Override
    public void displayCreateItemView() {
        mEditText.setVisibility(View.VISIBLE);
        mAddButton.setBackgroundResource(R.drawable.round_button_green);
        mAddButton.setImageResource(R.drawable.ic_done_white_24dp);
        mCancelAddButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeCreateItemView() {
        mEditText.setText("");
        mEditText.setVisibility(View.GONE);
        mCancelAddButton.setVisibility(View.GONE);
        mAddButton.setBackgroundResource(R.drawable.round_button_pink);
        mAddButton.setImageResource(R.drawable.ic_add_white_24dp);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void displayNoTextError() {
        Toast.makeText(getActivity(), getString(R.string.empty_edit_text), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayDeleteListItemView(final ListItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Remove " + item.getItemName() + " from the list?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteListItem(item);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    public void launchShareAction() {
        Intent intent = ListActivity.shareIntent(getActivity(),mShoppingList);
        startActivity(intent);
    }

    private class ListItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private ListItem mListItem;
        private TextView mNameTextView;
        private TextView mCompletedDateTextView;

        public ListItemHolder(View itemView){
            super(itemView);
            mNameTextView = (TextView)itemView.findViewById(R.id.list_item_name);
            mCompletedDateTextView = (TextView)itemView.findViewById(R.id.list_item_completed_date);
            itemView.setOnLongClickListener(this);
        }

        public void bindListItem(ListItem item){
            mListItem = item;
            mNameTextView.setText(item.getItemName());
            if (item.isCompleted()) {
                mNameTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                mCompletedDateTextView.setText(DateFormat.getDateInstance().format(item.getCompletedDate()));
            } else {
                mNameTextView.setPaintFlags(0);
                mCompletedDateTextView.setText("");
            }
        }

        public void onSwiped(){
            mPresenter.listItemSwiped(mListItem);
        }

        @Override
        public boolean onLongClick(View v) {
            mPresenter.onItemLongClicked(mListItem);
            return true;
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
