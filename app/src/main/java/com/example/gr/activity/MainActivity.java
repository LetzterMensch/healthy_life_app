package com.example.gr.activity;

import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.adapter.MainViewPagerAdapter;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDevice;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gr.databinding.ActivityMainBinding;

import java.util.List;

// Hỗ trợ chuyển tiếp giữa các fragments
public class MainActivity extends BaseActivity {

    private ActivityMainBinding mActivityMainBinding;
    private DeviceManager deviceManager;
    private List<GBDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        deviceManager = ((ControllerApplication) getApplication()).getDeviceManager();
        deviceList = deviceManager.getDevices();

        mActivityMainBinding.viewpager2.setUserInputEnabled(false);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mActivityMainBinding.viewpager2.setAdapter(mainViewPagerAdapter);
        mActivityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;

                    case 1:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_diary).setChecked(true);
                        break;

                    case 2:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_exercise).setChecked(true);
                        break;

                    case 3:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_sleep).setChecked(true);
                        break;

                    case 4:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        break;
                }
            }
        });

        mActivityMainBinding.navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.navigation_dashboard){
                mActivityMainBinding.viewpager2.setCurrentItem(0);
            }else if ( id == R.id.navigation_diary){
                mActivityMainBinding.viewpager2.setCurrentItem(1);
            }else if (id == R.id.navigation_exercise){
                mActivityMainBinding.viewpager2.setCurrentItem(2);
            }else if (id == R.id.navigation_sleep){
                mActivityMainBinding.viewpager2.setCurrentItem(3);
            }else if (id == R.id.navigation_profile){
                mActivityMainBinding.viewpager2.setCurrentItem(4);
            }
            return true;
        });
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);

    }

//    @Override
//    public void onBackPressed() {
//        super.getOnBackPressedDispatcher().onBackPressed();
//        showConfirmExitApp();
//    }

    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finish())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }
    public void setToolBar(boolean isHome, String title) {
        if (isHome) {
            mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.GONE);
            return;
        }
        mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityMainBinding.toolbar.tvTitle.setText(title);
    }
}