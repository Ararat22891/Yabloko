package com.tatdep.yabloko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.tatdep.yabloko.R;
import com.tatdep.yabloko.cods.NewsDataAddList;

import java.util.ArrayList;

public class newsAdapterListView extends ArrayAdapter<NewsDataAddList> {

    public newsAdapterListView(@NonNull Context context, ArrayList<NewsDataAddList> arrayList){
        super(context, 0, arrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.novost, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        NewsDataAddList currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView numbersImage = currentItemView.findViewById(R.id.ris);
        assert currentNumberPosition != null;

        Picasso.get().load(currentNumberPosition.getAvatarsPhoto())
                .into(numbersImage);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.users_if);
        textView1.setText(currentNumberPosition.getNameOfUser());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.posts_text);
        textView2.setText(currentNumberPosition.getTextOfUserPost());

        TextView textView3 = currentItemView.findViewById(R.id.time);
        textView3.setText(currentNumberPosition.getDate());

        CheckBox ch1 = currentItemView.findViewById(R.id.lovee);
        ch1.setText(currentNumberPosition.getLikesQuantity());

        // then return the recyclable view
        return currentItemView;
    }
}
