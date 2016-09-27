package io.github.osdlabs.osmusicplayer.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.osdlabs.osmusicplayer.Adapters.MainSongsAdapter;
import io.github.osdlabs.osmusicplayer.ItemFormats.MainSongsItemFormat;
import io.github.osdlabs.osmusicplayer.R;

/**
 * Created by vikramaditya on 1/9/16.
 */

public class MainSongsFragment extends Fragment {

    RecyclerView musicListRecyclerView;
    MainSongsAdapter adapter;
    List<MainSongsItemFormat> mainSongsItemFormatList = new ArrayList<>();

    private static final int REQUEST_WRITE_STORAGE = 21;
    int permissionCheck;
    int currentApi;


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

        musicListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_songs_rv);
        permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        currentApi = Build.VERSION.SDK_INT;


        adapter = new MainSongsAdapter(getContext(), mainSongsItemFormatList);
        musicListRecyclerView.setHasFixedSize(true);
        musicListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        musicListRecyclerView.setAdapter(adapter);

        if (currentApi < Build.VERSION_CODES.M) {
            populateList();
            adapter.notifyDataSetChanged();
        } else
            getPermission();


        return view;
    }

    public void getPermission() {
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Need storage permission")
                    .setMessage("We need to storage permission to read your storage for songs.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);


                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else{
            populateList();
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateList();
                    adapter.notifyDataSetChanged();


                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        }
    }

    private void populateList() {
        if (mainSongsItemFormatList == null) mainSongsItemFormatList = new ArrayList<>(500);
        else mainSongsItemFormatList.clear();

        ContentResolver cr = getActivity().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count;

        if (cur != null) {
            count = cur.getCount();

            if (count > 0) {
                while (cur.moveToNext()) {
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    // Add code to get more column here

                    // Save to your list here
                    mainSongsItemFormatList.add(new MainSongsItemFormat(data));
                }

            }
        }

        if (cur != null) {
            cur.close();
        }
        adapter.notifyDataSetChanged();

    }


}
