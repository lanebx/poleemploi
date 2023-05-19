package com.example.projetpoleemploi.ui.main.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface JobDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(JobEntity jobEntity);

    @Delete
    void delete(JobEntity jobEntity);

    @Query("SELECT * FROM job_table WHERE id = :jobId")
    JobEntity getJobById(String jobId);

    @Query("SELECT * FROM job_table")
    LiveData<List<JobEntity>> getAllJobs();
}