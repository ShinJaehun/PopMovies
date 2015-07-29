package com.shinjaehun.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2015-07-27.
 */
public class PopMovieAdapter extends ArrayAdapter<PopMovie> {
    Context context;
    LayoutInflater inflater;
    public final String LOG_TAG = PopMovieAdapter.class.getSimpleName();

    public PopMovieAdapter(Context context, List<PopMovie> objects) {
        super(context, R.layout.list_item_movie_image, objects);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopMovie movie = getItem(position);

        if (convertView == null) {
            convertView =inflater.inflate(R.layout.list_item_movie_image, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_image);
        Picasso.with(context)
                .load(context.getString(R.string.BASE_URL_POSTER) + movie.poster_Location)
                .into(imageView);

        return convertView;
    }
}
