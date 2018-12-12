package com.semestr2.bartek.androidzadanie1.basket;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class Basket extends Fragment {
    private OnFragmentInteractionListener mListener;
    private AppCompatActivity activity;
    private ArrayList<Book> items;
    private RecyclerViewAdapter adapter;
    DatabaseAccess instance;
    private View v;
    public Basket() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        items = new ArrayList<>();
        instance = DatabaseAccess.getInstance(getContext());
        items = instance.getBasketItems();
        View v = inflater.inflate(R.layout.fragment_basket, container, false);
        Button payment = v.findViewById(R.id.button2);
        payment.setOnClickListener(button-> Toast.makeText(activity, "Not in project scope", Toast.LENGTH_SHORT).show());


        RecyclerView rv = v.findViewById(R.id.basket_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Basket.RecyclerViewAdapter(items, mListener, this);
        rv.setAdapter(adapter);
        this.v = v;
        calculatePrice();
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            activity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<Basket.RecyclerViewAdapter.ViewHolder> {
        public RecyclerViewAdapter(ArrayList<Book> items, OnFragmentInteractionListener mListener, Basket basket) {
            mValues = items;
            this.mListener = mListener;
            context = basket;

        }
        private final List<Book> mValues;
        private final OnFragmentInteractionListener mListener;
        private final Basket context;


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.basket_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Book b = mValues.get(position);
            holder.image.setImageBitmap(b.getCoverAsBitmap());
            holder.title.setText(b.getTitle());
            holder.amount.setText("Amount: " + b.getAmount());
            holder.total.setText("Razem: "+ b.getPrice()*b.getAmount());

            holder.minus.setOnClickListener(minus -> {
                mValues.get(position).setAmount(mValues.get(position).getAmount()-1);
                instance.addToBasket(mValues.get(position));
                if(mValues.get(position).getAmount()==0){
                    mValues.remove(position);
                }
                adapter.notifyDataSetChanged();
                calculatePrice();
            });
            holder.plus.setOnClickListener(plus -> {
                mValues.get(position).setAmount(mValues.get(position).getAmount()+1);
                instance.addToBasket(mValues.get(position));
                adapter.notifyDataSetChanged();
                calculatePrice();
            });

            holder.delete.setOnClickListener(plus -> {
                mValues.get(position).setAmount(0);
                instance.addToBasket(mValues.get(position));
                mValues.remove(position);
                adapter.notifyDataSetChanged();
                calculatePrice();
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public double getTotal(){
            double sum = 0;
            for (Book mValue : mValues) {
                sum+=mValue.getAmount()*mValue.getPrice();
            }
            return sum;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final ImageView image;
            final ImageView delete;
            Book mItem;
            final ImageView plus;
            final ImageView minus;
            final TextView title;
            final TextView amount;
            final TextView total;

            ViewHolder(View view) {
                super(view);
                mView = view;
                image = view.findViewById(R.id.imageView);
                plus = view.findViewById(R.id.plus);
                minus = view.findViewById(R.id.minus);
                title = view.findViewById(R.id.title);
                amount = view.findViewById(R.id.amount);
                total = view.findViewById(R.id.total);
                delete = view.findViewById(R.id.delete);
            }

        }
    }

    private void calculatePrice() {
        double total = adapter.getTotal();
        TextView tv = v.findViewById(R.id.total);
        tv.setText(String.format(String.format(getContext().getResources().getString(R.string.price_for_book), total+"")));
    }
}
