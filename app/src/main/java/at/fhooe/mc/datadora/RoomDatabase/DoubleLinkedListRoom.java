package at.fhooe.mc.datadora.RoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity class for the double linked list is represented as a SQLite table
 * aka the table that will hold the list values.
 * Each property in this class represents a column in the table.
 */
@Entity(tableName = "double_list_table")
public class DoubleLinkedListRoom {

    /**
     * An auto-generated primary key.
     * Generates an unique key aka an integer that is incremented.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * An integer value that holds the actual data from the user input.
     */
    private final int val;


    /**
     * Helper value to sort the values
     */
    private int position;


    /**
     * Constructor
     * @param val the value to be stored
     */
    public DoubleLinkedListRoom(int val){
        this.val = val;
    }

    /**
     * Constructor 2
     * @param val the value to be stored
     * @param pos the position aka the index where in the list
     */
    public DoubleLinkedListRoom(int val, int pos){
        this.val = val;
        this.position = pos;
    }


    /**
     * Getter method for the actual values
     * @return the current value
     */
    public int getVal() {
        return val;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

}
