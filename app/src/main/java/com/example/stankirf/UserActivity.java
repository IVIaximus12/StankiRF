package com.example.stankirf;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.model.machine.AdapterRecyclerViewMachinesSearch;
import com.example.stankirf.model.machine.Machine;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // private attributes

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerViewMachines;
    private AdapterRecyclerViewMachinesSearch adapterRecyclerViewMachines;

    private ArrayList<Machine> listMachines;
    private ArrayList<String> listId;

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

        initMenuView();
        initRecyclerViewMachines();
    }


    // private methods

    private void initRecyclerViewMachines(){
        listMachines = new ArrayList<>();
        listId = new ArrayList<>();

        recyclerViewMachines = findViewById(R.id.recyclerViewUserActivity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMachines.setLayoutManager(layoutManager);

        initListMachine(listMachines, listId);

        adapterRecyclerViewMachines = new AdapterRecyclerViewMachinesSearch(listMachines, listId);
        recyclerViewMachines.setAdapter(adapterRecyclerViewMachines);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMachines.getContext(),
                layoutManager.getOrientation());
        recyclerViewMachines.addItemDecoration(dividerItemDecoration);
    }

    private void initListMachine(ArrayList<Machine> listMachines, ArrayList<String> listId){

        Collections.addAll(listMachines,
                new Machine("11111", "B620", "Италия",
                        "Biglia & C. S.p.A.", "Токарный станок"),
                new Machine("22222", "ГС526УЦ с УЦИ", "Беларусь",
                        "Гомельский завод станочных узлов ГЗСУ", "Токарный станок"),
                new Machine("33333", "TC25x500", "Китайская Народная Республика",
                        "Ningbo Haitian Precison Machinery Co., Ltd.", "Токарный станок"),
                new Machine("44444", "Рысь", "Россия",
                        "ООО Интермаш", "Токарный станок"),
                new Machine("55555", "ТЦ-480НФ3 исп №3", "Россия",
                        "ТАЙФУН - станки металлообрабатывающие", "Токарный станок"),
                new Machine("66666", "CF36", "Китайская Народная Республика",
                        "JSTOMI", "Токарный станок"),
                new Machine("77777", "16К30Ф31", "Россия",
                        "Рязанский станкостроительный завод (РСЗ)", "Токарный станок"));


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

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
