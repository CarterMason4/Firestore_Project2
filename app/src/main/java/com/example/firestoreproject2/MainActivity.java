package com.example.firestoreproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // VIEWS
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private Button saveData;
    private Button loadData;

    // CONSTANTES
    private static final String TAG = "MainActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference docRef = db.document("Notebook/My first note");
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
    }

    private void initiateViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        saveData = findViewById(R.id.saveButton);
        loadData = findViewById(R.id.loadButton);

        saveDatas();
        loadDatas();
    }

    private void saveDatas() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if(!title.isEmpty() && !description.isEmpty()) {
            saveData.setOnClickListener(view -> {
                Map<String, Object> note = new HashMap<>();
                note.put(KEY_TITLE, title);
                note.put(KEY_DESCRIPTION, description);

                docRef.set(note)
                        .addOnSuccessListener(unused -> {
                            displayToast(getResources().getString(R.string.success_saving_note));
                            Log.e(TAG, "Note successfully saved !");
                        })
                        .addOnFailureListener(error -> Log.e(TAG, error.toString()));

                resetEditTexts();
            });
        }
    }

    private void loadDatas() {}

    private void resetEditTexts() {
        editTextTitle.setText("");
        editTextDescription.setText("");
    }

    private void displayToast(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}