package com.curtidosbadia.badiapp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityLoginBinding;
import com.curtidosbadia.badiapp.model.User;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.HomeViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppController {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        HomeViewModel viewModel = new HomeViewModel();

        BadiaApi.get("getAppSplashscreen", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("getAppSplashscreen",response);
                viewModel.setApp_splashscreen(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.setViewModel(viewModel);

        viewModel.didClickLogin.observe(this, (value) -> {
            if (value == true) {
                RequestParams params = new RequestParams();
                params.add("email", viewModel.getUsername());
                params.add("password", viewModel.getPassword());
                BadiaApi.post("login", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        Log.d("login",response);
                        try{
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("response").equals("ok")){
                                Session session = new Session(getApplicationContext());
                                session.setUser(new User(obj.getString("user")));
                                finish();
                            }
                        }catch (JSONException exc){
                            exc.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = new String(responseBody);

                        Log.d("Login response", response);

                    }
                });
                viewModel.didClickLogin.setValue(false);
            }
        });
    }
}
