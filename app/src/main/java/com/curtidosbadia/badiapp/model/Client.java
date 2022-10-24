package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    private String client_id;
    private String client_name;
    private String client_email;
    private String client_phone;
    private String client_nif;
    private Address invoice_address;
    private Address send_address;

    public Client() {

        this.client_id = "";
        this.client_name = "";
        this.client_email = "";
        this.client_phone = "";
        this.client_nif = "";
        this.invoice_address = new Address();
        this.send_address = new Address();
    }

    public Client(String json_string) throws JSONException {
        JSONObject obj = new JSONObject(json_string);

        this.client_id = obj.getString("client_id");
        this.client_name = obj.getString("client_name");
        this.client_email = obj.getString("client_email");
        this.client_phone = obj.getString("client_phone");
        this.client_nif = obj.getString("client_nif");
        if(obj.has("invoice_address")) {
            this.invoice_address = new Address(obj.getString("invoice_address"));
        }
        if(obj.has("send_address")) {
            this.send_address = new Address(obj.getString("send_address"));
        }
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("client_id", this.client_id);
            obj.put("client_name", this.client_name);
            obj.put("client_email", this.client_email);
            obj.put("client_phone", this.client_phone);
            obj.put("client_nif", this.client_nif);
            obj.put("invoice_address", this.invoice_address.toJSON());
            obj.put("send_address", this.send_address.toJSON());

            return obj.toString();
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getClient_nif() {
        return client_nif;
    }

    public void setClient_nif(String client_nif) {
        this.client_nif = client_nif;
    }

    public Address getInvoice_address() {
        return invoice_address;
    }

    public void setInvoice_address(Address invoice_address) {
        this.invoice_address = invoice_address;
    }

    public Address getSend_address() {
        return send_address;
    }

    public void setSend_address(Address send_address) {
        this.send_address = send_address;
    }

    @Override
    public String toString(){
        return client_name;
    }
}
