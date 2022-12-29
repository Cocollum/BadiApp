package com.curtidosbadia.badiapp.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.databinding.HomeFragmentBinding;
import com.curtidosbadia.badiapp.databinding.OrdersFragmentBinding;
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

import cz.msebera.android.httpclient.Header;

public class OrdersContentFragment extends Fragment {
    private static final int ORDER_ACTIVITY = 1;
    private static final String TEXT = "text";
    private Session session;
    private OrderViewModel viewModel;

    public static OrdersContentFragment newInstance(String text) {
        OrdersContentFragment frag = new OrdersContentFragment();

        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new OrderViewModel();

        OrdersFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.orders_fragment, container, false);
        View layout = binding.getRoot();

        binding.setViewModel(viewModel);
        session = new Session(getContext());
        try {
            fetchOrders();
        } catch (JSONException exc) {
            exc.printStackTrace();
        }

        viewModel.didClickAddOrderButton.observe(getViewLifecycleOwner(), value -> {
            if (value == true) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("type", "addOrder");
                viewModel.didClickAddOrderButton.setValue(false);
                startActivityForResult(intent, ORDER_ACTIVITY);
            }
        });

        viewModel.didClickEditOrder.observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("type", "editOrder");
                intent.putExtra("order", value.getOrder_id());
                viewModel.didClickEditOrder.setValue(null);
                startActivityForResult(intent, ORDER_ACTIVITY);
            }
        });

        GestureDetector gestureDetector = new GestureDetector(getContext(), new MyGestureListener());

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        return layout;
    }

    private void fetchOrders() throws JSONException {
        RequestParams params = new RequestParams();
        params.add("token", session.getUser().getUser_token());

        BadiaApi.post("getOrders", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.d("Order get", response);
                try {
                    ArrayList<Order> orders = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        orders.add(new Order(array.getString(i)));
                    }

                    viewModel.setOrder_list(orders);
                } catch (JSONException exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Log.d("Order get", response);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ORDER_ACTIVITY: {
                if (resultCode == RESULT_OK) {
                    try {
                        fetchOrders();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 80;
        private static final int SWIPE_VELOCITY_THRESHOLD = 80;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
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

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
        viewModel.didSwipeLeft.observe(getViewLifecycleOwner(), index -> {
            if (index != null) {
                Order order = viewModel.getOrder_list().get(index);
                order.deleteOrderLine(index);
            }
        });
    }
}
