package com.example.stankirf.model.machine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.stankirf.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RemoveFavDialogFragment extends DialogFragment {

    private String currentIdMachines;
    private ArrayList<String> listId;
    private ArrayList<Machine> listMachinesFiltered;
    private AdapterRecyclerViewMachinesSearch adapterRecyclerView;
    private DatabaseReference dbRefUserDate;

    private View view;
    private Button btnNo, btnYes;

    public RemoveFavDialogFragment(ArrayList<String> listId, ArrayList<Machine> listMachinesFiltered, String currentIdMachines, AdapterRecyclerViewMachinesSearch adapterRecyclerView, DatabaseReference dbRefUserDate) {

        this.listId = listId;
        this.currentIdMachines = currentIdMachines;
        this.adapterRecyclerView = adapterRecyclerView;
        this.dbRefUserDate = dbRefUserDate;
        this.listMachinesFiltered = listMachinesFiltered;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.dialog_fragment_remove_fav, container, false);

        initViews();
        setClickListeners();

        return view;
    }

    private void initViews() {

        btnNo = view.findViewById(R.id.btnNo);
        btnYes = view.findViewById(R.id.btnYes);
    }

    private void setClickListeners() {

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listId.remove(currentIdMachines);
                for (int i = 0; i < listMachinesFiltered.size(); i++) {
                    if (listMachinesFiltered.get(i).getId().equals(currentIdMachines)) {
                        listMachinesFiltered.remove(i);
                    }
                }
                dbRefUserDate.child(currentIdMachines).removeValue();
                adapterRecyclerView.notifyDataSetChanged();
                dismiss();
            }
        });
    }
}
