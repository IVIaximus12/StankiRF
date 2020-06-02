package com.example.stankirf;

import android.os.Bundle;
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
import com.example.stankirf.model.machine.lathe.CncLathe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalogsFragment extends Fragment {


    // private attributes

    private View view;
    private String idMachine;
    private Machine machine;
    private Machine machineThis;
    private CncLathe cncLatheThis;

    private DatabaseReference refMachine;
    private DatabaseReference refMachineDetails;
    private DatabaseReference dbRefUserDate;

    private ArrayList<Machine> analogsList;
    private ArrayList<String> listIdFavorites;

    private AdapterRecyclerViewMachinesSearch adapterRecyclerViewMachines;
    private RecyclerView recyclerViewMachines;


    // public methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite_fragment, container, false);

        initLists();
        initDatabase();
        initRecyclerViewMachines();
        return view;
    }


    // public constructors

    public AnalogsFragment(String idMachine) {
        this.idMachine = idMachine;
    }


    // private methods

    private void initLists() {
        listIdFavorites = new ArrayList<>();
        analogsList = new ArrayList<>();
    }

    private void initRecyclerViewMachines() {

        recyclerViewMachines = view.findViewById(R.id.recyclerViewFavoriteFragment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewMachines.setLayoutManager(layoutManager);

        adapterRecyclerViewMachines = new AdapterRecyclerViewMachinesSearch(analogsList, listIdFavorites, dbRefUserDate);
        recyclerViewMachines.setAdapter(adapterRecyclerViewMachines);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMachines.getContext(),
                layoutManager.getOrientation());
        recyclerViewMachines.addItemDecoration(dividerItemDecoration);
    }

    private void initDatabase() {
        final String URLShort = "machines";
        final String URLDetail = "machines_details";

        refMachine = MyDatabaseUtil.getDatabase().getReference(URLShort);
        refMachineDetails = MyDatabaseUtil.getDatabase().getReference(URLDetail);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        dbRefUserDate = MyDatabaseUtil.getDatabase().getReference("userInfo").child("favorite").child(currentUser.getUid());

        getListFavorites();
        getMachineFirst();
    }

    private void getMachineFirst() {
        refMachine.child(idMachine).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machineThis = dataSnapshot.getValue(Machine.class);
                getMachineFirstDetailed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMachineFirstDetailed() {
        switch (machineThis.getMachineGroup()) {
            case "Токарный станок":
                switch (machineThis.getMachineType()) {
                    case "Токарный станок с ЧПУ":
                        DatabaseReference ref = refMachineDetails;
                        Query myQuery = ref.child("lathe").child("cnc_lathe").orderByChild("id").equalTo(idMachine);
                        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    CncLathe cncLathe = postSnapshot.getValue(CncLathe.class);
                                    if (cncLathe.getId().equals(idMachine)) {
                                        cncLatheThis = cncLathe;
                                        break;
                                    }
                                }

                                getAnalogs();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                }
        }
    }

    private void getAnalogs() {
        DatabaseReference ref = refMachineDetails;
        Query myQuery = ref.child("lathe").child("cnc_lathe").orderByChild("tool").equalTo(cncLatheThis.getTool());
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    CncLathe analog = postSnapshot.getValue(CncLathe.class);
                    if (compareCncLathe(analog))
                        getShortCncLatheData(analog.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean compareCncLathe(CncLathe analog) {
        return !analog.getId().equals(cncLatheThis.getId())
                && Integer.parseInt(analog.getMaximumTurningDiameter().trim()) >= Integer.parseInt(cncLatheThis.getMaximumTurningDiameter().trim())
                && Integer.parseInt(analog.getMaximumWorkpieceLength().trim()) >= Integer.parseInt(cncLatheThis.getMaximumWorkpieceLength().trim())
                && Integer.parseInt(analog.getMaximumSpindleSpeed().trim()) >= Integer.parseInt(cncLatheThis.getMaximumSpindleSpeed().trim())
                && analog.getCounterSpindle().equals(cncLatheThis.getCounterSpindle())
                && analog.getTool().equals(cncLatheThis.getTool());
    }

    private void getShortCncLatheData(final String idAnalog) {
        refMachine.child(idAnalog).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machine = dataSnapshot.getValue(Machine.class);
                machine.setId(idAnalog);
                if (machine.getProducingCountry().equals("Россия")) {
                    analogsList.add(machine);
                    adapterRecyclerViewMachines.upDateViews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getListFavorites() {
        dbRefUserDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, String>> generic = new GenericTypeIndicator<HashMap<String, String>>() {
                    };
                    HashMap<String, String> map = dataSnapshot.getValue(generic);

                    ArrayList<String> strings = new ArrayList<>(map.values());

                    if (listIdNeedToUpdate(strings, listIdFavorites)) {
                        listIdFavorites.clear();
                        listIdFavorites.addAll(strings);
                        adapterRecyclerViewMachines.upDateViews();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean listIdNeedToUpdate(ArrayList<String> strings, ArrayList<String> listId) {

        if (strings.size() != listId.size())
            return true;

        int count = 0;
        for (int i = 0; i < strings.size(); i++) {
            if (strings.contains(listId.get(i)))
                count++;
        }

        return count != strings.size();
    }
}
