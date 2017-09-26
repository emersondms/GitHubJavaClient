package com.emersondms.githubchallenge.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.emersondms.githubchallenge.R;
import com.emersondms.githubchallenge.view.fragment.RepositoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ToolbarTitleSetter {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        displayRepositoriesFragment();
    }

    public void setupToolbar() {
        resetToolbar();
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    resetToolbar();
                }
            }
        });
    }

    public void resetToolbar() {
        if (toolbar != null) {
            setToolbarTitle(getString(R.string.toolbar_title));
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void displayRepositoriesFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.recyclerContainer, new RepositoryFragment())
            .commit();
    }
}
