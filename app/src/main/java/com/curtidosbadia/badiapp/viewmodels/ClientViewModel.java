package com.curtidosbadia.badiapp.viewmodels;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.curtidosbadia.badiapp.BR;
import com.curtidosbadia.badiapp.model.Address;
import com.curtidosbadia.badiapp.model.Client;
import com.curtidosbadia.badiapp.model.Order;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ClientViewModel extends BaseObservable {
    private ArrayList<Client> client_list = new ArrayList<>();
    public MutableLiveData<Client> didClickEditClient = new MutableLiveData<>();
    private Client client;
    private ArrayList<Address> send_address;
    private ArrayList<Address> invoice_address;
    private PieData data = new PieData();
    private ArrayList<Integer> colors = new ArrayList<>();
    public MutableLiveData<Boolean> didClickViewStatistics = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickSearchBetweenDates = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickPrevious = new MutableLiveData<>();

    private Date startDate;
    private Date endDate;

    public ClientViewModel(){
        colors.add(ColorTemplate.VORDIPLOM_COLORS[0]);
        colors.add(ColorTemplate.JOYFUL_COLORS[0]);
        colors.add(ColorTemplate.COLORFUL_COLORS[0]);
        colors.add(ColorTemplate.LIBERTY_COLORS[0]);
        colors.add(ColorTemplate.PASTEL_COLORS[0]);
    }

    @Bindable
    public ArrayList<Client> getClient_list() {
        return client_list;
    }

    public void setClient_list(ArrayList<Client> client_list) {
        this.client_list = client_list;
        notifyPropertyChanged(BR.client_list);
    }

    public void onClickEditClient(Client c){
        didClickEditClient.setValue(c);
    }

    @Bindable
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        notifyPropertyChanged(BR.client);
    }

    @Bindable
    public ArrayList<Address> getSend_address() {
        return send_address;
    }

    public void setSend_address(ArrayList<Address> send_address) {
        this.send_address = send_address;
        notifyPropertyChanged(BR.send_address);
    }

    @Bindable
    public ArrayList<Address> getInvoice_address() {
        return invoice_address;
    }

    public void setInvoice_address(ArrayList<Address> invoice_address) {
        this.invoice_address = invoice_address;
        notifyPropertyChanged(BR.invoice_address);
    }

    @Bindable
    public String getStartDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return startDate != null ? format.format(startDate) : "";
    }

    public void setStartDate(String startDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.startDate= format.parse(startDate);
            notifyPropertyChanged(BR.startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public String getEndDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return endDate != null ? format.format(endDate) : "";
    }

    public void setEndDate(String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.endDate = format.parse(endDate);
            notifyPropertyChanged(BR.endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public PieData getData() {
        return data;
    }

    public void setData(PieData data) {
        this.data = data;
        notifyPropertyChanged(BR.data);
    }

    public void setData(JSONArray arr) throws JSONException {
        PieData data = new PieData();
        PieDataSet dataset = null;
        ArrayList<PieEntry> list = new ArrayList<>();
        Double totals = 0.0;
        String label = "";


        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);

            totals += obj.getDouble("price");
        }

        for(int i = 0; i < arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            Log.d("setData", obj.toString());
            Log.d("setData", !obj.optString("article").equals("") ? obj.optString("article") : (!obj.optString("color").equals("") ? obj.optString("color") : obj.optString("category")));
            PieEntry entry = new PieEntry(Math.round(obj.getDouble("price")*100/totals), !obj.optString("article").equals("") ? obj.optString("article") : (!obj.optString("color").equals("") ? obj.optString("color") : obj.optString("category")));
            list.add(entry);

            label = !obj.optString("article").equals("") ? "Articles" : (!obj.optString("color").equals("") ? "Colors" : "Categories");
        }

        dataset = new PieDataSet(list, label);

        dataset.setSliceSpace(2);
        dataset.setColors(getColors());

        data.setDataSet(dataset);
        setData(data);
    }

    @BindingAdapter("setPieData")
    public static void setPieData(PieChart view, PieData data){
        if(data.getEntryCount() > 0) {
            view.setData(data);
            view.invalidate();
            view.notifyDataSetChanged();
        }
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Integer> color) {
        this.colors = colors;
    }

    public void setAddresses(ArrayList<Address> list, String type){
        if(type.equals("send")){
            this.setSend_address(list);
        }else{
            this.setInvoice_address(list);
        }
    }

    public void onClickViewStatistics(){ this.didClickViewStatistics.setValue(true); }

    public void onClickSearchBetweenDates(){ this.didClickSearchBetweenDates.setValue(true); }
    public void onClickPrevious(){ this.didClickPrevious.setValue(true); }
}
