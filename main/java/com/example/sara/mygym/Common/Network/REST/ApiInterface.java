package com.example.sara.mygym.Common.Network.REST;

import com.example.sara.mygym.Common.Network.Wrappers.AboutUsModel;
import com.example.sara.mygym.Common.Network.Wrappers.AccountModel;
import com.example.sara.mygym.Common.Network.Wrappers.LoginModel;
import com.example.sara.mygym.Common.Network.Wrappers.MembershipModel;
import com.example.sara.mygym.Common.Network.Wrappers.MessageModel;
import com.example.sara.mygym.Common.Network.Wrappers.NotificationModel;
import com.example.sara.mygym.Common.Network.Wrappers.PeriodModel;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Common.Network.Wrappers.RoleModel;
import com.example.sara.mygym.Common.Network.Wrappers.TariffModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Sara on 14.1.2018..
 */

public interface ApiInterface {
    @POST("/api/login")
    Call<Integer> login(@Body LoginModel loginModel);

    @GET("/api/aboutUs")
    Call<AboutUsModel> getAboutUs();

    @PUT("/api/aboutUs")
    Call<Boolean> putAboutUs(@Body AboutUsModel aboutUsModel);



    @GET("/api/period")
    Call<List<PeriodModel>> getListOfPeriods();

    @POST("/api/period")
    Call<Boolean> postPeriod(@Body PeriodModel periodModel);

    @PUT("/api/period")
    Call<Boolean> putPeriod(@Body PeriodModel periodModel);

    @DELETE("/api/period/{id}")
    Call<Boolean> deletePeriod(@Path("id") int id);



    @GET("/api/tariff")
    Call<List<TariffModel>> getListOfTariffs();

    @POST("/api/tariff")
    Call<Boolean> postTariff(@Body TariffModel tariffModel);

    @PUT("/api/tariff")
    Call<Boolean> putTariff(@Body TariffModel tariffModel);

    @DELETE("/api/tariff/{id}")
    Call<Boolean> deleteTariff(@Path("id") int id);



    @GET("/api/persons")
    Call<List<PersonModel>> getListOfPersons();

    @GET("/api/persons/{id}")
    Call<PersonModel> getPersonById(@Path("id") int id);

    @POST("/api/persons/resetPass")
    Call<Void> resetPassword(@Body String idPerson);

    @POST("/api/persons")
    Call<Boolean> postPerson(@Body PersonModel personModel);

    @PUT("/api/persons")
    Call<Boolean> putPerson(@Body PersonModel personModel);

    @DELETE("/api/persons/{id}")
    Call<Boolean> deletePerson(@Path("id") int id);



    @GET("/api/membership/{id}")
    Call<MembershipModel> getMembershipById(@Path("id") int id);

    @POST("/api/membership")
    Call<Integer> postMembership(@Body MembershipModel membershipModel);

    @PUT("/api/membership")
    Call<Boolean> putMembership(@Body MembershipModel membershipModel);



    @GET("/api/notification")
    Call<List<NotificationModel>> getListOfNews();

    @POST("/api/notification")
    Call<Boolean> postNews(@Body NotificationModel notificationModel);

    @PUT("/api/notification")
    Call<Boolean> putNews(@Body NotificationModel notificationModel);

    @DELETE("/api/notification/{id}")
    Call<Boolean> deleteNews (@Path("id") int id);



    @GET("/api/messages")
    Call<List<MessageModel>> getMessages();

    @GET("/api/messages/{id}")
    Call<MessageModel> getMessageById(@Path("id") int id);

    @POST("/api/messages")
    Call<Boolean> postMessage(@Body MessageModel messageModel);

    @HTTP(method = "DELETE", path = "/api/messages/{idOwner}/{idPerson}", hasBody = true)
    Call<Void> deleteMessage (@Path("idOwner") int idOwner, @Path("idPerson") int idPerson, @Body String isAnonymous);



    @GET("/api/role")
    Call<List<RoleModel>> getRoles();

    @GET("api/roles/{idPerson}")
    Call<RoleModel> getRole(@Path("idPerson") int idPerson);


    @PUT("/api/account")
    Call<Void> putAccount(@Body AccountModel accountModel);
}
