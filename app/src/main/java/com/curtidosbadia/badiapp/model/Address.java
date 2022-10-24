package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {
    private String address_id;
    private String address_name;
    private String address_social_reason;
    private String address_zip;
    private String address_address_1;
    private String address_city;
    private String address_province;
    private String address_country;
    private String address_vat_number;

    public Address() {

        this.address_id = "";
        this.address_name = "";
        this.address_social_reason = "";
        this.address_zip = "";
        this.address_address_1 = "";
        this.address_city = "";
        this.address_province = "";
        this.address_country = "";
        this.address_vat_number = "";
    }

    public Address(String json_string) throws JSONException {
        JSONObject obj = new JSONObject(json_string);

        this.address_id = obj.getString("address_id");
        this.address_name = obj.getString("address_name");
        this.address_social_reason = obj.getString("address_social_reason");
        this.address_zip = obj.getString("address_zip");
        this.address_address_1 = obj.getString("address_address_1");
        this.address_city = obj.getString("address_city");
        this.address_province = obj.getString("address_province");
        this.address_country = obj.getString("address_country");
        this.address_vat_number = obj.getString("address_vat_number");
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();

            obj.put("address_id", this.address_id);
            obj.put("address_name", this.address_name);
            obj.put("address_social_reason", this.address_social_reason);
            obj.put("address_zip", this.address_zip);
            obj.put("address_address_1", this.address_address_1);
            obj.put("address_city", this.address_city);
            obj.put("address_province", this.address_province);
            obj.put("address_country", this.address_country);
            obj.put("address_vat_number", this.address_vat_number);

            return obj.toString();
        }catch (JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_social_reason() {
        return address_social_reason;
    }

    public void setAddress_social_reason(String address_social_reason) {
        this.address_social_reason = address_social_reason;
    }

    public String getAddress_zip() {
        return address_zip;
    }

    public void setAddress_zip(String address_zip) {
        this.address_zip = address_zip;
    }

    public String getAddress_address_1() {
        return address_address_1;
    }

    public void setAddress_address_1(String address_address_1) {
        this.address_address_1 = address_address_1;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_province() {
        return address_province;
    }

    public void setAddress_province(String address_province) {
        this.address_province = address_province;
    }

    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public String getAddress_vat_number() {
        return address_vat_number;
    }

    public void setAddress_vat_number(String address_vat_number) {
        this.address_vat_number = address_vat_number;
    }

    @Override
    public String toString(){
        return address_address_1 + ", " + address_zip + ", " + address_city + " "+ address_province + " (" + address_country + ")";
    }
}
