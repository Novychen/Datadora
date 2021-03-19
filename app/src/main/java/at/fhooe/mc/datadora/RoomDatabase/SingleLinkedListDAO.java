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
    abstract public void insert(SingleLinkedListRoom val); //append

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

    //TODO: warum nicht position??
    public void insertAt(SingleLinkedListRoom val, int position){

        mPosition = position;

        insert(val);
    }

    //TODO: check position, Constructor in Room sollte es ermöglichen!
    public void insertAt(SingleLinkedListRoom singleListVal){
        insert(singleListVal);
    }


    //TODO die größte id rausfinden -


    //@Query("SELECT * FROM single_list_table WHERE position = (SELECT max(position) FROM single_list_table)")
    @Query("SELECT max(position) FROM single_list_table")
    abstract public int getMaxPosition();

    public void setLastPosition(int position){

        //getMaxPosition();
        //Log.i("TAG",  getMaxPosition());

        mPosition = position;
        Log.i("TAG", "getLastPosition"+ position);
    }


    //die positions gehören upgedated nachdem die liste neu geladen wurde, sonst fängt es wieder bei 1 an - erledigt im der Database in der onOpen
    //bzw. die position fängt wieder bei 1 an nach app restart! - done
    //bei operatin CLEAR gehört die position resetted - done

    //TODO: prepend
    //TODO: eine 2. spalte mit position zwei machen? LOL das wird ein clusterfuck
    //TODO: 3. spalte position3 für insert at?? auch meh

    //TODO: wie mach ich dann beim delete At?

    //TODO: oder einfach mal IRGENDWIE einfügen und dann einfach SORTIEREN VOR DER AUSGABE?
    //TODO: oder die APPEND bei z.B. Position 30 beginnen.
    //TODO: die PRPEND bei z.B. Position 0 - 18
    //TODO: die insertAt sind wieder cancer, weil man nicht weiß wie man es ordnen muss


    //TODO: beim random knopf ist die position auf einmal irgendwas, mal schauen wie man das ändert


    // @Query("UPDATE single_list_table SET position = position + 1 WHERE position = :pos")

    //TODO: mit dieser query sollen die values erneuert werden

    //DIE VALUES BEI DELETE FIRST UND DELETE LAST WERDEN SCHON AUTOMATISCH GEÄNDERT
    //TODO: das prepend jetzt anschauen

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

    //TODO DOUBLE VALUES
    @Query("DELETE FROM single_list_table WHERE val = :val")
    abstract public void deleteByIDAt(int val);

    @Query("DELETE FROM single_list_table")
    abstract void deleteAllSingleLinkedListDBEntries();

    @Query(("SELECT * FROM single_list_table"))
    abstract public LiveData<List<SingleLinkedListRoom>> getAllSingleLinkedListValues();

    @Query(("SELECT * FROM single_list_table ORDER BY val ASC"))
    abstract public LiveData<List<SingleLinkedListRoom>> getAlphabetizedSingleLinkedListValues();



    //DATE
    
    //TODO check if it works with void, worked with long. works with void too

    /*
    @Query("INSERT INTO single_list_table (id, val, created_at, updated_at) VALUES (:val)")
    public abstract void actualInsertFront(SingleLinkedListRoom val); //for insertFront and insertAt

    public void insertFront(SingleLinkedListRoom val){
        //val.setCreatedAt(new Date(System.currentTimeMillis()));
        //val.setUpdatedAt(new Date(System.currentTimeMillis()));
        actualInsertFront(val);
    }


    @Update
    public abstract void actualUpdate(SingleLinkedListRoom val);

    public void updateTwo(SingleLinkedListRoom val) {
        val.setUpdatedAt(new Date(System.currentTimeMillis()));
        actualUpdate(val);
    }

     */





}
