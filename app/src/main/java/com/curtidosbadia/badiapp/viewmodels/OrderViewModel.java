package com.curtidosbadia.badiapp.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.curtidosbadia.badiapp.BR;
import com.curtidosbadia.badiapp.model.Order;
import com.curtidosbadia.badiapp.model.OrderLine;

import java.util.ArrayList;

public class OrderViewModel extends BaseObservable {
    private ArrayList<Order> order_list = new ArrayList<Order>();
    private Order order;
    private OrderLine orderLine;

    public MutableLiveData<Boolean> didClickAddOrderButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickAddOrderLineButton = new MutableLiveData<>();
    public MutableLiveData<Boolean> didAddOrderLine = new MutableLiveData<>();
    public MutableLiveData<Boolean> didSaveOrder = new MutableLiveData<>();
    public MutableLiveData<Order> didClickEditOrder = new MutableLiveData<>();
    public MutableLiveData<String> didClickOpenDialog = new MutableLiveData<>();
    public MutableLiveData<String> didClientNameChanged = new MutableLiveData<>();
    public MutableLiveData<Integer> didClickEditLine = new MutableLiveData<>();
    public MutableLiveData<Integer> didClickDeleteLine = new MutableLiveData<>();
    public MutableLiveData<Boolean> didClickDateInput = new MutableLiveData<>();
    public MutableLiveData<Integer> didSwipeLeft = new MutableLiveData<>();


    public OrderViewModel() {

    }

    @Bindable
    public ArrayList<Order> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<Order> order_list) {
        this.order_list = order_list;
        notifyPropertyChanged(BR.order_list);
    }

    @Bindable
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        notifyPropertyChanged(BR.order);
    }

    @Bindable
    public OrderLine getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
        notifyPropertyChanged(BR.orderLine);
    }

    public void onClickAddOrderButton(){ this.didClickAddOrderButton.setValue(true); }
    public void onClickAddOrderLineButton(){ this.didClickAddOrderLineButton.setValue(true); }

    public void onAddOrderLine(){
        this.didAddOrderLine.setValue(true);
    }

    public void onClickSaveOrder(){
        this.didSaveOrder.setValue(true);
    }

    public void onClickEditOrder(Order order){ this.didClickEditOrder.setValue(order); }

    public void onClickOpenDialog(String type){ this.didClickOpenDialog.setValue(type); }
    public void onClickEditLine(Integer index){  this.didClickEditLine.setValue(index); }
    public void onClickDeleteLine(Integer index){  this.didClickDeleteLine.setValue(index);
    }
    public void onSwipeLeft(Integer order_id){ this.didSwipeLeft.setValue(order_id);}

    public void onClientNameChanged(CharSequence s, int start, int before, int count){
        this.didClientNameChanged.setValue(s.toString());
    }

    public void onClickDateInput(){
        this.didClickDateInput.setValue(true);
    }
}
