package com.example.shaply_app;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<ListItem> itemList;

    public PlaylistAdapter(List<ListItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlists_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = itemList.get(position);

        // 플레이리스트 이름 설정
        holder.textListName.setText(item.getListName());

        // 플레이리스트 옵션 설정 (예: 선택된 텍스트)
        holder.textSelectedText.setText(item.getSelectedText());

        // 플레이리스트 태그 설정
        List<String> tags = item.getTags();
        StringBuilder tagsTextBuilder = new StringBuilder();
        for (String tag : tags) {
            tagsTextBuilder.append("#").append(tag).append(" ");
        }
        String tagsText = tagsTextBuilder.toString();
        holder.textOption.setText(tagsText); // 텍스트 설정
    }

    // setData 메서드 추가
    public void setData(List<ListItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textListName;
        TextView textSelectedText;
        TextView textOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰홀더의 텍스트뷰 초기화
            textListName = itemView.findViewById(R.id.tv_Listname);
            textSelectedText = itemView.findViewById(R.id.tv_Tag);
            textOption = itemView.findViewById(R.id.tv_Option);
        }
    }
}