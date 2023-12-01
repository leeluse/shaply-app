package com.example.shaply_app.Lists;

import java.util.List;

public class ListItem {
    private String listId;
    private String listName; // 플레이리스트명
    private String selectedText; // 플레이리스트 옵션
    private List<String> tags; // 플레이리스트 태그
    private String userUid; // 사용자 UID

    public ListItem(String listId, String listName, String selectedText, List<String> tags, String userUid) {
        this.listId = listId;
        this.listName = listName;
        this.selectedText = selectedText;
        this.tags = tags;
        this.userUid = userUid;
    }

    public String getSelectedText() {
        return selectedText;
    }
    public String getListName() {
        return listName;
    }
    public List<String> getTags() {
        return tags;
    }
    public String getUserUid() { return userUid; }
    public String getListId() { return listId; }
}
