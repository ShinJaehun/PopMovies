package com.shinjaehun.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayList<PopMovie> movies = new ArrayList<PopMovie>();

    PopMovieAdapter movieAdapter;
    private String mSortOrder;
    private SharedPreferences mPrefs;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    Boolean allowUpdate = true;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortOrder = mPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_key_default));
        Log.d(LOG_TAG, "mSortOrder onCreateView: " + mSortOrder);
        new FetchMovieTask().execute(mSortOrder);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.prefChanged) {
            //mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mSortOrder = mPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_key_default));
            Log.d(LOG_TAG, "mSortOrder onResume: " + mSortOrder);
            new FetchMovieTask().execute(mSortOrder);
            movieAdapter.clear();
            movieAdapter.notifyDataSetChanged();
            MainActivity.prefChanged = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //mSortOrder = mPrefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_key_default));
        //Log.d(LOG_TAG, "mSortOrder onCreateView: " + mSortOrder.toString());
        //new FetchMovieTask().execute(mSortOrder);

        GridView gridView = (GridView)rootView.findViewById(R.id.movies_list_gridView);
        movieAdapter = new PopMovieAdapter(getActivity(), movies);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, movies.get(position));
                startActivity(i);
            }
        });

        return rootView;

    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        @Override
        protected Void doInBackground(String... params) {

            Log.d(LOG_TAG, "Requesting movies List... ");
            Uri builtUri = Uri.parse(getString(R.string.BASE_URL))
                    .buildUpon()
                    .appendQueryParameter(getString(R.string.API_KEY_PARAM), getString(R.string.API_KEY))
                    .appendQueryParameter(getString(R.string.LANGUAGE_PARAM), getString(R.string.LANGUAGE_EN_PARAM))
                    //.appendQueryParameter(getString(R.string.SORT_PARAM), getString(R.string.POPULARITY_PARAM)
                    .appendQueryParameter(getString(R.string.SORT_PARAM), params[0]
                    +  getString(R.string.SORT_DESC_PARAM))
                    .build();

            Log.d(LOG_TAG, "URI "+ builtUri.toString());

            try {
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    moviesJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    moviesJsonStr = null;
                }
                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (reader != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error ", e);
                    }
                }
            }
        Log.v(LOG_TAG, "Movies Json String : " + moviesJsonStr);

        extractFromJSON(moviesJsonStr);

        moviesJsonStr = null;

        return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            movieAdapter.notifyDataSetChanged();

        }
    }

    private Void extractFromJSON(String jsonStr) {

        try {
            JSONObject moviesJson = new JSONObject(jsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject jo = moviesArray.getJSONObject(i);

                movies.add(new PopMovie(
                        jo.getString(getString(R.string.ORIGIONAL_TITLE_PARAM)),
                        jo.getString(getString(R.string.POSTER_PATH_PARAM)),
                        jo.getString(getString(R.string.BACKDROP_PATH_PARAM)),
                        jo.getString(getString(R.string.OVERVIEW_PARAM)),
                        jo.getString(getString(R.string.RELEASE_DATE_PARAM)),
                        jo.getString(getString(R.string.RATING_PARAM)),
                        jo.getString(getString(R.string.POPULARITY_PARAM))));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Title in movies : " + movies.get(0).title);
        return null;
    }
}
