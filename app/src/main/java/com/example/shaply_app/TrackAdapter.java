package com.example.shaply_app;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<Track> trackList;

    public TrackAdapter(List<Track> trackList) {
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.trackName.setText(track.getTrackName());
        holder.artistName.setText(track.getArtistName());
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView trackName;
        TextView artistName;
        List<Track> trackList;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.text_view_track_name);
            artistName = itemView.findViewById(R.id.text_view_artist_name);

            // 아이템 클릭 이벤트 처리
            itemView.setOnClickListener(v -> {
                Track track = trackList.get(getAdapterPosition());

                // 클릭된 아이템의 데이터 가져오기
                String selectedTrackName = track.getTrackName();
                String selectedArtistName = track.getArtistName();

                // 새로운 액티비티에 데이터 전달을 위한 Intent 생성
                Intent intent = new Intent(v.getContext(), MusicActivity.class);

                // 데이터 첨부
                intent.putExtra("trackName", selectedTrackName);
                intent.putExtra("artistName", selectedArtistName);

                // 다른 액티비티 시작
                v.getContext().startActivity(intent);
            });
        }
    }
}