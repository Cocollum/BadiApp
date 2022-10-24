package com.curtidosbadia.badiapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.databinding.ClientsFragmentBinding;
import com.curtidosbadia.badiapp.databinding.OrdersFragmentBinding;
import com.curtidosbadia.badiapp.model.Client;
import com.curtidosbadia.badiapp.model.Order;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.ClientViewModel;
import com.curtidosbadia.badiapp.viewmodels.OrderViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ClientsContentFragment extends Fragment {
    private static final String TEXT = "text";

    public static ClientsContentFragment newInstance(String text){
        ClientsContentFragment frag = new ClientsContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ClientViewModel viewModel = new ClientViewModel();


        ClientsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.clients_fragment, container, false);
        View layout = binding.getRoot();

        binding.setViewModel(viewModel);
        Session session = new Session(getContext());
        try {
            RequestParams params = new RequestParams();
            params.add("token", session.getUser().getUser_token());

            BadiaApi.post("getClients", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("Clients get", response);
                    try{
                        ArrayList<Client> clients = new ArrayList<>();
                        JSONArray array = new JSONArray(response);
                        for(int i = 0; i < array.length(); i++){
                            clients.add(new Client(array.getString(i)));
                        }

                        viewModel.setClient_list(clients);
                    }catch(JSONException exc){
                        exc.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("Order get", response);

                }
            });
        }catch(JSONException exc){
            exc.printStackTrace();
        }

        /*viewModel.didClickAddOrderButton.observe(this, value -> {
            if(value == true){
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("type", "addOrder");
                viewModel.didClickAddOrderButton.setValue(false);
                startActivity(intent);
            }
        });*/

        viewModel.didClickEditClient.observe(this, value -> {
            if(value != null){
                Intent intent = new Intent(getContext(), ClientActivity.class);
                intent.putExtra("type", "editClient");
                intent.putExtra("client", value.getClient_id());
                viewModel.didClickEditClient.setValue(null);
                startActivity(intent);
            }
        });

        return layout;
    }
}
