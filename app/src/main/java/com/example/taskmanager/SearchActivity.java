package com.example.taskmanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmanager.Modle.Tasks;
import com.example.taskmanager.adapter.TaskAdapter;
import com.example.taskmanager.databinding.ActivitySearchBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Tasks> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                getData(newText);
                return false;
            }
        });
    }

    public void getData(String query){

        db.collection("tasks").whereEqualTo("title",query).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }else {

                    data = new ArrayList<>();
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED ){
                            Tasks tasks = doc.getDocument().toObject(Tasks.class);
                            tasks.setId(doc.getDocument().getId());
                            data.add(tasks);
                        }
                    }
                    TaskAdapter taskAdapter = new TaskAdapter(data,getBaseContext());
                    binding.SearchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    binding.SearchRecycler.setAdapter(taskAdapter);
                }
            }
        });
    }

}