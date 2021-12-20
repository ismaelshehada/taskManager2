package com.example.taskmanager.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmanager.Modle.Tasks;
import com.example.taskmanager.R;
import com.example.taskmanager.adapter.TaskAdapter;
import com.example.taskmanager.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<Tasks> data;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        showDialog();
        getData();


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    public void getData() {


        firebaseFirestore.collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    hideDialog();
                }else {
                    hideDialog();
                    data = new ArrayList<>();
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED ){
                            Tasks tasks = doc.getDocument().toObject(Tasks.class);
                            tasks.setId(doc.getDocument().getId());
                            data.add(tasks);
                        }
                    }
                    TaskAdapter taskAdapter = new TaskAdapter(data,getActivity());
                    binding.recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.recycle.setAdapter(taskAdapter);
                }
            }
        });





    }

    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(HomeFragment.this.getText(R.string.Loading));
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
