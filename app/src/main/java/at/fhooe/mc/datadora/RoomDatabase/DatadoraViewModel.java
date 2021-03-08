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


    /**
     * Constructor
     * @param application the application dependency
     */
    public DatadoraViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DatadoraRepository(application);
        mAllStackValues = mRepository.getAllStackValues(); //get the values from the repo class
        mAllQueueValues = mRepository.getAllQueueValues();
    }

    public LiveData<List<StackRoom>> getmAllStackValues(){
        return mAllStackValues;
    }
    public LiveData<List<QueueRoom>> getmAllQueueValues(){
        return mAllQueueValues;
    }

    public void insert(StackRoom stackVal){
        mRepository.insert(stackVal);
    }
    public void insert(QueueRoom queueVal){
        mRepository.insert(queueVal);
    }

    public void insertAllStack(List<StackRoom> stackValues) {
        mRepository.insertAllStack(stackValues);
    }

    public void insertAllQueue(List<QueueRoom> queueValues) {
        mRepository.insertAllQueue(queueValues);
    }

    public void delete(StackRoom stackVal){
        mRepository.delete(stackVal);
    }
    public void delete(QueueRoom queueVal){
        mRepository.delete(queueVal);
    }

    public void deleteByIDStack(int stackVal){
        mRepository.deleteByIDStack(stackVal);
    }
    public void deleteByIDQueue(int queueVal){
        mRepository.deleteByIDStack(queueVal);
    }

    public void deleteAllStackDatabaseEntries(){
        mRepository.deleteAllStack();
    }
    public void deleteAllQueueDatabaseEntries(){
        mRepository.deleteAllQueue();
    }

}
