package com.example.projetpoleemploi;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.projetpoleemploi.ui.main.PlaceholderFragment.ARG_SECTION_NUMBER;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.content.Intent;
import android.os.Bundle;

import com.example.projetpoleemploi.databinding.ActivityMainBinding;
import com.example.projetpoleemploi.ui.main.PlaceholderFragment;
import com.example.projetpoleemploi.ui.main.SettingsFragment;
import com.example.projetpoleemploi.ui.main.data.webservice.database.PEResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.SearchView;

import com.example.projetpoleemploi.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PEResponse peResponse;
    private String textSearched = "Informatique";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onSearch();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton settingsButton = findViewById(R.id.settingsButton);
        FloatingActionButton backSettingsButton = findViewById(R.id.backSettingsButton);
        CardView settingsContainer = findViewById(R.id.settingsContainer);

        settingsButton.setOnClickListener(v -> {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settingsContainer, new SettingsFragment())
                        .commit();
                settingsContainer.setVisibility(VISIBLE);
                settingsButton.setVisibility(INVISIBLE);
                backSettingsButton.setVisibility(VISIBLE);
                backSettingsButton.setClickable(TRUE);
            });

        backSettingsButton.setOnClickListener(v -> {
                settingsContainer.setVisibility(INVISIBLE);
                backSettingsButton.setVisibility(INVISIBLE);
                settingsButton.setVisibility(VISIBLE);
                settingsButton.setClickable(TRUE);
        });
    }

    public void onSearch() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearched = s;
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof PlaceholderFragment && fragment.getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                        ((PlaceholderFragment) fragment).getJobList(textSearched);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public String getTextSearched() {
        return textSearched;
    }
}