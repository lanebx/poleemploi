package com.example.projetpoleemploi.ui.main.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {JobEntity.class}, version = 1)
public abstract class JobDatabase extends RoomDatabase {
    public abstract JobDao jobDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile JobDatabase INSTANCE;

    public static JobDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JobDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JobDatabase.class, "job_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;

//        if the database does not work. put before the build
//        .fallbackToDestructiveMigration()
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                JobDao dao = INSTANCE.jobDao();
                dao.getAllJobs();

                for (JobEntity oneFav : JobEntity.fav)
                    dao.insert(oneFav);

            });
        }
    };
}
