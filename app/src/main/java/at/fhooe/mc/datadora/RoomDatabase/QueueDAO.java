package at.fhooe.mc.datadora.RoomDatabase;

import java.util.List;
import java.util.Queue;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
public abstract class QueueDAO implements BaseDAO<QueueRoom> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract public void insert(QueueRoom val);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract public void insertAll(List<QueueRoom> values);

    @Update
    abstract public void update(QueueRoom val);

    @Delete
    abstract public void delete(QueueRoom val);

    @Query("DELETE FROM queue_table WHERE val = :val")
    abstract public void deleteByID(int val);

    @Query("DELETE FROM queue_table")
    abstract void deleteAllQueueDBEntries();

    @Query(("SELECT * FROM queue_table"))
    abstract public LiveData<List<QueueRoom>> getAllQueueValues();

    @Query(("SELECT * FROM queue_table ORDER BY val ASC"))
    abstract public LiveData<List<QueueRoom>> getAlphabetizedQueueValues();
}
