package com.example.inhacsecapstone.drugs.allDrug;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMedicineList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMedicineList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";
    private AppDatabase db;
    private AllDrugListAdapter adapter;
    private ArrayList<Medicine> medis;
    private ArrayList<Takes> takes;


    // TODO: Rename and change types of parameters
    private String mParam1;

    public AllMedicineList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type "ALLDRUG" OR "DAYDRUG" OR "RECOGRESULT".
     * @return A new instance of fragment MedicineList.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String type) {
        AllMedicineList fragment = new AllMedicineList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_medicine_list, container, false);
        db = AppDatabase.getDataBase(getActivity().getApplicationContext());
        medis = db.getAllMedicine();
        takes = db.getAllTakes();
        RecyclerView recyclerView = view.findViewById(R.id.allMedicineView);
        adapter = new AllDrugListAdapter(getActivity(), medis, takes);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDecorator(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    public void notifyDataSetChanged(){
        adapter.refresh();
    }
}


