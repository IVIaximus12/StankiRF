package com.example.stankirf.model.machine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecyclerViewMachinesSearch
        extends RecyclerView.Adapter<AdapterRecyclerViewMachinesSearch.NumberViewHolder> implements Filterable {


    // private attributes

    private ArrayList<Machine> listMachines;
    private ArrayList<Machine> listMachinesFiltered;
    private ArrayList<String> listId;


    // public constructor

    public AdapterRecyclerViewMachinesSearch(ArrayList<Machine> listMachines, ArrayList<String> listId){

        this.listMachines = listMachines;
        this.listId = listId;
        this.listMachinesFiltered = new ArrayList<>(listMachines);
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
        final Machine machine = listMachinesFiltered.get(position);
        holder.modelName.setText(context.getResources().getString(R.string.modelNameText).
                concat(machine.getModelName()));
        holder.producingCountry.setText(context.getResources().getString(R.string.producingCountryText).
                concat(machine.getProducingCountry()));
        holder.producer.setText(context.getResources().getString(R.string.producerText).
                concat(machine.getProducer()));
        holder.machineGroup.setText(context.getResources().getString(R.string.machineGroupText).
                concat(machine.getMachineGroup()));

        if (listId.contains(machine.getId())){
            holder.buttonAddFavorite.setImageDrawable(context.getResources().getDrawable((R.drawable.icons8_star_on)));
        } else {
            holder.buttonAddFavorite.setImageDrawable(context.getResources().getDrawable((R.drawable.icons8_star_off)));
        }

    }

    @Override
    public int getItemCount() {
        return listMachinesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase().trim();

                if (charString.isEmpty()) {
                    listMachinesFiltered.clear();
                    listMachinesFiltered.addAll(listMachines);
                } else {
                    ArrayList<Machine> filteredList = new ArrayList<>();

                    for (Machine item : listMachines) {
                        if (item.getModelName().toLowerCase().contains(charString)) {
                            filteredList.add(item);
                        }
                    }
                    listMachinesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listMachinesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listMachinesFiltered = (ArrayList<Machine>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void upDateViews(){

        listMachinesFiltered.clear();
        listMachinesFiltered.addAll(listMachines);
        notifyDataSetChanged();
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

                    String currentIdMachines = listMachinesFiltered.get(getAdapterPosition()).getId();

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
