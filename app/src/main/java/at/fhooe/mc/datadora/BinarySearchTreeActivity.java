package at.fhooe.mc.datadora;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Vector;

import at.fhooe.mc.datadora.databinding.ActivityBinarySearchTreeBinding;
public class BinarySearchTreeActivity extends AppCompatActivity {

    private TextView mTextView;
    private Vector<Integer> mBST = new Vector<>();
    private ActivityBinarySearchTreeBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_search_tree);

        mTextView = (TextView) findViewById(R.id.text);
        // array eingeben sachen speichern

        // Enables Always-on
       }
}