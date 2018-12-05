package com.semestr2.bartek.androidzadanie1.books;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.List;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<BookListRecyclerViewAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnFragmentInteractionListener mListener;
    private final BookFragment context;

    BookListRecyclerViewAdapter(List<Book> items, OnFragmentInteractionListener listener, BookFragment fragment) {
        mValues = items;
        mListener = listener;
        context = fragment;
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
        if(holder.mItem.getAmount()!=0)
        holder.amount.setText(String.format("Ilość w koszyku : %d", holder.mItem.getAmount()));
        else{
            holder.amount.setText("");
        }
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
        final ImageButton mCart;
        final TextView price;
        final TextView amount;

        ViewHolder(View view) {
            super(view);
            mView = view;
            pager = view.findViewById(R.id.book_item_pager);
            mCart = view.findViewById(R.id.add);
            price = view.findViewById(R.id.price);
            amount = view.findViewById(R.id.amount);
        }

    }
}
