package at.fhooe.mc.datadora.BinarySearchTree;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for the Binary Search Tree. Stores the four different fragments in a list.
 * The fragments are added in the setUpTabLayout() method in the BinarySearchTreeActivity
 */
public class BinarySearchTreeTabAdapter extends FragmentStateAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public BinarySearchTreeTabAdapter(@NonNull FragmentActivity _fragmentActivity) {
        super(_fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int _position) {
        return mFragmentList.get(_position);
    }

    public void addFragment (Fragment _fragment) {
        mFragmentList.add(_fragment);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}