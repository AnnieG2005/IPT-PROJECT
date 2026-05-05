package com.shruti.lofo.data.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("phone")
    private String phone;

    public RegisterRequest(String email, String password, String full_name, String phone){
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.phone = phone;
    }

}
