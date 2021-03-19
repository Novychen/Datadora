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
public abstract class DoubleLinkedListDAO implements BaseDAO<DoubleLinkedListRoom> {


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
    abstract public void insert(DoubleLinkedListRoom val);

    //the actual insert methods that are called in the repository class
    public void append(DoubleLinkedListRoom val){
        setPositionIncrement();
        val.setPosition(mPosition);
        insert(val);
    }

    public void prepend(DoubleLinkedListRoom val){
        mPosition = 0;
        val.setPosition(mPosition);
        insert(val);
    }

    public void insertAt(DoubleLinkedListRoom val, int position){

        mPosition = position;

        insert(val);
    }

    public void insertAt(DoubleLinkedListRoom val){
        insert(val);
    }



    @Query("SELECT max(position) FROM double_list_table")
    abstract public int getMaxPosition();

    public void setLastPosition(int position){

        mPosition = position;
        Log.i("TAG", "getLastPosition"+ position);
    }


    @Query("UPDATE double_list_table SET position = position - 1") //after deleting the first item, values are decreased
    public abstract void decrementPositionColumn();

    @Query("UPDATE double_list_table SET position = position + 1")
    public abstract void incrementPositionColumn();

    @Update
    abstract public void update(DoubleLinkedListRoom val);

    @Delete
    abstract public void delete(DoubleLinkedListRoom val);

    @Query("DELETE FROM double_list_table WHERE val = :val AND id = (SELECT min(id) FROM double_list_table)")
    abstract public void deleteByIDFirst(int val);

    @Query("DELETE FROM double_list_table WHERE val = :val AND id = (SELECT max(id) FROM double_list_table)")
    abstract public void deleteByIDLast(int val);

    //TODO DOUBLE VALUES
    @Query("DELETE FROM double_list_table WHERE val = :val")
    abstract public void deleteByIDAt(int val);

    @Query("DELETE FROM double_list_table")
    abstract void deleteAllDoubleLinkedListDBEntries();

    @Query(("SELECT * FROM double_list_table"))
    abstract public LiveData<List<DoubleLinkedListRoom>> getAllDoubleLinkedListValues();

    @Query(("SELECT * FROM double_list_table ORDER BY val ASC"))
    abstract public LiveData<List<DoubleLinkedListRoom>> getAlphabetizedDoubleLinkedListValues();
}
