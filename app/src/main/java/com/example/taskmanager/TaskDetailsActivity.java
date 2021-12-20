package com.example.taskmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.taskmanager.Modle.Tasks;
import com.example.taskmanager.databinding.ActivityTaskDetailsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class TaskDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    Tasks tasks = new Tasks();
    Bundle bundle;
    TextView taskNumber;
    ProgressDialog progressDialog;

    private String id;
    private Menu menu;

    private ActivityTaskDetailsBinding binding;

    GoogleMap googleMap;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bundle = getIntent().getExtras();
        id = bundle.getString("id");



        db.collection("tasks").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    tasks = value.toObject(Tasks.class);
                    Picasso.get().load(tasks.getImage())
                            .error(R.drawable.ic_launcher_background)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(binding.imageD);

                    String taskN = tasks.getId();
                    binding.tasknumber.setText(taskN + "");

                    String taskTitle = tasks.getTitle();
                    binding.TVtaskTitle.setText(taskTitle);
                    String taskd = tasks.getDescription();
                    binding.taskdes.setText(taskd);
                    String taskDa = tasks.getDate();
                    binding.dateTv.setText(taskDa);
                    String taskT = tasks.getTime();
                    binding.timeTv.setText(taskT);

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setTitle(taskTitle);


                }

            }
        });

        binding.mapView.getMapAsync(TaskDetailsActivity.this);
        binding.mapView.onCreate(savedInstanceState);


    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
        db.collection("tasks").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    tasks = value.toObject(Tasks.class);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_display, menu);
        this.menu = menu;
        if (tasks.getFavorite() == 0) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_24));
        } else if (tasks.getFavorite() == 1) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_fav_24));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favoriteButton) {
            favorite();
            return true;
        }
        if (id == R.id.action_delete) {
            delete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favorite() {
        if (tasks.getFavorite() == 0) {
            db.collection("tasks")
                    .document(tasks.getId())
                    .update("favorite", 1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(getApplicationContext(), "Add to favorite", Toast.LENGTH_SHORT).show();
                            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_fav_24));
                        }
                    });
        } else if (tasks.getFavorite() == 1) {
            db.collection("tasks")
                    .document(tasks.getId())
                    .update("favorite", 0)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(getApplicationContext(), "Remove from favorite", Toast.LENGTH_SHORT).show();
                            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_star_24));

                        }
                    });
        }
    }

    public void delete() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(TaskDetailsActivity.this.getText(R.string.deleted));
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showDialog();

                db.collection("tasks")
                        .document(tasks.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                                hideDialog();
                                TaskDetailsActivity.this.finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                hideDialog();
                            }
                        });
            }

        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alert.show();

    }

    public void showDialog() {
        progressDialog = new ProgressDialog(this, R.style.color);
        progressDialog.setMessage(TaskDetailsActivity.this.getString(R.string.delete));
        progressDialog.show();

    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        onBackPressed();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap = googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(tasks.getLatitude()),Double.parseDouble(tasks.getLongitude()));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("my Position");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);

        CameraUpdate  cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        binding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }
}
