package com.example.projetpoleemploi.ui.main.data.webservice.database;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PEInterface {
    @FormUrlEncoded
    @POST("/connexion/oauth2/access_token?realm=partenaire")
    Call<AccessTokenResponse> getAccessToken(@Field("grant_type") String grant_type,
                                    @Field("client_id") String client_id,
                                    @Field("client_secret") String client_secret,
                                    @Field("scope") String scope);

    @GET("/partenaire/offresdemploi/v2/offres/search")
    Call<JobResponse> getAvailableJobs(@Header("Authorization") String accessToken,
                                       @Query("motsCles") String motsCles,
                                       @Query("range") String nombreResultats,
                                       @Query("departement") Integer departement,
                                       @Query("inclureLimitrophes") boolean inclureLimitrophes,
                                       @Query("tempsPlein") boolean tempsPlein,
                                       @Query("typeContrat") String typeContrat);
}