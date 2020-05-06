package com.example.stankirf.model.machine;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerViewMachines extends RecyclerView.Adapter<AdapterRecyclerViewMachines.NumberViewHolder> {


    // private attributes

    private ArrayList<Machine> listMachines;
    private ArrayList<String> listId;

    // public constructor

    public AdapterRecyclerViewMachines(ArrayList<Machine> listMachines, ArrayList<String> listId){

        this.listMachines = listMachines;
        this.listId = listId;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_view_user_activity, parent, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {

        Context context = holder.itemView.getContext();
        holder.modelName.setText(context.getResources().getString(R.string.modelNameText).
                concat(listMachines.get(position).getModelName()));
        holder.producingCountry.setText(context.getResources().getString(R.string.producingCountryText).
                concat(listMachines.get(position).getProducingCountry()));
        holder.producer.setText(context.getResources().getString(R.string.producerText).
                concat(listMachines.get(position).getProducer()));
        holder.machineGroup.setText(context.getResources().getString(R.string.machineGroupText).
                concat(listMachines.get(position).getMachineGroup()));
    }

    @Override
    public int getItemCount() {
        return listMachines.size();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circlePicZaglushka;
        TextView modelName, producingCountry, producer, machineGroup;
        ImageButton buttonAddFavorite;


        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);

            initViews(itemView);
            setOnClickListeners();
        }

        private void setOnClickListeners(){
            buttonAddFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String currentIdMachines = listMachines.get(getAdapterPosition()).getId();

                    if (!listId.contains(currentIdMachines)){
                        buttonAddFavorite.setImageDrawable(v.getResources().getDrawable((R.drawable.icons8_star_on)));
                        listId.add(currentIdMachines);
                    } else {
                        buttonAddFavorite.setImageDrawable(v.getResources().getDrawable((R.drawable.icons8_star_off)));
                        listId.remove(currentIdMachines);
                    }

                }
            });
        }

        private void initViews(@NonNull View itemView) {

            circlePicZaglushka = itemView.findViewById(R.id.picZaglushka);
            modelName = itemView.findViewById(R.id.modelName);
            producingCountry = itemView.findViewById(R.id.producingCountry);
            producer = itemView.findViewById(R.id.producer);
            machineGroup = itemView.findViewById(R.id.machineGroup);
            buttonAddFavorite = itemView.findViewById(R.id.buttonAddFavorite);
        }
    }
}
