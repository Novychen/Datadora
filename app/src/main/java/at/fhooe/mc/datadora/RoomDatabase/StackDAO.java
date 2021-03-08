package at.fhooe.mc.datadora.RoomDatabase;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * A data access object validates the SQL at compile-time and associates it with a method.
 * The annotations represent the most common database operations.
 * LiveData solves the problem of displaying the updated data in the UI.
 * It is a data holder class that is aware of lifecycle events.
 * LiveData observes the changes in the database and notifies the observer when data changes.
 */
@Dao
public abstract class StackDAO implements BaseDAO<StackRoom> {

    @Insert
    abstract public void insert(StackRoom val);

    @Insert
    abstract public void insertAll(List<StackRoom> values);

    @Update
    abstract public void update(StackRoom val);

    @Delete
    abstract public void delete(StackRoom val);


    @Query("DELETE FROM stack_table WHERE val = :val")
    abstract public void deleteByID(int val);

    @Query("DELETE FROM stack_table")
    abstract void deleteAllStackDBEntries();

    @Query(("SELECT * FROM stack_table"))
    abstract public LiveData<List<StackRoom>> getAllStackValues();

    @Query(("SELECT * FROM stack_table ORDER BY val ASC"))
    abstract public LiveData<List<StackRoom>> getAlphabetizedStackValues();

}
