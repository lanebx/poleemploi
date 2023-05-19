package com.example.projetpoleemploi.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetpoleemploi.R;
import com.example.projetpoleemploi.ui.main.database.JobEntity;
import com.example.projetpoleemploi.ui.main.database.JobRepository;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class ItemDetailFragment extends Fragment {
    private JobEntity jobEntity;
    private ImageView itemImage;
    private TextView itemTitle;
    private TextView itemLocation;
    private TextView itemDescription;
    private Button backButton;
    private Button favoriteButton;
    private JobAdapter jobAdapter;
    JobRepository jobRepository;

    public ItemDetailFragment() {
    }

    public static ItemDetailFragment newInstance(JobEntity jobEntity) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("jobEntity", jobEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobEntity = (JobEntity) getArguments().getSerializable("jobEntity");
        }
        jobRepository = new JobRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_detail, container, false);

        itemImage = view.findViewById(R.id.item_image);
        itemTitle = view.findViewById(R.id.item_title);
        itemLocation = view.findViewById(R.id.item_location);
        itemDescription = view.findViewById(R.id.item_description);
        backButton = view.findViewById(R.id.back_button);
        favoriteButton = view.findViewById(R.id.favorite_button);

        setItemDetails(jobEntity);

        backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            fragmentManager.popBackStack();

            View recyclerView = ((AppCompatActivity) v.getContext()).findViewById(R.id.view_pager);
            View searchView = ((AppCompatActivity) v.getContext()).findViewById(R.id.searchView);
            if (recyclerView != null && searchView != null) {
                recyclerView.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
            }
        });

        final Boolean[] jobInDb = {isJobInFavorites(jobEntity.id)};

        if (jobInDb[0]) {
            favoriteButton.setText("Delete from Favorites");
        } else {
            favoriteButton.setText("Add to Favorites");
        }

        favoriteButton.setOnClickListener(v -> {
            if (jobInDb[0]) {
                jobRepository.deleteFavoris(jobEntity);
                jobInDb[0] = false;
                favoriteButton.setText("Add to Favorites");
            } else {
                jobRepository.insertJob(jobEntity);
                jobInDb[0] = true;
                favoriteButton.setText("Delete from Favorites");
            }
        });
        return view;
    }

    private boolean isJobInFavorites(String jobId) {
        JobEntity jobEntity = jobRepository.getJobById(jobId);
        return jobEntity != null;
    }

    private void setItemDetails(JobEntity jobEntity) {
        if (jobEntity != null) {
            itemTitle.setText(jobEntity.title);
            itemLocation.setText(jobEntity.location);
            itemDescription.setText(jobEntity.description);
            if (jobEntity.image != null) {
                Picasso.get().load(jobEntity.image).into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.default_vacancy_image);
            }
            Log.d("ContextMenu", jobEntity.id);
        }
    }

    private void showContextMenuDeletion(RecyclerView.ViewHolder holder, int position) {
        View anchor = holder.itemView;
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.inflate(R.menu.context_menu_deletion);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.delete_from_favorites:
                    JobEntity selectedJob = jobAdapter.getJobEntityAtPosition(position);
                    jobRepository.deleteFavoris(selectedJob);
                    Log.d("ContextMenu", "delete a fav");
                    Snackbar.make(holder.itemView, "Favorite deleted !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }
}

