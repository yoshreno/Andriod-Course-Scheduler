package Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import DAO.AssessmentDAO;
import DAO.CourseDAO;
import DAO.TermDAO;
import Entity.Assessment;
import Entity.Course;
import Entity.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 5, exportSchema = false)
public abstract class TermScheduleDatabase extends RoomDatabase {

    public abstract TermDAO termDao();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile TermScheduleDatabase INSTANCE;
    //private static final int NUMBER_OF_THREADS = 4;
    //static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TermScheduleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermScheduleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TermScheduleDatabase.class, "TermSchedule_DB")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}