package com.shruti.lofo.data.model;

public class ProfileUpdateRequest {
    private String full_name;

    private String phone;

    public ProfileUpdateRequest (String full_name, String phone){
        this.full_name = full_name;
        this.phone = phone;
    }
}

// put serialization if needed for different var names