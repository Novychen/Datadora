package at.fhooe.mc.datadora.RoomDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


/**
 * This Base Database Access Object interface defines generic (CRUD) methods that are the same for each DAO.
 * Each DAO that inherits from this interface defines more methods that are specific to each of them.
 * @param <T> generic placeholder
 */

@Dao
public interface BaseDAO<T> {

    //allowing the insert of the same value multiple times by passing a
    //conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(T val);

    @Update
    void update(T val);

    @Delete
    void delete(T val);

}
