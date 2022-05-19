package com.example.firestoreproject2;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String documentId;
    private String title;
    private String description;
    private int priority;

    public Note() { /* no-arg constructor needed by Firebase */ }

    public Note(String documentId, String title, String description, int priority) {
        this(title, description, priority);
        setDocumentId(documentId);
    }

    public Note(String title, String description, int priority) {
        this(title, description);
        setPriority(priority);
    }

    public Note(String title, String description) {
        setTitle(title);
        setDescription(description);
    }
    public void setDocumentId(String documentId) {
        if(documentId == null || documentId.isEmpty()) {
            this.documentId = "";
        } else {
            this.documentId = documentId;
        }
    }

   @Exclude
   public String getDocumentId() {
        return documentId;
   }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title == null || title.isEmpty()) {
            this.title = "";
        } else {
            this.title = title;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null || description.isEmpty()) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }


    @Override
    @NonNull
    public String toString() {
        return "ID : " + documentId
                + '\n'
                + "Title : " + title
                + '\n'
                + "Description : " + description
                + '\n'
                + "Priority : " + priority
                + "\n\n";
    }
}
