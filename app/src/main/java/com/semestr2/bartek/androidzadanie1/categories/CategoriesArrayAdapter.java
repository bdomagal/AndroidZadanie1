package com.semestr2.bartek.androidzadanie1.categories;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.semestr2.bartek.androidzadanie1.R;

import java.util.ArrayList;
import java.util.Collection;

public class CategoriesArrayAdapter extends ArrayAdapter<Category> {

    private final Activity context;
    private final ArrayList<Category> objects;

    public CategoriesArrayAdapter(@NonNull Activity context, @NonNull ArrayList<Category> objects) {
        super(context, R.layout.categories_drawer_item, objects);

        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        if(rowView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.categories_drawer_item, parent, false);
        }

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.genre);
        TextView groupTextField = rowView.findViewById(R.id.group);
        ImageView imageView = rowView.findViewById(R.id.genre_icon);
        CheckBox checkBox = rowView.findViewById(R.id.genre_select);

        //this code sets the values of the objects to values from the arrays
        Category item = objects.get(position);
        nameTextField.setText(item.getName());
        groupTextField.setText(item.getCategoryGroup());
        checkBox.setChecked(item.isChecked());
        if(item.getImage()!=null){
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        }

        return rowView;
    }

    public Collection<Category> getData() {
        return objects;
    }

    public static class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
            CheckBox cb = view.findViewById(R.id.genre_select);
            Category c = (Category) adapterView.getItemAtPosition(i);
            c.toggle();
            cb.toggle();
        }
    }
}
