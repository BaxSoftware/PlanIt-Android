package com.baxsoftware.planit.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Event extends PlanItModel {

    public String uid_creator;
    public String name;
    public String description;
    public String fb_id;
    public String google_id;

    public Event() {
        mDatabase = FirebaseDatabase.getInstance().getReference("events");
    }

    public static List<Event> getFromID()
    {
        return new ArrayList<Event>();
    }
}
