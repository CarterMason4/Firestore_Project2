package com.example.firestoreproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // VIEWS
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;
    private Button saveData;
    private Button loadData;

    // CONSTANTES
    private static final String TAG = "MainActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference docRef = db.document("Notebook/My first note");
    private final CollectionReference collectionReference = db.collection("Notebook");
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TITLE = "title";
    private static final int NOTIFICATION_ID = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetEditTexts();
    }

    // TODO I think the notification goes here.

   @Override
   protected void onStart() {
       super.onStart();
       collectionReference.addSnapshotListener(this, (value, error) -> {
           if(error != null) {
               Log.e(TAG, getResources().getString(R.string.error_loading_notes));
               return;
           }

           assert value != null : getResources().getString(R.string.error_loading_notes);

           StringBuilder builder = new StringBuilder();

           for(QueryDocumentSnapshot snapshot : value) {
               Note note = snapshot.toObject(Note.class);
               builder.append("Titre : ")
                       .append(note.getTitle())
                       .append('\n')
                       .append("Description : ")
                       .append(note.getDescription())
                       .append("\n\n");
           }

           textViewData.setText(builder.toString());
           displayToast(getResources().getString(R.string.success_update_note));
           showUpdateNotification();
       });
   }

    private void initiateViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewData = findViewById(R.id.textViewData);
        saveData = findViewById(R.id.saveButton);
        loadData = findViewById(R.id.loadButton);

        // saveDatas();
        // loadDatas();
        addDatas();
        getAllNotes();
    }

    // Going to create an other method, called "Add data"
    // It's a bit different because it uses a CollectionReference instead of a DocumentReference
    private void addDatas() {
            saveData.setOnClickListener(view -> {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();

                if(!title.isEmpty() && !description.isEmpty()) {
                    Note note = new Note(title, description);

                    collectionReference.add(note)
                            .addOnSuccessListener(unused -> {
                                displayToast(getResources().getString(R.string.success_saving_note));
                                Log.e(TAG, getResources().getString(R.string.success_saving_note));
                            })
                            .addOnFailureListener(error -> Log.e(TAG, error.toString()));
                }
                resetEditTexts();
            });
    }

    private void getAllNotes() {
        loadData.setOnClickListener(view -> {
            StringBuilder text = new StringBuilder();
            collectionReference.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Note note = snapshot.toObject(Note.class);
                            text.append(note.getTitle())
                                    .append("\n")
                                    .append(note.getDescription())
                                    .append("\n\n");
                        }
                        textViewData.setText(text.toString());
                        Log.e(TAG, "Succcessfully retrieved all notes");
                    })
                    .addOnFailureListener(error -> Log.e(TAG, error.toString()));
        });
    }
    // TODO not actually the button that's going to be used
    // TODO Do not forget to rewire the correct button to this method.
    // TODO Otherwise the app is gonna crash.

    private void updateNote() {
        saveData.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();

            docRef.update(KEY_TITLE, title, KEY_DESCRIPTION, description)
                    .addOnSuccessListener(unused -> {
                        displayToast(getResources().getString(R.string.success_update_note));
                        Log.e(TAG, getResources().getString(R.string.success_update_note));
                    })
                    .addOnFailureListener(error -> Log.e(TAG, error.toString()));
        });
    }

    // TODO not actually the button that's going to be used
    // TODO Do not forget to rewire the correct button to this method.
    // TODO Otherwise the app is gonna crash.

    private void deleteNote() {
        saveData.setOnClickListener(view ->
                docRef.delete()
                .addOnSuccessListener(unused -> {
                    displayToast(getResources().getString(R.string.success_deletion_note));
                    Log.e(TAG, getResources().getString(R.string.success_deletion_note));
                }).addOnFailureListener(error -> Log.e(TAG, error.toString())));
    }

    // TODO not actually the button that's going to be used
    // TODO Do not forget to rewire the correct button to this method.
    // TODO Otherwise the app is gonna crash.

    private void deleteTitle() {
        saveData.setOnClickListener(view ->
                docRef.update(KEY_TITLE, FieldValue.delete())
                .addOnSuccessListener(unused -> {
                    displayToast(getResources().getString(R.string.success_delete_title));
                    Log.e(TAG, getResources().getString(R.string.success_delete_title));
                })
                .addOnFailureListener(error -> Log.e(TAG, error.toString())));
    }

    private void deleteDescription() {
        saveData.setOnClickListener(view ->
                docRef.update(KEY_DESCRIPTION, FieldValue.delete())
                .addOnSuccessListener(unused -> {
                    displayToast(getResources().getString(R.string.success_delete_description));
                    Log.e(TAG, getResources().getString(R.string.success_delete_description));
                }).addOnFailureListener(error -> Log.e(TAG, error.toString())));
    }


    // TODO Delete when necessary
    private void saveDatas() {
        saveData.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            if(!title.isEmpty() && !description.isEmpty()) {
                Map<String, Object> note = new HashMap<>();
                note.put(KEY_TITLE, title);
                note.put(KEY_DESCRIPTION, description);

                docRef.set(note)
                        .addOnSuccessListener(unused -> {
                            displayToast(getResources().getString(R.string.success_saving_note));
                            Log.e(TAG, getResources().getString(R.string.success_saving_note));
                        })
                        .addOnFailureListener(error -> Log.e(TAG, error.toString()));
                resetEditTexts();
            }
        });
    }


    // TODO Delete when necessary
    private void loadDatas() {
        loadData.setOnClickListener(view ->
                    docRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if(documentSnapshot.exists()) {
                                    Note note = documentSnapshot.toObject(Note.class);

                                    assert note != null : "There was a problem retrieving the notes";

                                    textViewData
                                            .setText(getResources()
                                            .getString(R.string.result, note.getTitle(), note.getDescription()));
                                } else {
                                    displayToast("Document doesn't contain files.");
                                }
                            }).addOnFailureListener(error -> Log.e(TAG, error.toString())));
    }

    private void showUpdateNotification() {
        String channelId = getResources().getString(R.string.notification_channel_id);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setTicker(getResources().getString(R.string.app_name))
                .setContentTitle(getResources().getString(R.string.received_update))
                .setWhen(System.currentTimeMillis())
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create the manager that is gonna handle the notification.
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a notification channel
        // in order to link Android to the app
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_id);
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    getResources().getString(R.string.notification_channel_id),
                    name,
                    importance);

            NotificationManager newNotificationManager = getSystemService(NotificationManager.class);
            newNotificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void resetEditTexts() {
        editTextTitle.setText("");
        editTextDescription.setText("");
    }

    private void displayToast(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}