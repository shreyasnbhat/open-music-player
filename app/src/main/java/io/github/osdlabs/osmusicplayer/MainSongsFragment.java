package io.github.osdlabs.osmusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vikramaditya on 1/9/16.
 */

public class MainSongsFragment extends Fragment {

    RecyclerView recyclerView;
    MainSongsAdapter adapter;
    List<MainSongsItemFormat> mainSongsItemFormatList = new ArrayList<>();

    public MainSongsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_songs, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_songs_rv);

        populateList();
        adapter = new MainSongsAdapter(getContext(), mainSongsItemFormatList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    private void populateList() {
        try {
            mainSongsItemFormatList = new AsyncTask<Void, Void, List<MainSongsItemFormat>>() {
                List<MainSongsItemFormat> mList = new ArrayList<>(500);

                @Override
                protected List<MainSongsItemFormat> doInBackground(Void... params) {
                    ContentResolver cr = getActivity().getContentResolver();

                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
                    String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
                    Cursor cur = cr.query(uri, null, selection, null, sortOrder);
                    int count = 0;

                    if (cur != null) {
                        count = cur.getCount();

                        if (count > 0) {
                            while (cur.moveToNext()) {
                                String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                                // Add code to get more column here

                                // Save to your list here
                                mList.add(new MainSongsItemFormat(data));
                            }

                        }
                    }

                    if (cur != null) {
                        cur.close();
                    }

                    return mList;
                }
            }.execute().get();
            Log.e("TAG", "size is " + mainSongsItemFormatList.size());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
