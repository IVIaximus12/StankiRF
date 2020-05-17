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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stankirf.model.machine.AdapterRecyclerViewMachinesSearch;
import com.example.stankirf.model.machine.Machine;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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
    private DatabaseReference dbRefUserDate;
    private FirebaseAuth mAuth;
    private SearchView searchView;
    private FavoriteFragment favoriteFragment;


    // public methods

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.searchMachines:
                setTitle(R.string.search_bar);
                clearFragmentManager();
                closeLeftMenu();
                break;
            case R.id.favoriteMachines:
                setTitle(R.string.favoriteMachines);
                toFavoriteFragment();
                closeLeftMenu();
                break;
            case R.id.appInfo:
                break;
            case R.id.exit:
                logout();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeLeftMenu();
        } else if (!isEmptyFragmentManager()) {
            setTitle(R.string.search_bar);
            clearFragmentManager();
            navigationView.getMenu().findItem(R.id.searchMachines).setChecked(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_item);


        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getText(R.string.search_bar));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterRecyclerViewMachines.getFilter().filter(query);
                if (!isEmptyFragmentManager() && (getSupportFragmentManager().findFragmentByTag("FavoriteFragment") != null)) {
                    favoriteFragment.getAdapterRecyclerViewMachines().getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRecyclerViewMachines.getFilter().filter(newText);
                if (!isEmptyFragmentManager() && (getSupportFragmentManager().findFragmentByTag("FavoriteFragment") != null)) {
                    favoriteFragment.getAdapterRecyclerViewMachines().getFilter().filter(newText);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle(R.string.search_bar);


        initLists();
        initDatabase();
        setFbListener();
        initRecyclerViewMachines();
        initMenuView();
    }


    // private methods

    private void initLists() {
        listMachines = new ArrayList<>();
        listId = new ArrayList<>();
    }

    private void initRecyclerViewMachines() {

        recyclerViewMachines = findViewById(R.id.recyclerViewUserActivity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMachines.setLayoutManager(layoutManager);

        adapterRecyclerViewMachines = new AdapterRecyclerViewMachinesSearch(listMachines, listId, dbRefUserDate);
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
        dbRefUserDate.keepSynced(true);
    }

    private void setFbListener() {
        dbRefMachine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, Machine>> generic = new GenericTypeIndicator<HashMap<String, Machine>>() {
                    };
                    HashMap<String, Machine> mapMachines = dataSnapshot.getValue(generic);

                    ArrayList<Machine> machines = new ArrayList<>(mapMachines.values());
                    Set<String> id = mapMachines.keySet();
                    String[] idArray = id.toArray(new String[id.size()]);

                    for (int i = 0; i < idArray.length; i++) {
                        machines.get(i).setId(idArray[i]);
                    }

                    listMachines.clear();
                    listMachines.addAll(machines);
                    adapterRecyclerViewMachines.upDateViews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbRefUserDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, String>> generic = new GenericTypeIndicator<HashMap<String, String>>() {
                    };
                    HashMap<String, String> map = dataSnapshot.getValue(generic);

                    ArrayList<String> strings = new ArrayList<>(map.values());
                    listId.clear();
                    listId.addAll(strings);
                    adapterRecyclerViewMachines.upDateViews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initMenuView() {
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

    private void closeLeftMenu() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void toFavoriteFragment() {

        favoriteFragment = new FavoriteFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContent, favoriteFragment, "FavoriteFragment")
                .commit();
    }

    private void clearFragmentManager() {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private boolean isEmptyFragmentManager() {
        return new ArrayList<>(getSupportFragmentManager().getFragments()).isEmpty();
    }

    private void logout() {

        mAuth.signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
