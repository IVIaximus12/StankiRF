package com.example.stankirf;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.model.machine.AdapterRecyclerViewMachinesSearch;
import com.example.stankirf.model.machine.Machine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteFragment extends Fragment {


    // private attributes

    private RecyclerView recyclerViewMachines;
    private AdapterRecyclerViewMachinesSearch adapterRecyclerViewMachines;
    private View view;

    private DatabaseReference dbRefMachine;
    private DatabaseReference dbRefUserDate;
    private FirebaseAuth mAuth;

    private ArrayList<Machine> listMachines;
    private ArrayList<String> listId;

    // public methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite_fragment, container, false);

        initLists();
        initDatabase();
        setFbUserDateListener();
        initRecyclerViewMachines();
        return view;
    }

// private methods

    private void initLists() {
        listMachines = new ArrayList<>();
        listId = new ArrayList<>();
    }

    private void initRecyclerViewMachines() {

        recyclerViewMachines = view.findViewById(R.id.recyclerViewFavoriteFragment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewMachines.setLayoutManager(layoutManager);

        adapterRecyclerViewMachines = new AdapterRecyclerViewMachinesSearch(listMachines, listId, dbRefUserDate, true);
        recyclerViewMachines.setAdapter(adapterRecyclerViewMachines);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMachines.getContext(),
                layoutManager.getOrientation());
        recyclerViewMachines.addItemDecoration(dividerItemDecoration);
    }

    private void initDatabase() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        dbRefMachine = MyDatabaseUtil.getDatabase().getReference("machines");
        dbRefUserDate = MyDatabaseUtil.getDatabase().getReference("userInfo").child("favorite").child(currentUser.getUid());
    }

    private void setFbUserDateListener() {
        dbRefUserDate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, String>> generic = new GenericTypeIndicator<HashMap<String, String>>() {
                    };
                    HashMap<String, String> map = dataSnapshot.getValue(generic);

                    ArrayList<String> strings = new ArrayList<>(map.values());


                    listId.clear();
                    listId.addAll(strings);
                    setFbMachineListener();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFbMachineListener() {

        for (String ref : listId) {
            dbRefMachine.child(ref).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        GenericTypeIndicator<Machine> generic = new GenericTypeIndicator<Machine>() {
                        };
                        Machine machine = dataSnapshot.getValue(generic);

                        int counter = 0;
                        for (int i = 0; i < listMachines.size(); i++) {

                            if (machine.getId() != null && listMachines.get(i).getId().equals(machine.getId())){
                                listMachines.set(i, machine);
                                counter++;
                            }
                        }
                        if (counter == 0) {
                            listMachines.add(machine);
                        }
                        adapterRecyclerViewMachines.upDateViews();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
