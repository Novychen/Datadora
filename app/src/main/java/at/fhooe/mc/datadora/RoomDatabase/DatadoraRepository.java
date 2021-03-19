package at.fhooe.mc.datadora.RoomDatabase;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * The repository class provides a clean API for data access to the rest of the application.
 */
public class DatadoraRepository {

    private final StackDAO mStackDao;
    private final LiveData<List<StackRoom>> mAllStackValues;

    private final QueueDAO mQueueDao;
    private final LiveData<List<QueueRoom>> mAllQueueValues;

    private final SingleLinkedListDAO mSingleListDao;
    private final LiveData<List<SingleLinkedListRoom>> mAllSingleListValues;

    private final DoubleLinkedListDAO mDoubleListDao;
    private final LiveData<List<DoubleLinkedListRoom>> mAllDoubleListValues;

    private final BinarySearchTreeDAO mBSTDao;
    private final LiveData<List<BinarySearchTreeRoom>> mAllBSTValues;

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
        mSingleListDao = db.singleLinkedListDAO();
        mDoubleListDao = db.doubleLinkedListDAO();
        mBSTDao = db.binarySearchTreeDAO();

        mAllStackValues = mStackDao.getAllStackValues();
        mAllQueueValues = mQueueDao.getAllQueueValues();
        mAllSingleListValues = mSingleListDao.getAllSingleLinkedListValues();
        mAllDoubleListValues = mDoubleListDao.getAllDoubleLinkedListValues();
        mAllBSTValues = mBSTDao.getAllBSTValues();

    }


    /**
     * Room executes all queries on a separate thread.
     * Observed LiveData will notify the observer when the data has changed.
     * @return all the values of each table
     */
    LiveData<List<StackRoom>> getAllStackValues() {
        return mAllStackValues;
    }

    LiveData<List<QueueRoom>> getAllQueueValues() {
        return mAllQueueValues;
    }

    LiveData<List<SingleLinkedListRoom>> getAllSingleLinkedListValues() {
        return mAllSingleListValues;
    }

    LiveData<List<DoubleLinkedListRoom>> getAllDoubleLinkedListValues() {
        return mAllDoubleListValues;
    }

    LiveData<List<BinarySearchTreeRoom>> getAllBinarySearchTreeValues() {
        return mAllBSTValues;
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


    void append(SingleLinkedListRoom singleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.append(singleListVal);
        });
    }

    void prepend(SingleLinkedListRoom singleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.prepend(singleListVal);
        });
    }


    void insertAt(SingleLinkedListRoom singleListVal, int position) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.insertAt(singleListVal, position);
        });
    }

    public void insertAt(SingleLinkedListRoom singleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.insertAt(singleListVal);
        });
    }

    /*
    void update(SingleLinkedListRoom singleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.updateTwo(singleListVal);
        });
    }

     */


    void append(DoubleLinkedListRoom doubleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.append(doubleListVal);
        });
    }

    void prepend(DoubleLinkedListRoom doubleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.prepend(doubleListVal);
        });
    }


    void insertAt(DoubleLinkedListRoom doubleListVal, int position) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.insertAt(doubleListVal, position);
        });
    }

    public void insertAt(DoubleLinkedListRoom doubleListVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.insertAt(doubleListVal);
        });
    }

    void insert(BinarySearchTreeRoom bstVal) {
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mBSTDao.insert(bstVal);
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

    void delete(SingleLinkedListRoom singleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.delete(singleListVal);
        });
    }

    void delete(DoubleLinkedListRoom doubleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.delete(doubleListVal);
        });
    }


    void delete(BinarySearchTreeRoom bstVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mBSTDao.delete(bstVal);
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

    void deleteByIDSingleListFirst(int singleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.deleteByIDFirst(singleListVal);
            mSingleListDao.decrementPositionColumn();
        });
    }

    void deleteByIDSingleListLast(int singleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.deleteByIDLast(singleListVal);
            mSingleListDao.setPositionDecrement();
        });
    }

    void deleteByIDSingleListAt(int singleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.deleteByIDAt(singleListVal);
            //mSingleListDao.setPositionDecrement(); //TODO: check
        });
    }


    void deleteByIDDoubleListFirst(int doubleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.deleteByIDFirst(doubleListVal);
            mDoubleListDao.decrementPositionColumn();
        });
    }

    void deleteByIDDoubleListLast(int doubleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.deleteByIDLast(doubleListVal);
            mDoubleListDao.setPositionDecrement();
        });
    }

    void deleteByIDDoubleListAt(int doubleListVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.deleteByIDAt(doubleListVal);
            //mSingleListDao.setPositionDecrement(); //TODO: check
        });
    }

    void deleteByIDBinarySearchTree(int bstVal){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mBSTDao.deleteByID(bstVal);
        });
    }

    void deleteAllStack(){
        DatadoraDatabase.databaseWriteExecutor.execute(mStackDao::deleteAllStackDBEntries);
    }

    void deleteAllQueue(){
        DatadoraDatabase.databaseWriteExecutor.execute(mQueueDao::deleteAllQueueDBEntries);
    }

    void deleteAllSingleLinkedList(){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mSingleListDao.deleteAllSingleLinkedListDBEntries();
            mSingleListDao.resetPosition();
        });
    }

    void deleteAllDoubleLinkedList(){
        DatadoraDatabase.databaseWriteExecutor.execute(() -> {
            mDoubleListDao.deleteAllDoubleLinkedListDBEntries();
            mDoubleListDao.resetPosition();
        });
    }

    void deleteAllBinarySearchTree(){
        DatadoraDatabase.databaseWriteExecutor.execute(mBSTDao::deleteAllBinarySearchTreeDBEntries);
    }



}
