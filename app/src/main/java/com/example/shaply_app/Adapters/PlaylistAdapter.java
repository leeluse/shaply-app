package com.example.shaply_app.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shaply_app.Activities.MusicActivity;
import com.example.shaply_app.Lists.ListItem;
import com.example.shaply_app.R;

import java.util.List;
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<ListItem> itemList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 리사이클러 playlists_layout 설정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlists_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = itemList.get(position);
        String listName = item.getListName();
        String listId = item.getListId();
//        String selectedText = item.getSelectedText();
        List<String> tags = item.getTags();

        // 플레이리스트 이름 설정
        holder.textListName.setText(listName);

        // 플레이리스트 옵션 설정 (예: 선택된 텍스트)
        //holder.textSelectedText.setText(selectedText);

        // 플레이리스트 태그 설정
        StringBuilder tagsTextBuilder = new StringBuilder();
        for (String tag : tags) {
            tagsTextBuilder.append("#").append(tag); // 각 태그 사이에 #을 추가합니다.
        }
        String tagsText = tagsTextBuilder.toString().replaceAll("\\s+", "").replaceAll("#+", " #"); // #과 태그 사이의 공백 제거 및 각 태그 사이에 공백 추가
        tagsText = tagsText.trim(); // 문자열 양 끝의 공백 제거
        holder.textTag.setText(tagsText); // 텍스트 설정


        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            // 여기에 클릭 이벤트 처리 코드를 넣어줍니다.
            Log.d("ItemClick", "Item clicked!");
            // 새로운 액티비티에 데이터 전달을 위한 Intent 생성
            Intent intent = new Intent(v.getContext(), MusicActivity.class);

            intent.putExtra("playlistId", listId);
            // 다른 액티비티 시작
            v.getContext().startActivity(intent);
        });

    }

    public void setData(List<ListItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged(); // 데이터가 변경됐음을 알림
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public PlaylistAdapter(List<ListItem> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textListName;
//        TextView textSelectedText;
        TextView textTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰홀더의 텍스트뷰 초기화
            textListName = itemView.findViewById(R.id.tv_Listname);
//            textSelectedText = itemView.findViewById(R.id.tv_Option);
            textTag = itemView.findViewById(R.id.tv_Tag);
        }
    }
}
