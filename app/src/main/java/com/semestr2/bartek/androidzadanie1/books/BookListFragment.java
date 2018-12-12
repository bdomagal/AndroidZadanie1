package com.semestr2.bartek.androidzadanie1.books;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semestr2.bartek.androidzadanie1.R;
import com.semestr2.bartek.androidzadanie1.database.DatabaseAccess;
import com.semestr2.bartek.androidzadanie1.fragments.OnFragmentInteractionListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class BookListFragment extends BookFragment {

    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Book> bookList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView =  view.findViewById(R.id.list);
            //Log.e("REC", String.valueOf(recyclerView));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("display_galeries_in_list", false)) {
                recyclerView.setAdapter(new BookListRecyclerViewAdapter(bookList, mListener, this));
            }
            else{
                recyclerView.setAdapter(new SmallBookListRecyclerViewAdapter(bookList, mListener, this));
            }
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        recyclerView.smoothScrollToPosition(((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                    }
                }
            });

        }
        if(bookList.isEmpty()){
            view.findViewById(R.id.list).setVisibility(View.GONE);
            //view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        }else{
            view.findViewById(R.id.list).setVisibility(View.VISIBLE);
            //view.findViewById(R.id.empty).setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setData(ArrayList<Book> filteredBooks) {
        bookList = filteredBooks;
    }

    @Override
    public void getDetails(Book book) {
        mListener.onDisplayDetailsListener(book);
    }

}
