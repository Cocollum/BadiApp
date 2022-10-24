package com.curtidosbadia.badiapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Order {
    private String order_id;
    private String order_ref;
    private String order_date;
    private String client_name;
    private Client client;
    private String incoterm;
    private String shipping_cost;
    private String shipping_forwarder;
    private String shipping_forwarder_type;
    private String shipping_account_number;
    private String observations;
    private String contact_id;
    private ArrayList<OrderLine> lines;
    private String status;

    public Order(String json_string) throws JSONException {
        JSONObject obj = new JSONObject(json_string);

        this.order_id = obj.getString("order_id");
        this.order_ref = obj.getString("order_ref");
        this.order_date = obj.getString("order_date");
        this.client_name = obj.getString("client_name");
        this.client = obj.opt("client") != null ? new Client(obj.getString("client")) : null;
        this.incoterm = obj.optString("incoterm");
        this.shipping_cost = obj.optString("shipping_cost");
        this.shipping_forwarder = obj.optString("shipping_forwarder");
        this.shipping_forwarder_type = obj.optString("shipping_forwarder_type");
        this.shipping_account_number = obj.optString("shipping_account_number");
        this.observations = obj.optString("observations");
        this.contact_id = obj.optString("contact_id");
        if(obj.opt("lines") != null) {
            JSONArray array = new JSONArray(obj.getString("lines"));
            this.lines = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                lines.add(new OrderLine(array.optString(i)));
            }
        }

        this.status = obj.optString("status");
    }

    public Order() {

        this.order_id = "";
        this.order_ref = "";
        this.order_date = "";
        this.client_name = "";
        this.client = new Client();
        this.incoterm = "";
        this.shipping_cost = "";
        this.shipping_forwarder = "";
        this.shipping_forwarder_type = "";
        this.shipping_account_number = "";
        this.observations = "";
        this.contact_id = "";
        this.lines = new ArrayList<>();
        this.status = "";
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("order_id", this.order_id);
            obj.put("order_ref", this.order_ref);
            obj.put("order_date", this.order_date);
            obj.put("client_name", this.client_name);
            obj.put("client", this.client.toJSON());
            obj.put("incoterm", this.incoterm);
            obj.put("shipping_cost", this.shipping_cost);
            obj.put("shipping_forwarder", this.shipping_forwarder);
            obj.put("shipping_forwarder_type", this.shipping_forwarder_type);
            obj.put("shipping_account_number", this.shipping_account_number);

            JSONArray arr = new JSONArray();

            for(int i = 0; i < this.lines.size(); i++){
                arr.put(this.lines.get(i).toJSON());
            }
            obj.put("lines", arr.toString());
            obj.put("observations", this.observations);
            obj.put("contact_id", this.contact_id);
            obj.put("status", this.status);

            return obj.toString();
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_ref() {
        return order_ref;
    }

    public void setOrder_ref(String order_ref) {
        this.order_ref = order_ref;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getIncoterm() {
        return incoterm;
    }

    public void setIncoterm(String incoterm) {
        this.incoterm = incoterm;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getShipping_forwarder() {
        return shipping_forwarder;
    }

    public void setShipping_forwarder(String shipping_forwarder) {
        this.shipping_forwarder = shipping_forwarder;
    }

    public String getShipping_forwarder_type() {
        return shipping_forwarder_type;
    }

    public void setShipping_forwarder_type(String shipping_forwarder_type) {
        this.shipping_forwarder_type = shipping_forwarder_type;
    }

    public String getShipping_account_number() {
        return shipping_account_number;
    }

    public void setShipping_account_number(String shipping_account_number) {
        this.shipping_account_number = shipping_account_number;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public ArrayList<OrderLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<OrderLine> lines) {
        this.lines = lines;
    }

    public void addOrderLine(OrderLine line){
        this.lines.add(line);
    }
    public OrderLine getOrderLine(Integer index){
        return this.lines.get(index);
    }
    public void editOrderLine(Integer index, OrderLine line){
        this.getLines().set(index, line);
    }
    public void deleteOrderLine(Integer index){
        this.getLines().remove(this.getLines().get(index));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
