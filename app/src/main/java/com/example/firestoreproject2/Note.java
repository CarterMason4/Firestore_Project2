package com.example.firestoreproject2;

import androidx.annotation.NonNull;

public class Note {

    private String title;
    private String description;

    public Note() {
        // no-arg constructor needed by Firebase
    }

    public Note(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        if(title == null || title.isEmpty()) {
            this.title = "";
        } else {
            this.title = title;
        }
    }


    public void setDescription(String description) {
        if(description == null || description.isEmpty()) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    @Override
    @NonNull
    public String toString() {
        return "Title : " + title
                + '\n'
                + "Description : " + description;
    }
}
