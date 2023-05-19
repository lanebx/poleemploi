package com.example.projetpoleemploi.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import com.example.projetpoleemploi.R;
import com.example.projetpoleemploi.ui.main.database.JobDao;

public class SettingsFragment extends PreferenceFragmentCompat {
    private EditTextPreference numberOfResults;
    private EditTextPreference departementChoice;
    private SwitchPreference logo;
    private SwitchPreference includeLimiSwitch;
    private SwitchPreference fullTimeSwitch;
    private SwitchPreference alphabetical;
    private static SharedPreferences preferences;
    private static int numberOfResultsV = 25;
    private static String typeContrat = "CDD,CDI";
    private static String typeContratF = "CDD,CDI";
    private static Integer departement = 0;
    private static boolean onlyLogo = false;
    private static boolean includeLimi = false;
    private static boolean fullTime = false;
    private static boolean alphabeticalV = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        typeContrat = preferences.getString("radio_preference", "CDI,CDD");
        typeContratF = preferences.getString("radio_preference2", "CDI,CDD");

        // Number of requests

        numberOfResults = findPreference("numberOfResults");
        numberOfResults.setOnPreferenceChangeListener((preference, newValue) -> {
            if (preference.getKey().equals("numberOfResults")) {
                if (newValue.toString().matches("\\d+")) {
                    if (Integer.parseInt((String) newValue) > 50) {
                        numberOfResultsV = 50;
                    } else {
                        numberOfResultsV = Integer.parseInt((String) newValue);
                    }
                }
                else {
                    System.out.println("WRONG VALUE");
                }
            }
            return true;
        });

        // Departements

        departementChoice = findPreference("departementChoice");
        departementChoice.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.toString().matches("\\d+")) {
                departement = Integer.parseInt(newValue.toString());
            } else {
                System.out.println("CAN'T FIND THE DEPARTEMENT");
            }
            return true;
        });

        // Limitrophes

        includeLimiSwitch = findPreference("inclureLimi");
        includeLimiSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!includeLimiSwitch.isChecked()) {
                includeLimi = true;
            } else {
                includeLimi = false;
            }
            return true;
        });

        // Full-time

        fullTimeSwitch = findPreference("fullTime");
        fullTimeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!fullTimeSwitch.isChecked()) {
                fullTime = true;
            } else {
                fullTime = false;
            }
            return true;
        });

        // Departement choice

        logo = findPreference("logo");
        logo.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!logo.isChecked()) {
                System.out.println("CHECKED");
                onlyLogo = true;
            } else {
                onlyLogo = false;
            }
            return true;
        });

        // Alphabetical order

        alphabetical = findPreference("alphabetical");
        alphabetical.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!alphabetical.isChecked()) {
                System.out.println("CHECKED");
                alphabeticalV = true;
            } else {
                alphabeticalV = false;
            }
            return true;
        });
    }

    public int getNumberOfResultsV() {
        return numberOfResultsV;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public int getDepartement() {
        return departement;
    }

    public boolean getLimi() {
        return includeLimi;
    }

    public boolean getFullTime() {
        return fullTime;
    }
    public String getTypeContratF() {
        return typeContratF;
    }

    public boolean getAlphabeticalV() {
        return alphabeticalV;
    }
    public boolean getOnlyLogo() {
        return onlyLogo;
    }
}
