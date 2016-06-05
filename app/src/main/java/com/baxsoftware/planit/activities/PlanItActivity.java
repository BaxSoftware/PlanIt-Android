package com.baxsoftware.planit.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.baxsoftware.planit.R;
import com.baxsoftware.planit.managers.EventsManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public abstract class PlanItActivity extends AppCompatActivity {
    protected FirebaseAnalytics mAnalytics;
    protected FirebaseRemoteConfig mRemoteConfig;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;

    protected EventsManager mEvents;
    protected DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Init or get event manager
        mEvents = EventsManager.getInstance();
        mEvents.updateContext(this.getApplicationContext());

        // Init Firebase Analytics
        mAnalytics = FirebaseAnalytics.getInstance(this);

        // Init Firebase Remote Config
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        mRemoteConfig.setDefaults(R.xml.rc_defaults);
        mRemoteConfig.fetch(20)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mRemoteConfig.activateFetched();
                        }

                        if (mRemoteConfig.getBoolean("maintenance"))
                        {
                            new AlertDialog.Builder(PlanItActivity.this)
                                    .setTitle("Sorry!")
                                    .setMessage("We are in maintenance... please check again later.")
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            PlanItActivity.this.finish();
                                            System.exit(0);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                });

        // Init Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Init Firebase Database if user is authenticated
        if (mUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    Object value = dataSnapshot.getValue(Object.class);
                    Log.d("PlanIT-CORE", key + " => " + value.toString());
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    // Failed to read value
                }
            });
        }

        Log.d("PlanIT-CORE", "Post init of database " + ((mDatabase != null) ? "Not null" : "Null"));

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
