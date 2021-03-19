package at.fhooe.mc.datadora.RoomDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;


/**
 * A data access object validates the SQL at compile-time and associates it with a method.
 * The annotations represent the most common database operations.
 * LiveData solves the problem of displaying the updated data in the UI.
 * It is a data holder class that is aware of lifecycle events.
 * LiveData observes the changes in the database and notifies the observer when data changes.
 */
@Dao
public abstract class BinarySearchTreeDAO implements BaseDAO<BinarySearchTreeRoom> {

    @Insert
    abstract public void insert(BinarySearchTreeRoom val);

    @Update
    abstract public void update(BinarySearchTreeRoom val);

    @Delete
    abstract public void delete(BinarySearchTreeRoom val);

    //no duplicate values can occur
    @Query("DELETE FROM bst_table WHERE val = :val")
    abstract public void deleteByID(int val);

    @Query("DELETE FROM bst_table")
    abstract void deleteAllBinarySearchTreeDBEntries();

    @Query(("SELECT * FROM bst_table"))
    abstract public LiveData<List<BinarySearchTreeRoom>> getAllBSTValues();

    @Query(("SELECT * FROM bst_table ORDER BY val ASC"))
    abstract public LiveData<List<BinarySearchTreeRoom>> getAlphabetizedBSTValues();

}
