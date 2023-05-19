package com.example.projetpoleemploi.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetpoleemploi.R;
import com.example.projetpoleemploi.ui.main.data.webservice.database.JobResponse;
import com.example.projetpoleemploi.ui.main.database.JobEntity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FavorisAdapter extends RecyclerView.Adapter<FavorisAdapter.FavoriViewHolder> {
    private List<JobEntity> favorisList;

    public FavorisAdapter(List<JobEntity> favorisList) {
        this.favorisList = favorisList;
    }

    @NonNull
    @Override
    public FavoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.favoris_item, parent, false);
        return new FavoriViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder holder, int position);
    }

    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriViewHolder holder, int position) {
        JobEntity favori = favorisList.get(position);
        holder.favTitle.setText(favori.title);
        holder.favLocation.setText(favori.location);
        if (favori.image == null) {
            holder.favImage.setImageResource(R.drawable.default_vacancy_image);
        } else {
            Picasso.get().load(favori.image).into(holder.favImage);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(holder, position);
            }
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }

            JobEntity jobEntity = favorisList.get(position); // В FavorisAdapter

            ItemDetailFragment itemDetailFragment = ItemDetailFragment.newInstance(jobEntity);

            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_replace_container, itemDetailFragment)
                    .addToBackStack(null)
                    .commit();

            // Hide elements that should not be visible
            View recyclerView = ((AppCompatActivity) v.getContext()).findViewById(R.id.view_pager);
            View searchView = ((AppCompatActivity) v.getContext()).findViewById(R.id.searchView);
            if (recyclerView != null && searchView != null) {
                recyclerView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorisList.size();
    }

    public static class FavoriViewHolder extends RecyclerView.ViewHolder {
        TextView favTitle, favLocation;
        ImageView favImage;

        public FavoriViewHolder(@NonNull View itemView) {
            super(itemView);
            favTitle = itemView.findViewById(R.id.item_title);
            favLocation = itemView.findViewById(R.id.item_location);
            favImage = itemView.findViewById(R.id.item_image);
        }
    }
}
