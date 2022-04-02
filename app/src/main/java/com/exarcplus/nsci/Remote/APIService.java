package com.exarcplus.nsci.Remote;

import com.exarcplus.nsci.LoginActivity;
import com.exarcplus.nsci.Model.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIService {

    @GET("/mobile/committee")
    Call<Committe_List> RetrieveCommitte();

    @GET
    Call<Profile_List> RetrieveProfile(@Url String api);

    @GET
    Call<InterestedUser>RetrieveInterestedUser(@Url String api);

    @GET
    Call<CompletedEvent_List> RetrieveCompletedEvent(@Url String api);

    @GET
    Call<Gallery_List> RetrieveGallery(@Url String api);

    @GET
    Call<Thought_List> RetrieveThoght(@Url String api);

    @GET
    Call<OngoingEvent_List> RetrieveOngoingEvent(@Url String api);

    @GET
    Call<About_List> RetrieveAboutus(@Url String api);

    @GET
    Call<UpcomingEvent_List> RetrieveUpcomingEvent(@Url String api);

    @GET
    Call<Member_List> RetrieveMember(@Url String api);

    @POST
    @FormUrlEncoded
    Call<Feedback_Post> saveFeedbackPost(@Url String api,
                                         @Field("subject") String subject,
                                        @Field("message") String message);

    @POST("/mobile/login")
    @FormUrlEncoded
    Call<Login_Post> saveLoginPost(@Field("email") String email,
                                   @Field("password") String password);

    @POST("/mobile/register")
    @FormUrlEncoded
    Call<Register_Post> saveRegisterPost(@Field("name") String name,
                                         @Field("committee") String committee,
                                         @Field("qualification") String qualification,
                                         @Field("email") String email,
                                         @Field("image") String image,
                                         @Field("password") String password,
                                         @Field("real_password") String real_password,
                                         @Field("address") String address,
                                         @Field("latitude") String latitude,
                                         @Field("longitude") String longitude,
                                         @Field("experience") String experience,
                                         @Field("mobile_no") String mobile_no);


    @POST
    @FormUrlEncoded
    Call<Update_Post> UpdateProfile(@Url String api,
                                      @Field("name") String name,
                                      @Field("committee") String committee,
                                      @Field("qualification") String qualification,
                                      @Field("email") String email,
                                      @Field("image") String image,
                                      @Field("address") String address,
                                      @Field("experience") String experience,
                                      @Field("mobile_no") String mobile_no);
}


