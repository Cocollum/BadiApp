package com.curtidosbadia.badiapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityClientBinding;
import com.curtidosbadia.badiapp.model.Address;
import com.curtidosbadia.badiapp.model.Client;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.viewmodels.ClientViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ClientActivity extends AppController {
    ClientViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String client_id = getIntent().getStringExtra("client");

        viewModel = new ClientViewModel();

        fetchClient(client_id);
        fetchAddress(client_id, "send");
        fetchAddress(client_id, "invoice");

        ActivityClientBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_client);

        binding.setViewModel(viewModel);

        viewModel.didClickViewStatistics.observe(this, (value) -> {
            if(value) {
                Intent intent = new Intent(getApplicationContext(), ViewStatisticsActivity.class);
                intent.putExtra("client", client_id);
                startActivity(intent);
            }
        });
    }

    private void fetchClient(String client_id){
        RequestParams params = new RequestParams();

        params.add("client", client_id);

        BadiaApi.post("getClient", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("get Client", response);
                try{
                    if(!response.equals("")){
                        Client client = new Client(response);
                        viewModel.setClient(client);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void fetchAddress(String client_id, String type){
        RequestParams params = new RequestParams();

        params.add("client_id", client_id);
        params.add("address_type", type);

        BadiaApi.post("getClientAddressesInfoById", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("get Address", response);
                try{
                    if(!response.equals("")){
                        JSONArray array = new JSONArray(response);
                        ArrayList<Address> addresses = new ArrayList<>();

                        for(int i = 0; i < array.length(); i++){
                            Address address = new Address(array.getString(i));
                            addresses.add(address);
                        }

                        viewModel.setAddresses(addresses, type);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
