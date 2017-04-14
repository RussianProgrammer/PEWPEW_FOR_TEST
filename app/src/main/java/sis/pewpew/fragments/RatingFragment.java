package sis.pewpew.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sis.pewpew.MainActivity;
import sis.pewpew.R;

public class RatingFragment extends Fragment {

    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.rating_fragment_name));
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
