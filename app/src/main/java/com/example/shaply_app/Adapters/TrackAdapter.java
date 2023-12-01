package com.example.shaply_app.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaply_app.Lists.MusicList;
import com.example.shaply_app.Lists.Track;
import com.example.shaply_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private final String playlistId; // playlistId를 저장할 변수
    private List<Track> trackList;
    private DatabaseReference playlistRef; // Firebase 데이터베이스 참조

    public TrackAdapter(List<Track> trackList, String playlistId) {
        this.trackList = trackList;
        this.playlistId = playlistId;

        // Firebase 데이터베이스 참조 가져오기
        playlistRef = FirebaseDatabase.getInstance().getReference().child("lists").child(playlistId).child("Track");
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 리사이클러 add_track_layout 설정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_track_layout, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.trackName.setText(track.getTrackName());
        holder.artistName.setText(track.getArtistName());

        holder.itemView.setOnClickListener(v -> {
            Track clickedTrack = trackList.get(position);
            String trackName = clickedTrack.getTrackName();
            String artistName = clickedTrack.getArtistName();

            // Firebase에 데이터 추가
            MusicList musicList = new MusicList(trackName, artistName);

            playlistRef.push().setValue(musicList)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 데이터 추가 성공 시에 토스트 메시지 출력
                            Toast.makeText(v.getContext(), "추가 완료", Toast.LENGTH_SHORT).show();
                        } else {
                            // 데이터 추가 실패 시에 토스트 메시지 출력
                            Toast.makeText(v.getContext(), "추가 실패", Toast.LENGTH_SHORT).show();
                        }
                    });

            Log.d("TrackAdapter", "Clicked track: " + trackName + " - " + artistName);
        });
    }


    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView trackName;
        TextView artistName;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.tv_track_name);
            artistName = itemView.findViewById(R.id.tv_artist_name);
        }
    }
}
