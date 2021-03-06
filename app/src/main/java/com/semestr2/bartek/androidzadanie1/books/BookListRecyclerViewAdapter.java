package com.semestr2.bartek.androidzadanie1.books;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.List;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnFragmentInteractionListener mListener;
    private final BookFragment context;
    private final BookListRecyclerViewAdapter self;

    BookListRecyclerViewAdapter(List<Book> items, OnFragmentInteractionListener listener, BookFragment fragment) {
        mValues = items;
        mListener = listener;
        context = fragment;
        self = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.pager.setAdapter(new BookGalleryAdapter(context, mValues.get(position), holder.pager));
        holder.mCart.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
        holder.delete.setOnClickListener(view -> {
            mValues.get(position).setAmount(0);
            DatabaseAccess.getInstance(context.getContext()).addToBasket(mValues.get(position));
            //mValues.remove(position);
            self.notifyDataSetChanged();
        });
        if(holder.mItem.getAmount()!=0) {
            holder.amount.setText(String.format(" : %d", holder.mItem.getAmount()));
            holder.mCart.setColorFilter(android.R.color.holo_green_dark);
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setClickable(true);
        }
        else{
            holder.mCart.setColorFilter(android.R.color.black);
            holder.amount.setText("");
            holder.delete.setVisibility(View.INVISIBLE);
            holder.delete.setClickable(false);
        }
        holder.liked.setChecked(mValues.get(position).isLiked());
        holder.liked.setOnCheckedChangeListener((compoundButton, b) -> {
            DatabaseAccess instance = DatabaseAccess.getInstance(context.getContext());
            instance.setLike(mValues.get(position), b, PreferenceManager.getDefaultSharedPreferences(context.getContext()).getString("pref_userName", null));
        });
        holder.price.setText("Cena: " + mValues.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ViewPager pager;
        Book mItem;
        final ImageView mCart;
        final TextView price;
        final TextView amount;
        final CheckBox liked;
        final ImageView delete;

        ViewHolder(View view) {
            super(view);
            mView = view;
            pager = view.findViewById(R.id.book_item_pager);
            mCart = view.findViewById(R.id.add);
            price = view.findViewById(R.id.price);
            amount = view.findViewById(R.id.amount);
            liked = view.findViewById(R.id.favourite2);
            delete = view.findViewById(R.id.delete);
        }

    }
}
