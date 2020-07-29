package com.example.nushfrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaSession2Service;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements Login.LoginListener, HomeActivity.HomeActivityListener, Pay.Paylistener, Scan.Scanlistener{

    public final Money buget = new Money(1500);;
    private HomeActivity homeActivity;
    private History history;
    private Pay pay;
    private Scan scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeActivity = new HomeActivity();
        history = new History();
        pay = new Pay();
        scan = new Scan();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showLoginDialog();
            buget.setSum(1500);
        }
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navi);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  homeActivity).commit();
    }
    private void showLoginDialog() {
        Login login = new Login();
        login.setCancelable(false);
        login.show(getSupportFragmentManager(), "login");
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        System.out.println(buget.getUser());
        System.out.println(login.username);
    }

    @Override
    public void applyText(String username) {
        if (username.equals("")){
            Toast.makeText(this, "One character minimum", Toast.LENGTH_SHORT).show();
        }
        else
        {
            buget.setUser(username);
            Toast.makeText(this, "Salut, " + username, Toast.LENGTH_SHORT).show();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            homeActivity.updateBani(buget);
                            selectedFragment = homeActivity;
                            break;
                        case R.id.nav_history:
                            selectedFragment = history;
                            break;
                        case R.id.nav_pay:
                            pay.updateBani(buget);
                            selectedFragment = pay;
                            break;
                        case R.id.nav_scan:
                            scan.updateBani(buget);
                            selectedFragment = scan;
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
    @Override
    public void onInputHomeSent(Money input){
        buget.setSum(input.getSum());
    }

    @Override
    public void onInputScanSent(Money input) {
        buget.setSum(input.getSum());
    }

    @Override
    public void onInputPaySent(Money input) {
        buget.setSum(input.getSum());
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        String UnameValue = buget.getUser();
        editor.putString("user", UnameValue);
        editor.putBoolean("firstStart", false);
        editor.commit();
    }

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences("prefs", Context.MODE_PRIVATE);
//
//        // Get value
        buget.setUser(settings.getString("user", "Unknown"));
    }


    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }
    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

}


