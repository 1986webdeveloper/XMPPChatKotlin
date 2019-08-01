package com.example.acquaint.xmppappdemo.networking_setup

import com.example.acquaint.xmppappdemo.model.GroupResponse
import com.example.acquaint.xmppappdemo.model.RegisterRequestResponse
import com.example.acquaint.xmppappdemo.model.UserResponse

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @get:Headers("Authorization:vA0CFcOI6tXGORAd", "Content-Type: application/json", "Accept: application/json")
    @get:GET("plugins/restapi/v1/users")
    val contactList: Call<UserResponse>


    @Headers("Authorization:vA0CFcOI6tXGORAd", "Content-Type: application/json", "Accept: application/json")
    @GET("plugins/restapi/v1/users/{username}/groups")
    fun getUserGroup(@Path("username") username: String): Call<GroupResponse>

    @Headers("Authorization:vA0CFcOI6tXGORAd", "Content-Type: application/json", "Accept: application/json")
    @POST("plugins/restapi/v1/users")
    fun registerUser(@Body registerRequestResponse: RegisterRequestResponse): Call<ResponseBody>

    //    @GET("webservice.php?action=city_list")
    //    Call<GeneralResponseModel> getAllCities();
    //
    //    @GET("webservice.php?action=register_user")
    //    Call<GeneralResponseModel> registerANewUser(@Query("user_email") String userEmail,
    //                                                @Query("user_pwd") String password,
    //                                                @Query("user_city_id") String cityId,
    //                                                @Query("user_status") String userStatus);
    //
    //    @GET("webservice.php?action=city_station_list")
    //    Call<GeneralResponseModel> GetStationsBasedOnCity(@Query("stn_city_id") String stnCityId);
    //
    //    @POST("register")
    //    Call<RegisterModel> registerUser(@Body RegisterModel registerRequestModel);
    //
    //    @POST("accessToken")
    //    Call<RegisterModel> getAccessToken(@Body RegisterModel registerRequestModel);
    //
    //    @PUT("register")
    //    Call<RegisterModel> registerToken(@Part("access_token") String accessToken);
    //
    //    @Headers("x-amzn-identity-auth-domain: amazon.com")
    //    @POST("auth/register")
    //    Call<AuthResponseModel> registerAuth(@Body RegisterModel registerModel);
    //
    //    //    @Multipart
    //    @PUT("register")
    //    Call<ResponseBody> refreshAmazonToken(@Body RegisterModel registerModel);
    //
    //
    //    @GET("GetOffersForProvider")
    //    Call<ResponseBody> getOffers(@Header("x-amz-access-token") String amazonToken,
    //                                 @Header("x-flex-instance-id") String flexId,
    //                                 @Header("User-Agent") String UserAgent,
    //                                 @Query("serviceAreaIds") List<String> serviceAreaId,
    //                                 @Query("apiVersion") String apiVersion);
    //
    //    @Headers({"Cache-Control: no-cache", "Accept: */*"})
    //    @GET("eligibleServiceAreas")
    //    Call<ServiceAreaIds> getEligibleServiceArea(@Header("x-amz-access-token") String amazonToken,
    //                                                @Header("x-flex-instance-id") String flexId,
    //                                                @Header("User-Agent") String UserAgent);
    //
    //    // @Headers({"Cache-Control: no-cache","Accept: */*"})
    //    @GET("webservice.php?action=serviceAreas")
    //    Call<ServiceAreaResponse> getServiceArea();
    //
    //    @FormUrlEncoded
    //    @POST("webservice.php?action=getAccessToken")
    //    Call<RegisterModel> getAccessTokenNew(@Field("email") String email,
    //                                          @Field("password") String password);
    //
    //    @FormUrlEncoded
    //    @POST("webservice.php?action=getOfferList")
    //    Call<OfferListResponse> getOffers(@Field("token") String apiVersion);


}
