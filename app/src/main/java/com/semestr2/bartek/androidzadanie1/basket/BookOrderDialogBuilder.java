package com.semestr2.bartek.androidzadanie1.basket;


import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.books.Book;
import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

public class BookOrderDialogBuilder {
    public static void showOrderDialog(Context ctx, Book mBook, OnFragmentInteractionListener mListener, ViewGroup parent) {
        DatabaseAccess instance = DatabaseAccess.getInstance(ctx);
        final Book b = instance.getBasketItem(mBook);
        Dialog dialog = new Dialog(ctx);
        View d = dialog.getLayoutInflater().inflate(R.layout.book_order, parent, false);
        Button add = d.findViewById(R.id.add_to_cart);
        Button buy = d.findViewById(R.id.buy_now);
        TextView t = d.findViewById(R.id.book_order_dialog_title);
        t.setText(mBook.getTitle());
        TextView p = d.findViewById(R.id.price);
        p.setText(String.format(ctx.getResources().getString(R.string.price_for_book), mBook.getPriceAsString()));
        TextView total = d.findViewById(R.id.total);
        EditText input = d.findViewById(R.id.editText);
        total.setText(String.format(ctx.getResources().getString(R.string.price_for_book), b.getAmount() * mBook.getPrice() + ""));
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0 && Integer.parseInt(s.toString())>0) {
                    int amount = Integer.parseInt(s.toString());
                    b.setAmount(amount);
                    add.setClickable(true);
                    add.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                    add.setOnClickListener(v -> {
                        instance.addToBasket(b);
                        dialog.dismiss();
                        Toast.makeText(ctx, "Dodano " + b.getTitle() + " x " + b.getAmount() + "do koszyka", Toast.LENGTH_SHORT);
                    });
                    buy.setClickable(true);
                    buy.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                    total.setText(String.format(ctx.getResources().getString(R.string.price_for_book), amount * b.getPrice() + ""));
                }else{
                    b.setAmount(0);
                    total.setText(String.format(ctx.getResources().getString(R.string.price_for_book), 0 * b.getPrice() + ""));
                    add.setClickable(false);
                    add.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
                    buy.setClickable(false);
                    buy.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
                }
            }
        });
        Button close = d.findViewById(R.id.close_dialog);
        buy.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
        add.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
        close.setBackgroundColor(ctx.getResources().getColor(R.color.red));
        close.setOnClickListener((view)-> dialog.dismiss());
        buy.setOnClickListener((v1 -> mListener.onAddToCartListener(b, true)));
        add.setOnClickListener((v1 -> mListener.onAddToCartListener(b, false)));
        dialog.setContentView(d);
        dialog.show();
        input.setText(b.getAmount()+"");
    }

}
