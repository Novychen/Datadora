package at.fhooe.mc.datadora.RoomDatabase;


import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


/**
 * The database class itself.
 * This class must be abstract and inherit from RoomDatabase.
 * Room is a database layer on top of an SQLite database.
 * There is only one instance needed of the database in the whole app.
 */
@Database(entities = {StackRoom.class, QueueRoom.class}, version = 4, exportSchema = false)
public abstract class DatadoraDatabase extends RoomDatabase {


    /**
     * Making a singleton of this class so only 1 instance can be created
     * to prevent having multiple instances of the db opened at the same time.
     */
    private static DatadoraDatabase mInstance;


    /**
     * The executor service has a fixed thread pool that is used to run database
     * operations asynchronously on a background thread.
     * To avoid poor UI performance, Room does not allow to issue queries on the main thread.
     */
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Creating an instance of each data access object to access the data.
     */
    public abstract StackDAO stackDAO();
    public abstract QueueDAO queueDAO();

    /**
     * Create the database. Only one instance possible because of singleton.
     * @param context the context
     * @return the instance of the database aka the singleton
     */
    public static synchronized DatadoraDatabase getDatabaseInstance(Context context){

        //only instantiate the database if its not already created
        if (mInstance == null){
            mInstance = Room.databaseBuilder(
                    context.getApplicationContext(), DatadoraDatabase.class, "datadora_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCB) //add callback after creating it
                    .build();
        }
        return mInstance;
    }

    /**
     * Pre-populate the database right when created so the user is already provided with pre-filled
     * example of ADTs when the app is being used for the first time.
     * Also instantiate each DAO and do not forget to add the callback above in the constructor.
     */
    private static final RoomDatabase.Callback roomCB = new RoomDatabase.Callback(){

        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {



                StackDAO stackDAO = mInstance.stackDAO();

                /*
                stackDAO.deleteAllStackDBEntries(); //clear it the first time
                StackRoom newVal = new StackRoom(2);
                stackDAO.insert(newVal);
                newVal = new StackRoom(3);
                stackDAO.insert(newVal);
                newVal = new StackRoom(2);
                stackDAO.insert(newVal);
                newVal = new StackRoom(4);
                stackDAO.insert(newVal);

                 */

                QueueDAO queueDAO = mInstance.queueDAO();
                //TODO prepoulate queue db e.g. 1337 later


            });

        }

    };





}
