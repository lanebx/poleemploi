package com.example.projetpoleemploi.ui.main.data.webservice.database;

import com.squareup.moshi.Json;

public class AccessTokenResponse {
    @Json(name = "expires_in")
    public String expires_in;
    @Json(name = "token_type")
    public String token_type;
    @Json(name = "access_token")
    public String accessToken;
    @Json(name = "scope")
    public String scope;
}