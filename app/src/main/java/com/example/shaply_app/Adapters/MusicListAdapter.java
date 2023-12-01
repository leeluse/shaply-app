package com.example.shaply_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaply_app.Lists.MusicList;
import com.example.shaply_app.R;

import java.util.List;
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {
    private List<MusicList> musicList;

    public MusicListAdapter(List<MusicList> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 리사이클러 track_layout 설정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_layout, parent, false);
        return new MusicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListViewHolder holder, int position) {
        MusicList music = musicList.get(position);
        holder.trackName.setText(music.getTrackName());
        holder.artistName.setText(music.getArtistName());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public static class MusicListViewHolder extends RecyclerView.ViewHolder {
        TextView trackName;
        TextView artistName;

        public MusicListViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.tv_track_name);
            artistName = itemView.findViewById(R.id.tv_artist_name);
        }
    }
}
