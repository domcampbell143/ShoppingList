package uk.co.domcampbell.shoppinglist.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import uk.co.domcampbell.shoppinglist.HomePresenter;
import uk.co.domcampbell.shoppinglist.ListActivity;
import uk.co.domcampbell.shoppinglist.R;
import uk.co.domcampbell.shoppinglist.ShoppingListApplication;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 23/06/16.
 */
public class HomeFragment extends Fragment implements HomeView {

    @Inject HomePresenter mPresenter;
    private List<ShoppingList> mShoppingLists;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private ShoppingListAdapter mAdapter;
    private AlertDialog mAlertDialog;


    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ShoppingListApplication)getActivity().getApplication()).getHomePresenterComponent().inject(this);
        mPresenter.setView(this);
        mShoppingLists = mPresenter.getShoppingLists();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_home_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ShoppingListAdapter(mShoppingLists);
        mRecyclerView.setAdapter(mAdapter);

        mFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.fragment_home_fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onCreateNewListClicked();
            }
        });

        return v;
    }

    @Override
    public void launchListActivity(ShoppingList shoppingList) {
        Intent intent = ListActivity.newIntent(getActivity(), shoppingList);
        startActivity(intent);
    }

    @Override
    public void displayRenameView(final ShoppingList shoppingList) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.rename_shopping_list, shoppingList.getListName()));
        final EditText input=new EditText(getActivity());
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.renameList(shoppingList, input.getText().toString());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void notifyListChanged(ShoppingList shoppingList) {
        mAdapter.notifyItemChanged(mShoppingLists.indexOf(shoppingList));
    }

    @Override
    public void displayContextView(final ShoppingList shoppingList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(shoppingList.getListName())
                .setItems(R.array.context_shopping_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //Rename
                            case 0:mPresenter.onListRenameClicked(shoppingList);
                                break;
                            case 1:mPresenter.deleteList(shoppingList);
                                default:dialog.cancel();
                        }


                    }
                });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void displayNewListView() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.name_shopping_list));
        final EditText input=new EditText(getActivity());
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.createNewList(input.getText().toString());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void notifyListAdded(ShoppingList shoppingList) {
        mAdapter.notifyItemInserted(mShoppingLists.indexOf(shoppingList));
    }

    @Override
    public void notifyListDeleted(int index) {
        mAdapter.notifyItemRemoved(index);
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ShoppingList mShoppingList;
        private TextView mTextView;

        public ShoppingListHolder(View itemView){
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.shopping_list_text_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindShoppingList(ShoppingList shoppingList){
            mShoppingList = shoppingList;
            mTextView.setText(mShoppingList.getListName());
        }

        @Override
        public void onClick(View v) {
            mPresenter.onListClicked(mShoppingList);
        }

        @Override
        public boolean onLongClick(View v) {
            mPresenter.onListLongClicked(mShoppingList);
            return true;
        }
    }

    private class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListHolder>{

        private List<ShoppingList> mShoppingLists;

        public ShoppingListAdapter(List<ShoppingList> shoppingLists){
            mShoppingLists = shoppingLists;
        }

        @Override
        public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.shopping_list, parent, false);
            return new ShoppingListHolder(view);
        }

        @Override
        public void onBindViewHolder(ShoppingListHolder holder, int position) {
            holder.bindShoppingList(mShoppingLists.get(position));
        }

        @Override
        public int getItemCount() {
            return mShoppingLists.size();
        }
    }
}
