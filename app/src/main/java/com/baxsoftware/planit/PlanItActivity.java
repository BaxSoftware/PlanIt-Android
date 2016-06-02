package com.baxsoftware.planit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PlanItActivity extends AppCompatActivity {
    protected FirebaseAnalytics mAnalytics;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;

    protected EventsManager mEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Init or get event manager
        mEvents = EventsManager.getInstance();
        mEvents.updateContext(this.getApplicationContext());

        // Init Firebase Analytics
        mAnalytics = FirebaseAnalytics.getInstance(this);

        // Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Init Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        // TODO: Should we move Google API here for indexing purposes?
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mEvents.mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mEvents.mAuthListener != null) {
            mAuth.removeAuthStateListener(mEvents.mAuthListener);
        }
    }
}
