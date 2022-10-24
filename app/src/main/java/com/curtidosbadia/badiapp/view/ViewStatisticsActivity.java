package com.curtidosbadia.badiapp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.curtidosbadia.badiapp.R;
import com.curtidosbadia.badiapp.app.AppController;
import com.curtidosbadia.badiapp.databinding.ActivityClientBinding;
import com.curtidosbadia.badiapp.databinding.ActivityViewStatisticsBinding;
import com.curtidosbadia.badiapp.utils.BadiaApi;
import com.curtidosbadia.badiapp.viewmodels.ClientViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class ViewStatisticsActivity extends AppController implements OnChartValueSelectedListener {
    ClientViewModel viewModel;
    JSONArray data;
    HashMap<String, JSONArray> current_data = new HashMap<>();
    String previous_key = "";
    String current_key = "products";
    String next_key = "colors";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String client_id = getIntent().getStringExtra("client");

        viewModel = new ClientViewModel();

        ActivityViewStatisticsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_statistics);

        binding.setViewModel(viewModel);

        viewModel.setStartDate("2021-01-01");
        viewModel.setEndDate("2021-06-30");

        viewModel.didClickSearchBetweenDates.observe(this, (value) -> {
            if(value){
                RequestParams params = new RequestParams();

                params.add("client", client_id);
                params.add("from", viewModel.getStartDate());
                params.add("to", viewModel.getEndDate());

                BadiaApi.post("getStatisticsForClient", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        Log.d("ViewStatistics", response);
                        try{
                            JSONArray obj = new JSONArray(response);
                            data = obj;
                            current_data.put(current_key,obj);

                            previous_key = "";
                            current_key = "products";
                            next_key = "colors";

                            viewModel.setData(obj);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        viewModel.didClickSearchBetweenDates.postValue(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = new String(responseBody);
                        Log.e("ViewStatistics", response);
                        viewModel.didClickSearchBetweenDates.postValue(false);
                    }
                });
            }
        });

        viewModel.didClickSearchBetweenDates.postValue(true);

        PieChart chart = binding.getRoot().findViewById(R.id.piechart);

        chart.setOnChartValueSelectedListener(this);

        viewModel.didClickPrevious.observe(this, (value) ->{
            if(value){
                try {
                    if(!previous_key.equals("")){
                        JSONArray arr = current_data.get(previous_key);
                        viewModel.setData(arr);
                    }else{
                        JSONArray arr = current_data.get(previous_key);
                        viewModel.setData(data);
                    }


                    next_key = current_key;
                    current_key = previous_key;
                    if (current_key.equals("categories")) {
                        previous_key = "colors";
                    }else if(current_key.equals("colors")){
                        previous_key = "products";
                    }else{
                        previous_key = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        if(!next_key.equals("")){
            try {
                JSONObject obj = current_data.get(current_key).getJSONObject((int) h.getX()).getJSONObject(next_key);
                Iterator<String> it = obj.keys();
                JSONArray arr = new JSONArray();
                while(it.hasNext()){
                    JSONObject item = obj.getJSONObject(it.next());
                    arr.put(item);
                }



                previous_key = current_key;
                current_key = next_key;
                if (current_key.equals("product")) {
                    next_key = "colors";
                }else if(current_key.equals("colors")){
                    next_key = "categories";
                }else{
                    next_key = "";
                }

                current_data.put(current_key,arr);

                viewModel.setData(arr);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }

        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }
}
