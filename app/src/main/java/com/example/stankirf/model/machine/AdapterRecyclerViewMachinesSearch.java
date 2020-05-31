package com.example.stankirf.model.machine;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.stankirf.R;
import com.example.stankirf.SelectionActivity;
import com.example.stankirf.UserActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AdapterRecyclerViewMachinesSearch
        extends RecyclerView.Adapter<AdapterRecyclerViewMachinesSearch.NumberViewHolder> implements Filterable {


    // private attributes

    private ArrayList<Machine> listMachines;
    private ArrayList<Machine> listMachinesFiltered;
    private ArrayList<String> listId;
    private DatabaseReference dbRefUserDate;
    private AdapterRecyclerViewMachinesSearch adapterRecyclerView;
    private boolean isFavorite;
    private FragmentManager fragmentManager;


    // public constructor

    public AdapterRecyclerViewMachinesSearch(ArrayList<Machine> listMachines, ArrayList<String> listId,
                                             DatabaseReference dbRefUserDate){

        this.listMachines = listMachines;
        this.listId = listId;
        this.listMachinesFiltered = new ArrayList<>(listMachines);
        this.dbRefUserDate = dbRefUserDate;
        isFavorite = false;
    }

    public AdapterRecyclerViewMachinesSearch(ArrayList<Machine> listMachines, ArrayList<String> listId,
                                             DatabaseReference dbRefUserDate, FragmentManager fragmentManager){

        this.listMachines = listMachines;
        this.listId = listId;
        this.listMachinesFiltered = new ArrayList<>(listMachines);
        this.dbRefUserDate = dbRefUserDate;
        this.isFavorite = true;
        this.fragmentManager = fragmentManager;
        this.adapterRecyclerView = this;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_view_machines, parent, false);
        if (isFavorite) {
            view.setFocusable(true);
            view.setClickable(true);
        }

        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {

        View view = holder.itemView;
        final Machine machine = listMachinesFiltered.get(position);
        holder.modelName.setText(view.getResources().getString(R.string.modelNameText).
                concat(machine.getModelName()));
        holder.producingCountry.setText(view.getResources().getString(R.string.producingCountryText).
                concat(machine.getProducingCountry()));
        holder.producer.setText(view.getResources().getString(R.string.producerText).
                concat(machine.getProducer()));
        holder.machineGroup.setText(view.getResources().getString(R.string.machineGroupText).
                concat(machine.getMachineGroup()));

        if (true) {
            String url = machine.getUrlImage();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.proba);
            Glide.with(view)
                    .load(url)
                    .apply(options)
                    .into(holder.imageMachine);
        } else {
            Glide.with(view).clear(holder.imageMachine);
            holder.imageMachine.setImageDrawable(view.getResources().getDrawable((R.drawable.proba)));
        }


        if (isFavorite) {
            holder.buttonAddFavorite.setVisibility(View.GONE);
            holder.imageAddFavorite.setVisibility(View.GONE);
        }
        if (listId.contains(machine.getId())){
            holder.imageAddFavorite.setImageDrawable(view.getResources().getDrawable((R.drawable.icons8_star_on)));
        } else {
            holder.imageAddFavorite.setImageDrawable(view.getResources().getDrawable((R.drawable.icons8_star_off)));
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

    // public methods

    public void upDateViews(){

        listMachinesFiltered.clear();
        listMachinesFiltered.addAll(listMachines);
        notifyDataSetChanged();
    }

    // inner class of Holder

    class NumberViewHolder extends RecyclerView.ViewHolder {

        ImageView imageMachine, imageAddFavorite;
        TextView modelName, producingCountry, producer, machineGroup;
        Button buttonAddFavorite;


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
                        imageAddFavorite.setImageDrawable(v.getResources().getDrawable((R.drawable.icons8_star_on)));
                        listId.add(currentIdMachines);
                        dbRefUserDate.child(currentIdMachines).setValue(currentIdMachines);
                    } else {
                        imageAddFavorite.setImageDrawable(v.getResources().getDrawable((R.drawable.icons8_star_off)));
                        listId.remove(currentIdMachines);
                        dbRefUserDate.child(currentIdMachines).removeValue();
                    }

                }
            });

            if (isFavorite) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final String currentIdMachines = listMachinesFiltered.get(getAdapterPosition()).getId();
                        RemoveFavDialogFragment removeFavDialogFragment = new RemoveFavDialogFragment(listId, listMachinesFiltered, currentIdMachines, adapterRecyclerView, dbRefUserDate);
                        removeFavDialogFragment.show(fragmentManager, "removeDialogFragment");

                        Log.d("long click", "long click");
                        return true;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String currentIdMachines = listMachinesFiltered.get(getAdapterPosition()).getId();
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, SelectionActivity.class);
                        intent.putExtra("idMachine", currentIdMachines);
                        context.startActivity(intent);
                    }
                });
            }
        }

        private void initViews(@NonNull View itemView) {

            imageMachine = itemView.findViewById(R.id.picMachine);
            modelName = itemView.findViewById(R.id.modelName);
            producingCountry = itemView.findViewById(R.id.producingCountry);
            producer = itemView.findViewById(R.id.producer);
            machineGroup = itemView.findViewById(R.id.machineGroup);
            buttonAddFavorite = itemView.findViewById(R.id.buttonAddFavorite);
            imageAddFavorite = itemView.findViewById(R.id.imageAddFavorite);
        }
    }
}
