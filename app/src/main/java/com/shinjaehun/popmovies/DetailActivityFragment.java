package com.shinjaehun.popmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    PopMovie movie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movie = (PopMovie)intent.getSerializableExtra(Intent.EXTRA_TEXT);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView image_backdrop = (ImageView)rootView.findViewById(R.id.imageView_backdrop);
        ImageView image_poster = (ImageView)rootView.findViewById(R.id.imageView_poster);
        TextView text_title = (TextView)rootView.findViewById(R.id.textView_movie_title);
        TextView text_release_date = (TextView)rootView.findViewById(R.id.textView_release_date);
        TextView text_rating = (TextView)rootView.findViewById(R.id.textView_rating);
        TextView text_overview = (TextView)rootView.findViewById(R.id.textView_overview);

        Picasso.with(getActivity())
                .load(getString(R.string.BASE_URL_DROPBACK) + movie.backdrop_path).into(image_backdrop);
        Picasso.with(getActivity())
                .load(getString(R.string.BASE_URL_POSTER) + movie.poster_Location).into(image_poster);
        text_title.setText(movie.title);
        text_release_date.setText(movie.release_Date);
        String roundedRate = String.valueOf(Math.round(Float.parseFloat(movie.rating)));
        text_rating.setText(roundedRate + "/10");
        text_overview.setText(movie.overview);

        return rootView;

        //return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}
