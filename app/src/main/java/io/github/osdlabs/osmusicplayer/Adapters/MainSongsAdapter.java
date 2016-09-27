package io.github.osdlabs.osmusicplayer.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.osdlabs.osmusicplayer.ItemFormats.MainSongsItemFormat;
import io.github.osdlabs.osmusicplayer.R;

/**
 * Created by vikramaditya on 1/9/16.
 */

public class MainSongsAdapter extends RecyclerView.Adapter<MainSongsAdapter.ViewHolder> {

    Context context;
    List<MainSongsItemFormat> list;

    public MainSongsAdapter(Context context, List<MainSongsItemFormat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.format_fragment_main_songs_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.format_fragment_main_songs_rv_item_title);
        }
    }
}
