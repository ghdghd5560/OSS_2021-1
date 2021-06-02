package project.oss_2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Cards card_item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        //image.setImageResource(R.mipmap.ic_launcher);
        switch (card_item.getprofileImageUrl()){
            case "default" :
                Glide.with(getContext()).load(R.mipmap.ic_launcher).into(image);
                break;

            default:
                Glide.with(image.getContext()).clear(image);
                Glide.with(getContext()).load(card_item.getprofileImageUrl()).into(image);
                break;

        }


        return convertView;
    }
}