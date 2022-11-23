package com.curtidosbadia.badiapp.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.view.GestureDetectorCompat;
import androidx.databinding.DataBindingUtil;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityOrderBinding;
import com.curtidosbadia.badiapp.databinding.DialogSearchebleSpinnerBinding;
import com.curtidosbadia.badiapp.model.Address;
import com.curtidosbadia.badiapp.model.Client;
import com.curtidosbadia.badiapp.model.Order;
import com.curtidosbadia.badiapp.model.OrderLine;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.utils.Session;
import com.curtidosbadia.badiapp.viewmodels.OrderViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cz.msebera.android.httpclient.Header;

public class OrderActivity extends AppController {
    private static final int ADD_ORDER_LINE_ACTIVITY = 1;
    private static final int EDIT_ORDER_LINE_ACTIVITY = 2;
    private OrderViewModel viewModel;
    private Session session;
    private GestureDetector mDetector;
    View.OnTouchListener gestureListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");

        session = new Session(this);

        viewModel = new OrderViewModel();
        if(type.equals("addOrder")) {
            try {
                RequestParams params = new RequestParams();
                params.add("token", session.getUser().getUser_token());

                BadiaApi.post("getNextOrderRef", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        Log.d("getNextOrderRef", response);

                        Order order = new Order();
                        order.setOrder_ref(response);
                        viewModel.setOrder(order);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = new String(responseBody);
                        Log.d("getNextOrderRef", response);
                        viewModel.setOrder(new Order());
                    }
                });
            }catch(JSONException e){
                e.printStackTrace();
            }
        }else{
            String order = getIntent().getStringExtra("order");
            RequestParams params = new RequestParams();
            params.add("order_id", order);

            BadiaApi.post("getOrder", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getOrder", response);
                    try{
                        Order order = new Order(response);
                        viewModel.setOrder(order);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

        //gesture detector -- probar
        mDetector = new GestureDetector(this, new GestureListener1());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };
        ActivityOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_order);

        binding.setViewModel(viewModel);

        viewModel.didClickAddOrderLineButton.observe(this, value -> {
            if(value == true){
                Intent intent = new Intent(getApplicationContext(), AddOrderLineActivity.class);
                viewModel.didClickAddOrderLineButton.setValue(false);
                startActivityForResult(intent, ADD_ORDER_LINE_ACTIVITY);
            }
        });

        viewModel.didSaveOrder.observe(this, value -> {
            if(value == true){
                try {
                    Order order = viewModel.getOrder();

                    RequestParams params = new RequestParams();
                    params.add("token", session.getUser().getUser_token());
                    params.add("order", order.toJSON());

                    BadiaApi.post(type, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String response = new String(responseBody);
                            Log.d("Add Order", response);
                            try{
                                if(response.equals("ok")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);

                                    builder.setMessage("The order was added correctly")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    setResult(RESULT_OK, intent);
                                                    finish();
                                                }
                                            });

                                    builder.show();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        viewModel.didClickOpenDialog.observe(this, (dialog_type) -> {
            if(!dialog_type.equals("")) {
                Dialog dialog = new Dialog(OrderActivity.this);
                DialogSearchebleSpinnerBinding dialog_binding = DialogSearchebleSpinnerBinding.inflate(getLayoutInflater().from(OrderActivity.this));
                dialog.setContentView(dialog_binding.getRoot());
                dialog.show();

                fetchClients(dialog);
            }
        });

        AtomicInteger num_changes = new AtomicInteger();
        viewModel.didClientNameChanged.observe(this, (client_name) -> {
            if(!client_name.equals("") && type.equals("addOrder")){
                showAddressList(client_name, "invoice");
            }else if(!client_name.equals("") && type.equals("editOrder") && num_changes.get() > 0){
                showAddressList(client_name, "send");
            }
            num_changes.getAndIncrement();
        });

        viewModel.didClickEditLine.observe(this, (index) -> {
            if(!index.equals("")) {
                Intent intent = new Intent(getApplicationContext(), AddOrderLineActivity.class);
                intent.putExtra("index", index.toString());
                intent.putExtra("orderline", viewModel.getOrder().getOrderLine(index).toJSON());
                viewModel.didClickAddOrderLineButton.setValue(false);
                startActivityForResult(intent, EDIT_ORDER_LINE_ACTIVITY);

            }
        });
        viewModel.didClickDeleteLine.observe(this, (index) -> {
            if(!index.equals("")) {
                Order order = viewModel.getOrder();
                order.deleteOrderLine(index);
                viewModel.setOrder(order);
            }
        });

        viewModel.didClickDateInput.observe(this, (value) -> {
            if(value == true){
                viewModel.didClickDateInput.postValue(false);

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = year + "-" + String.format("%1$2s", (month+1)).replace(' ', '0') + "-" + day;
                        Order order = viewModel.getOrder();
                        order.setOrder_date(selectedDate);
                        viewModel.setOrder(order);
                    }
                });
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void showAddressList(String client_name, String address_type){

        try {
            RequestParams params = new RequestParams();
            params.add("token",session.getUser().getUser_token());
            params.add("client_name",client_name);
            params.add("address_type",address_type);
            BadiaApi.post("getClientAddressesInfo", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getClientAddressesInfo", response);
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setTitle(address_type.equals("send") ? "Shipping address" : "Invoice address");
                    final ArrayAdapter<Address> arrayAdapter = new ArrayAdapter<>(OrderActivity.this, android.R.layout.select_dialog_singlechoice);

                    try{
                        JSONArray arr = new JSONArray(response);
                        for(int i = 0; i < arr.length(); i++){
                            Address address = new Address(arr.getString(i));
                            arrayAdapter.add(address);
                        }

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Address address = arrayAdapter.getItem(i);
                                Order order = viewModel.getOrder();
                                if(address_type.equals("invoice")) {
                                    order.getClient().setInvoice_address(address);
                                    showAddressList(client_name, "send");
                                }else{
                                    order.getClient().setSend_address(address);
                                }

                                viewModel.setOrder(order);

                                dialogInterface.dismiss();
                            }
                        });

                        builder.show();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("getClientAddressesInfo", response);
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void fetchClients(Dialog dialog){
        try {
            RequestParams params = new RequestParams();
            params.add("token", session.getUser().getUser_token());
            BadiaApi.post("getClients", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Log.d("getClients", response);

                    ArrayList<Client> list = new ArrayList<>();

                    try {
                        JSONArray arr = new JSONArray(response);
                        for(int i = 0; i < arr.length(); i++){
                            list.add(new Client(arr.getString(i)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    EditText editText = dialog.findViewById(R.id.edit_text);
                    ListView listView = dialog.findViewById(R.id.list_view);

                    ArrayAdapter<Client> adapter=new ArrayAdapter<>(OrderActivity.this, android.R.layout.simple_list_item_1, list);
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
                            Order order = viewModel.getOrder();
                            order.setClient(adapter.getItem(position));

                            viewModel.setOrder(order);
                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    Log.d("getClients", response);
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_ORDER_LINE_ACTIVITY: {
                if (resultCode == RESULT_OK) {
                    try {
                        Order order = viewModel.getOrder();
                        order.addOrderLine(new OrderLine(data.getStringExtra("line")));

                        viewModel.setOrder(order);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case EDIT_ORDER_LINE_ACTIVITY: {
                if (resultCode == RESULT_OK) {
                    try {
                        Order order = viewModel.getOrder();
                        order.editOrderLine(Integer.parseInt(data.getStringExtra("index")), new OrderLine(data.getStringExtra("line")));

                        viewModel.setOrder(order);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    private final class GestureListener1 extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 80;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX <= 0) {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
    public void onSwipeLeft() {
        viewModel.didSwipeLeft.observe( OrderActivity.this, (order_id) -> {
            if(order_id != null) {
                Order order = viewModel.getOrder();
                order.deleteOrderSwipe(order_id);
                viewModel.setOrder(order);
            }
        });
    }
}


