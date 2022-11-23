package com.curtidosbadia.badiapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityMainBinding;
import com.curtidosbadia.badiapp.model.RelatedProduct;
import com.curtidosbadia.badiapp.model.User;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.BadiaWebApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.curtidosbadia.badiapp.BuildConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppController implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    Session session;
    HomeViewModel viewModel;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);

        try{
            user = session.getUser();
        }catch (Exception e){
            e.printStackTrace();
        }

        viewModel = new HomeViewModel();

        viewModel.setFragmentAdapter(getSupportFragmentManager());

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("Firebase Token", token);

                        RequestParams params = new RequestParams();

                        if(user != null) {

                            params.add("user_id", user.getUser_id());
                            params.add("token", token);

                            BadiaApi.post("registerFBMToken", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String response = new String(responseBody);
                                    Log.d("registerFBMToken", response);

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    String response = new String(responseBody);
                                    Log.d("registerFBMToken", response);

                                }
                            });
                        }
                    }
                });

        binding.setViewModel(viewModel);

        RequestParams params = new RequestParams();
        //params.add("action", "getRelatedProducts");
        BadiaApi.get("getRelatedProducts", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("getRelatedProducts", response);
                if(!response.equals("")){
                    try{
                        JSONArray arr = new JSONArray(response);
                        ArrayList<Fragment> list = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){
                            String obj = arr.getString(i);

                            list.add(RelatedProductFragment.newInstance(obj));
                        }


                        viewModel.setRelated_products(list);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.d("getRelatedProducts", response);

            }
        });

        try {
            if(session.getUser() == null){
               Intent intent = new Intent(this, LoginActivity.class);
               startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle("");
        }

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(toggle != null && drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            drawerLayout.addDrawerListener(this);
        }

        navigationView = findViewById(R.id.navigation_view);
        if(navigationView != null) {
            if(user != null) {
                if(!user.getUser_type().equals("commercial")) {
                    navigationView.inflateMenu(R.menu.main_menu_client);
                }else{
                    navigationView.inflateMenu(R.menu.main_menu);
                }
            }else{
                navigationView.inflateMenu(R.menu.main_menu);
            }

            navigationView.setNavigationItemSelectedListener(this);

            MenuItem menuItem = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(menuItem);
            menuItem.setChecked(true);

            TextView version = (TextView) navigationView.findViewById(R.id.version_text);
            version.setText("Version: " + BuildConfig.VERSION_NAME);

            View header = navigationView.getHeaderView(0);
            if(header != null && header.findViewById(R.id.header_title) != null) {
                header.findViewById(R.id.header_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new HomeContentFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();

                        drawerLayout.closeDrawer(GravityCompat.START);

                    }
                });
            }
        }

        viewModel.didClickLogout.observe(this, (value) -> {
            if(value == true){
                session.removeUser();
                viewModel.didClickLogout.postValue(false);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    public int checkNavigationMenuItem(){
        Menu menu = navigationView.getMenu();
        for(int i = 0; i < menu.size(); i++){
            if(menu.getItem(i).isChecked()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if(checkNavigationMenuItem() != 0){
                navigationView.getMenu().getItem(0).setChecked(true);
                Fragment fragment = new HomeContentFragment();
                //((HomeContentFragment) fragment).viewModel = viewModel;
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,fragment).commit();
            }else {
                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        int title;
        Intent browser = null;
        Fragment fragment = null;
        Uri uri = null;

        for(int i = 0; i < navigationView.getMenu().size(); i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        menuItem.setChecked(true);

        switch(menuItem.getItemId()){
            case R.id.dashboard:
                fragment = HomeContentFragment.newInstance("");
               // ((HomeContentFragment) fragment).viewModel = viewModel;
                break;
            case R.id.orders:
                fragment = OrdersContentFragment.newInstance("");
               // ((HomeContentFragment) fragment).viewModel = viewModel;
                break;
            case R.id.clients:
                fragment = ClientsContentFragment.newInstance("");
               // ((HomeContentFragment) fragment).viewModel = viewModel;
                break;
            /*case R.id.news:
                fragment = PostContentFragment.newInstance("");
                // ((HomeContentFragment) fragment).viewModel = viewModel;
                break;
            case R.id.events:
                fragment = EventContentFragment.newInstance("");
                // ((HomeContentFragment) fragment).viewModel = viewModel;
                break;*/
            default:
                throw new IllegalArgumentException("Menu option not implemented!");
        }

        if(fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.home_content, fragment).commit();

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if(browser != null){
            startActivity(browser);
        }

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v){

    }

    @Override
    public void onDrawerOpened(@NonNull View view){
    }

    @Override
    public void onDrawerClosed(@NonNull View view){
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }
}