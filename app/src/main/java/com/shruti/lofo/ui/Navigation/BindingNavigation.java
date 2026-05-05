package com.shruti.lofo.ui.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.shruti.lofo.R;
import com.shruti.lofo.auth.Login;
import com.shruti.lofo.databinding.ActivityBindNavBinding;

import com.shruti.lofo.utils.DrawerBlurHelper;
import com.shruti.lofo.utils.SessionManager;

public class BindingNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityBindNavBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerBlurHelper drawerBlurHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBindNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(findViewById(R.id.toolbar));

        // ── Navigation setup ──────────────────────────────────────────────────
        Fragment navHostFragment = getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment instanceof NavHostFragment) {
            navController = ((NavHostFragment) navHostFragment).getNavController();
        } else {
            return;
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard,
                R.id.lost_drawer,
                R.id.found_drawer,
                R.id.navigation_help
        )
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navDrawer.setNavigationItemSelectedListener(this);

        View headerView = binding.navDrawer.getHeaderView(0);
        if (headerView != null) {
            DrawerManipulator.updateDrawerHeader(this, headerView);
        }

        // ── Blur helper setup ─────────────────────────────────────────────────
        // Pass the view you want blurred (your main content container)
        // and the overlay view you added in XML
        View mainContent = binding.navHostFragment; // or binding.contentContainer — your main content view
        View overlay = binding.blurOverlay;

        drawerBlurHelper = new DrawerBlurHelper(mainContent, overlay);

        // ── Drawer listener ───────────────────────────────────────────────────
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                drawerBlurHelper.onDrawerSlide(slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerBlurHelper.onDrawerOpened();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerBlurHelper.onDrawerClosed();
            }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Intercept the Logout click
        if (item.getItemId() == R.id.logout_drawer) {

            // Wipe the digital wallet unconditionally
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.logoutUser();

            Toast.makeText(this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();

            // Send back to Log in and wipe the back-stack
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

        if (handled) {
            binding.drawerLayout.closeDrawers();
        }

        return handled;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}