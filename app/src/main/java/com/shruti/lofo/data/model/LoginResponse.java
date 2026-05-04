package com.shruti.lofo.data.model;


public class LoginResponse {
    private String message;
    private String token;
    private User user;

    public String getMessage() {
        return message;
    }
    public String getToken() {
        return token;
    }

    public User getUser() { return user; }


    public static class User {
        private int user_id;
        private String full_name;
        private String email;
        private String phone;

        public int getId() { return user_id; }
        public String getName(){return full_name;}
        public String getEmail(){return email;}
        public String getPhone(){return phone;}
    }
}
