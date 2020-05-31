package com.example.stankirf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SelectionActivity extends AppCompatActivity {

    String idMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        getIntentData();
        initToolbar();
        initViewPager();
    }


    // private methods

    private void getIntentData() {
        idMachine = getIntent().getStringExtra("idMachine");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarSelection);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        setBackArrowListener(toolbar);
    }

    private void setBackArrowListener(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewPagerSelection);
        viewPager.setAdapter(new ViewPagerAdapterSelection(this, idMachine));

        initTabLayout(viewPager);
    }

    private void initTabLayout(ViewPager2 viewPager) {
        TabLayout tabLayout = findViewById(R.id.tabLayoutSelection);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout,
                viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(R.string.characteristics);
                        break;
                    case 1:
                        tab.setText(R.string.analogs);
                }
            }
        });
        tabLayoutMediator.attach();
    }
}
