package at.fhooe.mc.datadora.RoomDatabase;

import android.app.Application;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * This class provides data to the UI. It is the communicator between the repository and the UI.
 * There are no database calls from the ViewModel (this is all handled by the repository)
 */
public class DatadoraViewModel extends AndroidViewModel {


    /**
     * Repository instance is made in this class.
     */
    private final DatadoraRepository mRepository;

    /**
     * Using LiveData for changeable data that the UI will display.
     */
    private final LiveData<List<StackRoom>> mAllStackValues;
    private final LiveData<List<QueueRoom>> mAllQueueValues;
    private final LiveData<List<SingleLinkedListRoom>> mAllSingleLinkedListValues;
    private final LiveData<List<DoubleLinkedListRoom>> mAllDoubleLinkedListValues;
    private final LiveData<List<BinarySearchTreeRoom>> mAllBinarySearchTreeValues;


    /**
     * Constructor
     * @param application the application dependency
     */
    public DatadoraViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DatadoraRepository(application);
        mAllStackValues = mRepository.getAllStackValues(); //get the values from the repo class
        mAllQueueValues = mRepository.getAllQueueValues();
        mAllSingleLinkedListValues = mRepository.getAllSingleLinkedListValues();
        mAllDoubleLinkedListValues = mRepository.getAllDoubleLinkedListValues();
        mAllBinarySearchTreeValues = mRepository.getAllBinarySearchTreeValues();
    }

    public LiveData<List<StackRoom>> getmAllStackValues(){
        return mAllStackValues;
    }
    public LiveData<List<QueueRoom>> getmAllQueueValues(){
        return mAllQueueValues;
    }
    public LiveData<List<SingleLinkedListRoom>> getmAllSingleLinkedListValues(){
        return mAllSingleLinkedListValues;
    }
    public LiveData<List<DoubleLinkedListRoom>> getmAllDoubleLinkedListValues(){
        return mAllDoubleLinkedListValues;
    }
    public LiveData<List<BinarySearchTreeRoom>> getmAllBinarySearchTreeValuesValues(){
        return mAllBinarySearchTreeValues;
    }

    public void insert(StackRoom stackVal){
        mRepository.insert(stackVal);
    }
    public void insert(QueueRoom queueVal){
        mRepository.insert(queueVal);
    }

    public void insertLast(SingleLinkedListRoom singleListVal){
        mRepository.append(singleListVal);
    }

    public void insertFirst(SingleLinkedListRoom singleListVal){
        mRepository.prepend(singleListVal);
    }

    public void insertAt(SingleLinkedListRoom singleListVal) {
        mRepository.insertAt(singleListVal);
    }

    /*
    public void insertAt(SingleLinkedListRoom singleListVal, int position){
        mRepository.insertAt(singleListVal, position);
    }

     */



    /*
    public void update(SingleLinkedListRoom singleListVal){
        mRepository.update(singleListVal);
    }

     */

    public void insertLast(DoubleLinkedListRoom doubleListVal){
        mRepository.append(doubleListVal);
    }

    public void insertFirst(DoubleLinkedListRoom doubleListVal){
        mRepository.prepend(doubleListVal);
    }

    public void insertAt(DoubleLinkedListRoom doubleListVal) {
        mRepository.insertAt(doubleListVal);
    }

    public void insert(BinarySearchTreeRoom bstVal){
        mRepository.insert(bstVal);
    }


    public void delete(StackRoom stackVal){
        mRepository.delete(stackVal);
    }
    public void delete(QueueRoom queueVal){
        mRepository.delete(queueVal);
    }
    public void delete(SingleLinkedListRoom singleListVal){
        mRepository.delete(singleListVal);
    }
    public void delete(DoubleLinkedListRoom doubleListVal){
        mRepository.delete(doubleListVal);
    }
    public void delete(BinarySearchTreeRoom bstVal){
        mRepository.delete(bstVal);
    }

    public void deleteByIDStack(int stackVal){
        mRepository.deleteByIDStack(stackVal);
    }
    public void deleteByIDQueue(int queueVal){
        mRepository.deleteByIDQueue(queueVal);
    }

    public void deleteByIdSLLFirst(int singleLinkedListVal){
        mRepository.deleteByIDSingleListFirst(singleLinkedListVal);
    }

    public void deleteByIdSLLLast(int singleLinkedListVal){
        mRepository.deleteByIDSingleListLast(singleLinkedListVal);
    }

    public void deleteByIdSLLAt(int singleLinkedListVal){
        mRepository.deleteByIDSingleListAt(singleLinkedListVal);
    }


    public void deleteByIdDLLFirst(int doubleLinkedListVal){
        mRepository.deleteByIDDoubleListFirst(doubleLinkedListVal);
    }

    public void deleteByIdDLLLast(int doubleLinkedListVal){
        mRepository.deleteByIDDoubleListLast(doubleLinkedListVal);
    }

    public void deleteByIdDLLAt(int doubleLinkedListVal){
        mRepository.deleteByIDDoubleListAt(doubleLinkedListVal);
    }

    public void deleteByIdBST(int bstVal){
        mRepository.deleteByIDBinarySearchTree(bstVal);
    }


    public void deleteAllStackDatabaseEntries(){
        mRepository.deleteAllStack();
    }
    public void deleteAllQueueDatabaseEntries(){
        mRepository.deleteAllQueue();
    }
    public void deleteAllSingleLinkedListDatabaseEntries(){
        mRepository.deleteAllSingleLinkedList();
    }

    public void deleteAllDoubleLinkedListDatabaseEntries(){
        mRepository.deleteAllDoubleLinkedList();
    }

    public void deleteAllBSTDatabaseEntries(){
        mRepository.deleteAllBinarySearchTree();
    }


}
