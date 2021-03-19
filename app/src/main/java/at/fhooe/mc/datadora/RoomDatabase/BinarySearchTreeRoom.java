package at.fhooe.mc.datadora.RoomDatabase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * The entity class for the binary search tree is represented as a SQLite table
 * aka the table that will hold the tree values.
 * Each property in this class represents a column in the table.
 */
@Entity(tableName = "bst_table")
public class BinarySearchTreeRoom {

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
     * Constructor
     * @param val the value to be stored
     */
    public BinarySearchTreeRoom(int val){
        this.val = val;
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
}
