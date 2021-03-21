package at.fhooe.mc.datadora.RoomDatabase;


import android.util.Log;

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
public abstract class SingleLinkedListDAO implements BaseDAO<SingleLinkedListRoom>{



    int mPosition;

    private void setPositionIncrement(){
        mPosition = mPosition + 1;
    }

    public void setPositionDecrement(){
        mPosition = mPosition - 1;
    }

    public void resetPosition(){
        mPosition = 0;
    }

    public int getLastPosition(){
        return mPosition;
    }



    @Insert
    abstract public void insert(SingleLinkedListRoom val);

    //called in the repo
    public void append(SingleLinkedListRoom val){
        setPositionIncrement();
        val.setPosition(mPosition);
        insert(val);
    }

    public void prepend(SingleLinkedListRoom val){
        mPosition = 0;
        val.setPosition(mPosition);
        insert(val);
    }

    public void insertAt(SingleLinkedListRoom val, int position){
        mPosition = position;
        insert(val);
    }

    public void insertAt(SingleLinkedListRoom singleListVal){
        insert(singleListVal);
    }


    @Query("SELECT max(position) FROM single_list_table")
    abstract public int getMaxPosition();

    public void setLastPosition(int position){

        mPosition = position;
        Log.i("TAG", "getLastPosition"+ position);
    }


    @Query("UPDATE single_list_table SET position = position - 1") //after deleting the first item, values are decreased
    public abstract void decrementPositionColumn();

    @Query("UPDATE single_list_table SET position = position + 1")
    public abstract void incrementPositionColumn();

    @Update
    abstract public void update(SingleLinkedListRoom val);

    @Delete
    abstract public void delete(SingleLinkedListRoom val);

    @Query("DELETE FROM single_list_table WHERE val = :val AND id = (SELECT min(id) FROM single_list_table)")
    abstract public void deleteByIDFirst(int val);

    @Query("DELETE FROM single_list_table WHERE val = :val AND id = (SELECT max(id) FROM single_list_table)")
    abstract public void deleteByIDLast(int val);

    @Query("DELETE FROM single_list_table WHERE val = :val")
    abstract public void deleteByIDAt(int val);

    @Query("DELETE FROM single_list_table")
    abstract void deleteAllSingleLinkedListDBEntries();

    @Query(("SELECT * FROM single_list_table"))
    abstract public LiveData<List<SingleLinkedListRoom>> getAllSingleLinkedListValues();

    @Query(("SELECT * FROM single_list_table ORDER BY val ASC"))
    abstract public LiveData<List<SingleLinkedListRoom>> getAlphabetizedSingleLinkedListValues();

}
