package com.baxsoftware.planit.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public abstract class PlanItModel {

    protected String uid;
    public long created;
    public long updated;

    protected DatabaseReference mDatabase;

    public String getUID()
    {
        return this.uid;
    }

    public void update()
    {
        updated = (System.currentTimeMillis() / 1000L);

        DatabaseReference cRef;
        if (uid == null || uid.isEmpty())
        {
            cRef = mDatabase.push();
            uid = cRef.getKey();
        }
        else
            cRef = mDatabase.child(uid);

        cRef.setValue(this);
    }

    public void delete()
    {
        if (uid != null && !uid.isEmpty())
        {
            mDatabase.child(uid).removeValue();
        }
    }
}
