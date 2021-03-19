package at.fhooe.mc.datadora.RoomDatabase;


import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import at.fhooe.mc.datadora.BinarySearchTree.BinarySearchTree;


/**
 * The database class itself.
 * This class must be abstract and inherit from RoomDatabase.
 * Room is a database layer on top of an SQLite database.
 * There is only one instance needed of the database in the whole app.
 */
@Database(entities = {StackRoom.class, QueueRoom.class, SingleLinkedListRoom.class,
        DoubleLinkedListRoom.class, BinarySearchTreeRoom.class}, version = 10, exportSchema = false)
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
    public abstract SingleLinkedListDAO singleLinkedListDAO();
    public abstract DoubleLinkedListDAO doubleLinkedListDAO();
    public abstract BinarySearchTreeDAO binarySearchTreeDAO();

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
     * example of ADTs when the app is being used for the very first time.
     * Also instantiate each DAO and do not forget to add the callback above in the constructor.
     */
    private static final RoomDatabase.Callback roomCB = new RoomDatabase.Callback(){

        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {

                //keep in mind that onOpen is only called when app started and database opened
                SingleLinkedListDAO singleLinkedListDAO = mInstance.singleLinkedListDAO();
                DoubleLinkedListDAO doubleLinkedListDAO = mInstance.doubleLinkedListDAO();

                //continue the position counter after app restart (until the list is cleared)
                int position = singleLinkedListDAO.getMaxPosition();
                int pos2 = doubleLinkedListDAO.getMaxPosition();
                Log.i("TAG", "DatadoraDatabase onOpen gerMaxPosition was: " + singleLinkedListDAO.getMaxPosition());

                if (position != 0){
                    singleLinkedListDAO.setLastPosition(position);
                    Log.i("TAG", "DatadoraDatabase onOpen position was set to: " + position);
                }

                if (pos2 != 0){
                    doubleLinkedListDAO.setLastPosition(pos2);
                }


            });


        }

        /**
         * Called when the database is created for the first time (after installing the app)
         * This is called after all the tables are created.
         * @param db the database
         */
        public void onCreate(SupportSQLiteDatabase db){
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                StackDAO stackDAO = mInstance.stackDAO();
                stackDAO.deleteAllStackDBEntries(); //clear it the first time
                StackRoom newVal = new StackRoom(2);
                stackDAO.insert(newVal);
                newVal = new StackRoom(3);
                stackDAO.insert(newVal);
                newVal = new StackRoom(2);
                stackDAO.insert(newVal);
                newVal = new StackRoom(4);
                stackDAO.insert(newVal);

                QueueDAO queueDAO = mInstance.queueDAO();
                queueDAO.deleteAllQueueDBEntries();
                QueueRoom qVal = new QueueRoom(1);
                queueDAO.insert(qVal);
                qVal = new QueueRoom(3);
                queueDAO.insert(qVal);
                qVal = new QueueRoom(3);
                queueDAO.insert(qVal);
                qVal = new QueueRoom(7);
                queueDAO.insert(qVal);

                SingleLinkedListDAO singleLinkedListDAO = mInstance.singleLinkedListDAO();
                singleLinkedListDAO.deleteAllSingleLinkedListDBEntries();
                SingleLinkedListRoom sllr = new SingleLinkedListRoom(2);
                singleLinkedListDAO.insert(sllr);
                sllr = new SingleLinkedListRoom(4);
                singleLinkedListDAO.insert(sllr);
                sllr = new SingleLinkedListRoom(6);
                singleLinkedListDAO.insert(sllr);
                sllr = new SingleLinkedListRoom(8);
                singleLinkedListDAO.insert(sllr);
                sllr = new SingleLinkedListRoom(10);
                singleLinkedListDAO.insert(sllr);
                sllr = new SingleLinkedListRoom(12);
                singleLinkedListDAO.insert(sllr);

                DoubleLinkedListDAO doubleLinkedListDAO = mInstance.doubleLinkedListDAO();
                doubleLinkedListDAO.deleteAllDoubleLinkedListDBEntries();
                DoubleLinkedListRoom dllr = new DoubleLinkedListRoom(1);
                doubleLinkedListDAO.insert(dllr);
                dllr = new DoubleLinkedListRoom(3);
                doubleLinkedListDAO.insert(dllr);
                dllr = new DoubleLinkedListRoom(5);
                doubleLinkedListDAO.insert(dllr);
                doubleLinkedListDAO.insert(dllr);
                dllr = new DoubleLinkedListRoom(7);
                doubleLinkedListDAO.insert(dllr);
                doubleLinkedListDAO.insert(dllr);
                dllr = new DoubleLinkedListRoom(9);
                doubleLinkedListDAO.insert(dllr);

                BinarySearchTreeDAO bstDAO = mInstance.binarySearchTreeDAO();
                bstDAO.deleteAllBinarySearchTreeDBEntries();
                BinarySearchTreeRoom bstr = new BinarySearchTreeRoom(88);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(65);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(97);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(54);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(82);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(99);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(76);
                bstDAO.insert(bstr);
                bstr = new BinarySearchTreeRoom(80);
                bstDAO.insert(bstr);


            });


        }

    };





}
