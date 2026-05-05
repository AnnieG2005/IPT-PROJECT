package com.shruti.lofo.api;

import com.shruti.lofo.data.model.Item;
import com.shruti.lofo.data.model.ItemRequest;
import com.shruti.lofo.data.model.LoginRequest;
import com.shruti.lofo.data.model.LoginResponse;
import com.shruti.lofo.data.model.ProfileUpdateRequest;
import com.shruti.lofo.data.model.RegisterRequest;
import com.shruti.lofo.data.model.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    /// +=========================================+
    //  | API CALLS IN CASE OF EMERGENCY CALL 911 |
    //  | WILL CONNECT TO BACKEND WHICH IS NODE.JS|
    /// +=========================================+



    /// +=======================================+
    //  |    API CALLS FOR ITEM MANAGEMENT      |
    /// +=======================================+
    //  |           ITEMS GET METHODS           |
    /// +=======================================+

    // GET : FETCHES ITEM DETAILS / INDIVIDUAL ITEMS
    // USED TO SHOW DETAILS IN EVERY ITEM
    @GET("/api/items/{id}")
    Call<Item> getItemById(@Path("id") int id);


    // GET : ALL USER OWN POSTS/ITEM
    // USED ON MY ITEMS PAGE
    @GET("/api/user-items")
    Call<List<Item>> getUserItems(@Query("type") String type);

    // GET : FETCHES ALL ITEMS BY TYPE IF ETHER LOST/FOUND
    @GET("/api/items")
    Call<List<Item>> getItems(@Query("type") String type);

    /// +=======================================+
    //  |        ITEMS POST METHODS             |
    /// +=======================================+

    // CREATE : CREATING NEW ITEM
    @POST("/api/items")
    Call<Item> createItem(@Body ItemRequest itemRequest);

    /// +=======================================+
    //  |        ITEMS PATCH METHODS            |
    /// +=======================================+

    @FormUrlEncoded
    @PATCH("/api/items/{id}/status")
    Call<Void> updateItemStatus(@Path("id") int id, @Field("status") String newStatus);


    /// +=======================================+
    //  |           ITEMS DELETE METHODS        |
    /// +=======================================+

    // DELETE : DELETE ITEM
    @DELETE("/api/items/{id}")
    Call<Void> deleteItem(@Path ("id") int id);



    /// +=======================================+
    //  |    API CALLS FOR USER MANAGEMENT      |
    /// +=======================================+
    //  |           USER POST METHODS           |
    /// +=======================================+

    // LOGIN USER
    @POST("/api/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // REGISTER USER
    @POST("/api/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);


    /// +=======================================+
    //  |         USER PATCH METHODS            |
    /// +=======================================+

    // UPDATE USER PROFILE
    @PATCH("/api/update-profile")
    Call<LoginResponse.User> updateProfile(@Body ProfileUpdateRequest request);
}
