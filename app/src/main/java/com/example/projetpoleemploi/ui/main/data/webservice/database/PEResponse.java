package com.example.projetpoleemploi.ui.main.data.webservice.database;

import android.app.Application;
import android.util.Log;
import android.view.View;

import com.example.projetpoleemploi.ui.main.JobAdapter;
import com.example.projetpoleemploi.ui.main.SettingsFragment;
import com.example.projetpoleemploi.ui.main.database.JobDatabase;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import androidx.preference.SeekBarPreference;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
@JsonClass(generateAdapter = true)
public class PEResponse {
    String grant_type = "client_credentials";
    String client_id = "PAR_applicationmobile_642165f2e1d49a760c9a2eccd618d935e2b24eb33f79628e08760a31ba2bde3f";
    String client_secret = "a8cde988739bc8566269dbda84e6e35ac50af82ad09de12a606d78a126494af9";
    String scope = "api_offresdemploiv2 o2dsoffre";
    String accessToken;
    String expires_in;
    private PEInterface api;
    private JobResponse jobResponse = new JobResponse();
    private JobDatabase jobDatabase;
    private SettingsFragment settingsFragment = new SettingsFragment();
    private static volatile PEResponse INSTANCE;
    public PEResponse(Application application) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://entreprise.pole-emploi.fr")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        api = retrofit.create(PEInterface.class);
        jobDatabase = Room.databaseBuilder(application, JobDatabase.class, "job_database").build();
    }
    public static PEResponse get(Application application) throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new PEResponse(application);
        } else {
            System.out.println("CANNOT CREATE A SECOND INSTANCE");
        }
        return INSTANCE;
    }

    public void getAccessToken() {
        api.getAccessToken(grant_type, client_id, client_secret, scope).enqueue(
                new Callback<AccessTokenResponse>() {
                    public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                        if (response.isSuccessful()) {
                            accessToken = response.body().accessToken;
                            expires_in = response.body().expires_in;
                            System.out.println("TOKEN : " + accessToken);
                            System.out.println("EXPIRES IN : " + expires_in + " SECONDS");
                        } else {
                            System.out.println("ERROR - CODE : " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                        System.out.println("REQUEST FAILED");
                    }
                }
        );
    }

    public void getAvailableJobs(JobAdapter jobAdapter, String textSearched) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pole-emploi.io")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        api = retrofit.create(PEInterface.class);
        String resultsNumber = formateSliderValue(settingsFragment.getNumberOfResultsV());
        String typeContrat = settingsFragment.getTypeContrat();
        Integer departement = settingsFragment.getDepartement();
        if (departement == 0) {
            departement = null;
        }
        boolean inclureLimitrophes = settingsFragment.getLimi();
        boolean tempsPlein = settingsFragment.getFullTime();
        api.getAvailableJobs("Bearer " + accessToken, textSearched, resultsNumber, departement,
                inclureLimitrophes, tempsPlein, typeContrat).enqueue(
                new Callback<JobResponse>() {
                    @Override
                    public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                        if (response.isSuccessful()) {
                            System.out.println("REQUEST WORKED");
                            System.out.println(call.request());
                            if (response.body() == null) {
                                System.out.println("RESULTS EMPTY");
                            } else {
                                jobAdapter.updateJobs(response.body().resultats);
                            }
                        } else {
                            System.out.println("ERROR - CODE : " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<JobResponse> call, Throwable t) {
                        System.out.println("REQUEST FAILED");
                    }
                }
        );
    }

    public String formateSliderValue(int sliderValue) {
        if (sliderValue <= 0) {
            sliderValue = 1;
        }
        System.out.println("VALEUR SLIDERBAR : " + sliderValue);
        return "0-" + (sliderValue - 1);
    }
}