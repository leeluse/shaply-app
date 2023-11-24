package com.example.shaply_app;

import java.util.List;

public class ListItem {
    private String listName;
    private String selectedText;
    private List<String> tags;

    public ListItem(String listName, String selectedText, List<String> tags) {
        this.listName = listName;
        this.selectedText = selectedText;
        this.tags = tags;
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
}
