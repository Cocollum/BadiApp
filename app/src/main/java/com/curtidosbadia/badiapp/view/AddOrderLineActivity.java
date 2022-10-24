package com.curtidosbadia.badiapp.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityOrderLineBinding;
import com.curtidosbadia.badiapp.databinding.DialogSearchebleSpinnerBinding;
import com.curtidosbadia.badiapp.model.Client;
import com.curtidosbadia.badiapp.model.Color;
import com.curtidosbadia.badiapp.model.Order;
import com.curtidosbadia.badiapp.model.OrderLine;
import com.curtidosbadia.badiapp.model.Product;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.OrderViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AddOrderLineActivity extends AppController {
    OrderViewModel viewModel;
    private Session session;
    String index;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        session = new Session(this);

        viewModel = new OrderViewModel();

        String orderline = getIntent().getStringExtra("orderline");
        index = getIntent().getStringExtra("index");

        if(orderline == null || orderline.equals("")) {
            viewModel.setOrderLine(new OrderLine());
        }else{
            try {
                viewModel.setOrderLine(new OrderLine(orderline));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ActivityOrderLineBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_order_line);
        binding.setViewModel(viewModel);

        viewModel.didAddOrderLine.observe(this, value -> {
            if(value == true){
                Intent intent = new Intent();
                if(index != null && !index.equals("")) {
                    intent.putExtra("index", index);
                }
                intent.putExtra("line", viewModel.getOrderLine().toJSON());
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        viewModel.didClickOpenDialog.observe(this, (dialog_type) -> {
            if(!dialog_type.equals("")) {
                Dialog dialog = new Dialog(AddOrderLineActivity.this);
                DialogSearchebleSpinnerBinding dialog_binding = DialogSearchebleSpinnerBinding.inflate(getLayoutInflater().from(AddOrderLineActivity.this));
                dialog.setContentView(dialog_binding.getRoot());
                dialog.show();

                switch (dialog_type) {
                    case "article":
                        fetchArticles(dialog);
                    case "color":
                        fetchColors(dialog);
                    case "thickness":
                        fetchThickness(dialog);
                }
            }
        });
    }

    private void fetchArticles(Dialog dialog){
        try {
            RequestParams params = new RequestParams();
            params.add("token", session.getUser().getUser_token());
            BadiaApi.post("getProducts", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getProducts", response);

                    ArrayList<Product> list = new ArrayList<>();

                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            list.add(new Product(arr.getString(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    EditText editText = dialog.findViewById(R.id.edit_text);
                    ListView listView = dialog.findViewById(R.id.list_view);

                    ArrayAdapter<Product> adapter = new ArrayAdapter<>(AddOrderLineActivity.this, android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            OrderLine orderLine = viewModel.getOrderLine();
                            orderLine.setArticle(adapter.getItem(position).toString());

                            viewModel.setOrderLine(orderLine);
                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("getProducts", response);
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void fetchColors(Dialog dialog){
        try {
            RequestParams params = new RequestParams();
            params.add("token", session.getUser().getUser_token());
            BadiaApi.post("getColors", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getColors", response);

                    ArrayList<Color> list = new ArrayList<>();

                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            list.add(new Color(arr.getString(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    EditText editText = dialog.findViewById(R.id.edit_text);
                    ListView listView = dialog.findViewById(R.id.list_view);

                    ArrayAdapter<Color> adapter = new ArrayAdapter<>(AddOrderLineActivity.this, android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            OrderLine orderLine = viewModel.getOrderLine();
                            orderLine.setColor_name(adapter.getItem(position).getName());
                            orderLine.setColor_code(Integer.toString(adapter.getItem(position).getId()));

                            viewModel.setOrderLine(orderLine);
                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("getColors", response);
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void fetchThickness(Dialog dialog){
        try {
            RequestParams params = new RequestParams();
            params.add("token", session.getUser().getUser_token());
            BadiaApi.post("getWeights", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getWeights", response);

                    ArrayList<String> list = new ArrayList<>();

                    try {
                        JSONArray arr = new JSONArray(response);
                        for (int i = 0; i < arr.length(); i++) {
                            list.add(arr.getJSONObject(i).getString("weight_name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    EditText editText = dialog.findViewById(R.id.edit_text);
                    ListView listView = dialog.findViewById(R.id.list_view);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddOrderLineActivity.this, android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // when item selected from list
                            // set selected item on textView
                            OrderLine orderLine = viewModel.getOrderLine();
                            orderLine.setThickness(adapter.getItem(position));

                            viewModel.setOrderLine(orderLine);
                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("getWeights", response);
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
