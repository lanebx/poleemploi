package com.example.projetpoleemploi.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetpoleemploi.R;
import com.example.projetpoleemploi.ui.main.data.webservice.database.JobResponse;
import com.example.projetpoleemploi.ui.main.database.JobEntity;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<JobResponse.Resultat> jobList;

    public JobAdapter(List<JobResponse.Resultat> jobList) {
        this.jobList = jobList;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<JobResponse.Resultat> jobList, View v);
    }

    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.job_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.job_item_mirrored, parent, false);
        }
        return new JobViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobResponse.Resultat job = jobList.get(position);
        holder.jobTitle.setText(job.intitule);
        holder.jobLocation.setText(job.lieuTravail.libelle.substring(5));
        if (job.entreprise.logo != null) {
            Picasso.get().load(job.entreprise.logo).into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.default_vacancy_image);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(holder, position);
            }
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, jobList, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobLocation;
        ImageView itemImage;

        public JobViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == 0) {
                jobTitle = itemView.findViewById(R.id.item_title);
                jobLocation = itemView.findViewById(R.id.item_detail);
                itemImage = itemView.findViewById(R.id.item_image);
            } else {
                jobTitle = itemView.findViewById(R.id.item_title);
                jobLocation = itemView.findViewById(R.id.item_detail);
                itemImage = itemView.findViewById(R.id.item_image);
            }
        }
    }

    public void updateJobs(List<JobResponse.Resultat> newJobList) {
        jobList.clear();
        jobList.addAll(newJobList);
        notifyDataSetChanged();
    }

    public JobEntity getJobEntityAtPosition(int position) {
        JobResponse.Resultat jobResult = jobList.get(position);
        return new JobEntity(
                jobResult.id,
                jobResult.intitule,
                jobResult.description,
                jobResult.lieuTravail.libelle.substring(5),
                jobResult.entreprise.logo,
                jobResult.typeContrat
        );
    }
}

