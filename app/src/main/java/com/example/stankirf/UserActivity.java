package com.example.stankirf;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.model.machine.AdapterRecyclerViewMachinesSearch;
import com.example.stankirf.model.machine.Machine;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // private attributes

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerViewMachines;
    private AdapterRecyclerViewMachinesSearch adapterRecyclerViewMachines;

    private ArrayList<Machine> listMachines;
    private ArrayList<String> listId;

    private DatabaseReference dbRefMachine;
    private FirebaseAuth mAuth;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.appInfo:
                break;
            case R.id.favoriteMachines:
                break;
            case R.id.exit:
                logout();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawerUser);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_item);


        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getText(R.string.searchItemText));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterRecyclerViewMachines.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRecyclerViewMachines.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        initLists();
        initDatabase();
        setFbListener();
        initRecyclerViewMachines();
        initMenuView();
    }

    // private methods

    private void initLists(){
        listMachines = new ArrayList<>();
        listId = new ArrayList<>();
    }

    private void initRecyclerViewMachines(){

        recyclerViewMachines = findViewById(R.id.recyclerViewUserActivity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMachines.setLayoutManager(layoutManager);

        adapterRecyclerViewMachines = new AdapterRecyclerViewMachinesSearch(listMachines, listId);
        recyclerViewMachines.setAdapter(adapterRecyclerViewMachines);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMachines.getContext(),
                layoutManager.getOrientation());
        recyclerViewMachines.addItemDecoration(dividerItemDecoration);
    }

    private void initDatabase(){

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        dbRefMachine = FirebaseDatabase.getInstance().getReference("machines");
    }

    private void setFbListener(){
        dbRefMachine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, Machine>> generic = new GenericTypeIndicator<HashMap<String, Machine>>() {};
                HashMap<String, Machine> passportsAndNames = dataSnapshot.getValue(generic);

                ArrayList<Machine> machines = new ArrayList<>(passportsAndNames.values());
                Set<String> id = passportsAndNames.keySet();
                String[] idArray = id.toArray(new String[id.size()]);

                for (int i = 0; i < idArray.length; i++){
                    machines.get(i).setId(idArray[i]);
                }

                listMachines.clear();
                listMachines.addAll(machines);
                adapterRecyclerViewMachines.upDateViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initMenuView(){
        drawerLayout = findViewById(R.id.drawerUser);
        toolbar = findViewById(R.id.toolbarUser);
        navigationView = findViewById(R.id.navigationViewUser);

        setSupportActionBar(toolbar);

        final RelativeLayout content = findViewById(R.id.contentUser);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void logout(){

        mAuth.signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
