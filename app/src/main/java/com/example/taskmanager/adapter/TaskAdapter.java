package com.example.taskmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Modle.Tasks;
import com.example.taskmanager.R;
import com.example.taskmanager.TaskDetailsActivity;
import com.example.taskmanager.databinding.RecycleItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    ArrayList<Tasks> data;
    Context context;

    public TaskAdapter(ArrayList<Tasks> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecycleItemBinding binding = RecycleItemBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        Picasso.get().load(data.get(i).getImage())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(myViewHolder.binding.image);

        myViewHolder.binding.taskNmV.setText("#" + data.get(i).getId() + "");
        myViewHolder.binding.Title.setText(data.get(i).getTitle());
        myViewHolder.binding.summaryTv.setText(data.get(i).getSummary());
        myViewHolder.binding.date.setText(data.get(i).getDate());
        myViewHolder.binding.time1.setText(data.get(i).getTime());
        myViewHolder.binding.recycleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra("id", data.get(i).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RecycleItemBinding binding;

        public MyViewHolder(@NonNull RecycleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
