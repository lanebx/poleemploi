package com.example.projetpoleemploi.ui.main;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetpoleemploi.MainActivity;
import com.example.projetpoleemploi.R;
import com.example.projetpoleemploi.databinding.FragmentMainBinding;
import com.example.projetpoleemploi.ui.main.data.webservice.database.JobResponse;
import com.example.projetpoleemploi.ui.main.data.webservice.database.PEResponse;
import com.example.projetpoleemploi.ui.main.database.JobEntity;
import com.example.projetpoleemploi.ui.main.database.JobRepository;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import android.widget.PopupMenu;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private FragmentMainBinding binding;

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private MainActivity mainActivity;
    private String textSearched;
    private SettingsFragment settingsFragment = new SettingsFragment();
    TextView textEnterSearch;
    ProgressBar progressBar;
    JobRepository jobRepository;

    public static PlaceholderFragment newInstance(int index, String textSearched) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString("textSearched", textSearched);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        String index = "Favoris";
        if (getArguments() != null) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                index = "Emplois";
            }
            textSearched = getArguments().getString("textSearched");
        }
        jobRepository = new JobRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        jobAdapter = new JobAdapter(new ArrayList<>());

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            binding.recyclerView.setAdapter(jobAdapter);
        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            getFavoritesList();
        }

        textSearched = mainActivity.getTextSearched();

        jobAdapter.setOnItemLongClickListener((holder, position) -> {
            showContextMenu(holder, position);
        });

        jobAdapter.setOnItemClickListener((position, jobList, v) -> {
            openItemDetail(position, jobList, v);
        });

        progressBar = binding.progressBar;
        textEnterSearch = binding.textEnterSearch;

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            showStatusMessage("Search for vacancies");
        }

        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            getJobList(textSearched);
        }

        return root;
    }

    public void getJobList(String textSearched) {
        Application application = getActivity().getApplication();
        try {
            PEResponse peResponse = PEResponse.get(application);
            peResponse.getAccessToken();

            textEnterSearch.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                peResponse.getAvailableJobs(jobAdapter, textSearched);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                progressBar.setVisibility(View.INVISIBLE);

            }, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFavoritesList() {
        Observer<List<JobEntity>> favoriteObserver = jobEntities -> {
            List<JobEntity> favoriteJobs = jobEntities;
            List<JobEntity> favoriteFiltered = new ArrayList<>();

            String typeContrat = settingsFragment.getTypeContratF();
            boolean onlyLogo = settingsFragment.getOnlyLogo();

            for (JobEntity job : favoriteJobs) {
                if (typeContrat.equals("CDD")) {
                    if (job.typeContrat != "2") {
                        continue;
                    }
                }
                if (typeContrat.equals("CDI")) {
                    if (job.typeContrat != "1") {
                        continue;
                    }
                }
                if (onlyLogo) {
                    if (job.image.equals(R.drawable.default_vacancy_image)) {
                        continue;
                    }
                }
                favoriteFiltered.add(job);
            }

            // Alphabetical order of jobs
            if (settingsFragment.getAlphabeticalV()) {
                Collections.sort(favoriteFiltered, Comparator.comparing(j -> j.title));
            }

            FavorisAdapter favorisAdapter = new FavorisAdapter(favoriteFiltered);
            favorisAdapter.setOnItemLongClickListener((holder, position) -> {
                showContextMenu(holder, position, favoriteFiltered);
            });

            binding.recyclerView.setAdapter(favorisAdapter);
        };

        jobRepository.getAllFavoris().observe(getViewLifecycleOwner(), favoriteObserver);
    }

    private void showStatusMessage(String message) {
        TextView statusMessage = binding.textEnterSearch;
        statusMessage.setVisibility(View.VISIBLE);
        statusMessage.setText(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showContextMenu(RecyclerView.ViewHolder holder, int position) {
        JobEntity selectedJob = jobAdapter.getJobEntityAtPosition(position);
        View anchor = holder.itemView;
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.inflate(R.menu.context_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.add_to_favorites:
                    jobRepository.insertJob(selectedJob);
                    Log.d("ContextMenu", "add to fav");
                    Snackbar.make(holder.itemView, "Favorite added !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
                case R.id.copy_to_clipboard:
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Offer link", "https://candidat.pole-emploi.fr/offres/recherche/detail/" + selectedJob.id);
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(holder.itemView, "Offer link copied !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void showContextMenu(RecyclerView.ViewHolder holder, int position, List<JobEntity> favoriteJobs) {
        JobEntity selectedJob = favoriteJobs.get(position);
        View anchor = holder.itemView;
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        popupMenu.inflate(R.menu.context_menu_deletion);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.delete_from_favorites:
                    jobRepository.deleteFavoris(selectedJob);
                    Log.d("ContextMenu", "remove from fav");
                    Snackbar.make(holder.itemView, "Favorite removed !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
                case R.id.copy_to_clipboard:
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Offer link", "https://candidat.pole-emploi.fr/offres/recherche/detail/" + selectedJob.id);
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(holder.itemView, "Offer link copied !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    private void openItemDetail(int position, List<JobResponse.Resultat> jobList, View v) {
        String mes = String.valueOf(position);
        Log.d("Open Item in API list", jobList.get(position).intitule);

        JobEntity jobEntity = JobEntity.fromResultat(jobList.get(position)); // JobAdapter

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
    }
}