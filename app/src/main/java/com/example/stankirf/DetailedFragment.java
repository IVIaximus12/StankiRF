package com.example.stankirf;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.stankirf.model.machine.Machine;
import com.example.stankirf.model.machine.lathe.CncLathe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailedFragment extends Fragment {

    private View view;
    private String idMachine;
    private ImageView picMachine;
    private TextView modelName;
    private TextView producingCountry;
    private TextView producer;
    private TextView machineGroup;
    private TextView machineType;

    private Machine machine;
    private DatabaseReference refMachine;
    private DatabaseReference refMachineDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detailed_fragment, container, false);

        initViews();
        initDatabase();
        return view;
    }

    public DetailedFragment(String idMachine) {
        this.idMachine = idMachine;
    }

    private void initViews() {
        picMachine = view.findViewById(R.id.picMachine);
        modelName = view.findViewById(R.id.modelName);
        producingCountry = view.findViewById(R.id.producingCountry);
        producer = view.findViewById(R.id.producer);
        machineGroup = view.findViewById(R.id.machineGroup);
        machineType = view.findViewById(R.id.machineType);
    }

    private void initDatabase() {
        final String URLShort = "machines";
        final String URLDetail = "machines_details";

        refMachine = MyDatabaseUtil.getDatabase().getReference(URLShort);
        refMachineDetails = MyDatabaseUtil.getDatabase().getReference(URLDetail);
        getMachine();
    }

    private void getMachine() {
        refMachine.child(idMachine).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machine = dataSnapshot.getValue(Machine.class);

                addingShortDataToScrollView();
                getDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDetails() {
        switch (machine.getMachineGroup()) {
            case "Токарный станок":
                switch (machine.getMachineType()) {
                    case "Токарный станок с ЧПУ":
                        DatabaseReference ref = refMachineDetails;
                        Query myQuery = ref.child("lathe").child("cnc_lathe").orderByChild("id").equalTo(idMachine);
                        myQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CncLathe cncLathe = new CncLathe();
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    cncLathe = postSnapshot.getValue(CncLathe.class);
                                    if (cncLathe.getId().equals(idMachine)) break;
                                }

                                addingDetailedDataToScrollView(cncLathe);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                }
        }
    }

    private void addingShortDataToScrollView() {
        String url = machine.getUrlImage();
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.proba);
        Glide.with(view)
                .load(url)
                .apply(options)
                .into(picMachine);

        modelName.setText(getString(R.string.modelNameText)
                .concat(machine.getModelName()));
        producingCountry.setText(getString(R.string.producingCountryText)
                .concat(machine.getProducingCountry()));
        producer.setText(getString(R.string.producerText)
                .concat(machine.getProducer()));
        machineGroup.setText(getString(R.string.machineGroupText)
                .concat(machine.getMachineGroup()));
        machineType.setText(getString(R.string.machineTypeText)
                .concat(machine.getMachineType()));
    }

    private void addingDetailedDataToScrollView(CncLathe cncLathe) {
        TextView text = view.findViewById(R.id.titleFirst);
        text.setText(getString(R.string.cncLatheTitle1));

        text = view.findViewById(R.id.titleSecond);
        text.setText(getString(R.string.cncLatheTitle2));

        text = view.findViewById(R.id.titleThird);
        text.setText(getString(R.string.cncLatheTitle3));

        text = view.findViewById(R.id.titleFourth);
        text.setText(getString(R.string.cncLatheTitle4));

        text = view.findViewById(R.id.titleFifth);
        text.setText(getString(R.string.cncLatheTitle5));

        addTextToTextView((TextView) view.findViewById(R.id.text1of1), getString(R.string.cncSystem), cncLathe.getCncSystem());
        addTextToTextView((TextView) view.findViewById(R.id.text2of1), getString(R.string.bed), cncLathe.getBed());
        addTextToTextView((TextView) view.findViewById(R.id.text1of2), getString(R.string.maximumDiameterRotation), cncLathe.getMaximumDiameterRotation());
        addTextToTextView((TextView) view.findViewById(R.id.text2of2), getString(R.string.maximumTurningDiameter), cncLathe.getMaximumTurningDiameter());
        addTextToTextView((TextView) view.findViewById(R.id.text3of2), getString(R.string.maximumWorkpieceLength), cncLathe.getMaximumWorkpieceLength());
        addTextToTextView((TextView) view.findViewById(R.id.text1of3), getString(R.string.maximumSpindleSpeed), cncLathe.getMaximumSpindleSpeed());
        addTextToTextView((TextView) view.findViewById(R.id.text2of3), getString(R.string.spindleMotorPower), cncLathe.getSpindleMotorPower());
        addTextToTextView((TextView) view.findViewById(R.id.text3of3), getString(R.string.counterSpindle), cncLathe.getCounterSpindle());
        addTextToTextView((TextView) view.findViewById(R.id.text1of4), getString(R.string.tool), cncLathe.getTool());
        addTextToTextView((TextView) view.findViewById(R.id.text2of4), getString(R.string.toolNumber), cncLathe.getToolNumber());
        addTextToTextView((TextView) view.findViewById(R.id.text1of5), getString(R.string.totalMachinePower), cncLathe.getTotalMachinePower());
        addTextToTextView((TextView) view.findViewById(R.id.text2of5), getString(R.string.length), cncLathe.getLength());
        addTextToTextView((TextView) view.findViewById(R.id.text3of5), getString(R.string.machineWidth), cncLathe.getMachineWidth());
        addTextToTextView((TextView) view.findViewById(R.id.text4of5), getString(R.string.height), cncLathe.getHeight());
        addTextToTextView((TextView) view.findViewById(R.id.text5of5), getString(R.string.weight), cncLathe.getWeight());
    }

    private void addTextToTextView(TextView text, String tag, String data) {
        if (data.isEmpty()) {
            text.setVisibility(View.GONE);
        } else {
            text.setText(tag.concat(data));
        }
    }
}
