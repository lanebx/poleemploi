package com.example.projetpoleemploi.ui.main.database;

import static com.example.projetpoleemploi.ui.main.database.JobDatabase.databaseWriteExecutor;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projetpoleemploi.ui.main.data.webservice.database.JobResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JobRepository {
    private LiveData<List<JobEntity>> allFavoris;
    private JobDao jobDao;
    private MutableLiveData<JobEntity> selectedFavori = new MutableLiveData<>();

    public JobRepository(Application application) {
        JobDatabase db = JobDatabase.getDatabase(application);
        jobDao = db.jobDao();
        allFavoris = jobDao.getAllJobs();
    }

    public LiveData<List<JobEntity>> getAllFavoris() {
        return allFavoris;
    }
    public MutableLiveData<JobEntity> getSearchResults() {
        return selectedFavori;
    }
    public void insertJob(JobEntity newJob) {
        databaseWriteExecutor.execute(() -> {
            jobDao.insert(newJob);
        });
    }

    public void deleteFavoris(JobEntity oldJob) {
        databaseWriteExecutor.execute(() -> {
            jobDao.delete(oldJob);
        });
    }

    public void setSelectedFavori(String id) {
        Future<JobEntity> fjob = databaseWriteExecutor.submit(() -> {
            return jobDao.getJobById(id);
        });
        try {
            selectedFavori.setValue(fjob.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JobEntity getJobById(String id) {
        Future<JobEntity> fjob = databaseWriteExecutor.submit(() -> {
            return jobDao.getJobById(id);
        });
        try {
            return fjob.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
