package at.fhooe.mc.datadora.RoomDatabase;

import android.app.Application;

import java.util.List;
import java.util.Vector;

import androidx.lifecycle.LiveData;

/**
 * The repository class provides a clean API for data access to the rest of the application.
 */
public class DatadoraRepository {

    private final StackDAO mStackDao;
    private final LiveData<List<StackRoom>> mAllStackValues;

    private final QueueDAO mQueueDao;
    private final LiveData<List<QueueRoom>> mAllQueueValues;

    /**
     * Constructor of the repository
     * The DAO is passed here because through the DAO the read/write methods can be accessed.
     * There is no need to expose the entire database to the repository.
     * @param application the application dependency
     */
    DatadoraRepository(Application application){
        DatadoraDatabase db = DatadoraDatabase.getDatabaseInstance(application);

        mStackDao = db.stackDAO();
        mQueueDao = db.queueDAO();

        mAllStackValues = mStackDao.getAllStackValues();
        mAllQueueValues = mQueueDao.getAllQueueValues();

    }


    /**
     * Room executes all queries on a separate thread.
     * Observed LiveData will notify the observer when the data has changed.
     * @return all stack values in the stack table
     */
    LiveData<List<StackRoom>> getAllStackValues() {
        return mAllStackValues;
    }

    /**
     * Room executes all queries on a separate thread.
     * Observed LiveData will notify the observer when the data has changed.
     * @return all queue values in the queue table
     */
    LiveData<List<QueueRoom>> getAllQueueValues() {
        return mAllQueueValues;
    }



    /**
     * Create methods for all the different database operations.
     * These methods need to be called on a non-UI thread or the app will throw an exception.
     * Hence the ExecutorService is used which is defined in the DatadoraDatabase class.
     * Those are the ones that the API exposes to the outside.
     */


    void insert(StackRoom stackVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mStackDao.insert(stackVal);
        });
    }

    void insert(QueueRoom queueVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mQueueDao.insert(queueVal);
        });
    }

    //TODO check if insertAll is even needed in the end
    void insertAllStack(List<StackRoom> stackValues){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            for (StackRoom sr : stackValues){
                mStackDao.insert(sr);
            }
        });
    }


    void insertAllQueue(List<QueueRoom> queueValues){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            for (QueueRoom qr : queueValues){
                mQueueDao.insert(qr);
            }
        });
    }

    void delete(StackRoom stackVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mStackDao.delete(stackVal);
        });
    }

    void delete(QueueRoom queueVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mQueueDao.delete(queueVal);
        });
    }

    void deleteByIDStack(int stackVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mStackDao.deleteByID(stackVal);
        });
    }

    void deleteByIDQueue(int queueVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mQueueDao.deleteByID(queueVal);
        });
    }



    void deleteAllStack(){
        DatadoraDatabase.databaseWriteExecutor.execute(mStackDao::deleteAllStackDBEntries);
    }

    void deleteAllQueue(){
        DatadoraDatabase.databaseWriteExecutor.execute(mQueueDao::deleteAllQueueDBEntries);
    }




}
