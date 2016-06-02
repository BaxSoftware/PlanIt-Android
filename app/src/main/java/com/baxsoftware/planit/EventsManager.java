package com.baxsoftware.planit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventsManager {
    private enum AuthState {
        UNKNOWN,
        LOGGED_OUT,
        LOGGED_IN
    }

    private static final String TAG = "[EVENTS]";
    private AuthState mLoggedIn = AuthState.UNKNOWN;

    public FirebaseAuth.AuthStateListener mAuthListener;
    public GoogleApiClient.OnConnectionFailedListener mGoogleConnectionFailedListener;

    private static EventsManager ourInstance = new EventsManager();
    public static EventsManager getInstance() { return ourInstance; }

    private Context mContext = null;
    public void updateContext (Context context) { mContext = context; }

    private EventsManager() {
        // Init Firebase authentication listeners
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    if (mLoggedIn == AuthState.LOGGED_OUT) {
                        mLoggedIn = AuthState.LOGGED_IN;
                        Intent mainIntent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(mainIntent);
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    if (mLoggedIn == AuthState.UNKNOWN ||
                            mLoggedIn == AuthState.LOGGED_IN) {
                        mLoggedIn = AuthState.LOGGED_OUT;
                        Intent loginIntent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(loginIntent);
                    }
                }
                // ...
            }
        };

        // Init Google Auth listeners
        mGoogleConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
            }
        };
    }
}
