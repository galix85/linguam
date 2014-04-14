package com.galix.linguam.RESTinterface;

import android.util.Base64;
import retrofit.RequestInterceptor;

/**
 * Interceptor used to authorize requests.
 * @author Jaume
 */
public class ApiRequestInterceptor implements RequestInterceptor {

   	private String username;
    private String password;

    @Override
    public void intercept(RequestFacade requestFacade) {

        if (username != null) {
            final String authorizationValue = encodeCredentialsForBasicAuthorization();
            requestFacade.addHeader("Authorization", authorizationValue);
        }
    }

    private String encodeCredentialsForBasicAuthorization() {
        final String userAndPassword = username + ":" + password;
        final int flags = 0;
        return "Basic " + Base64.encodeToString(userAndPassword.getBytes(), flags);
    }

    public void setUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}