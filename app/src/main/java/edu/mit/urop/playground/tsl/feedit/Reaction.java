package edu.mit.urop.playground.tsl.feedit;

public class Reaction {

    //omitted reaction id. - FirebaseDB will created this itself.

    //id of the situation this reaction is associated with.

    int mSituationId;
    String mTitle;
    String mDescription;

    //Default constructor for read operations.
    public Reaction() {
    }

    public Reaction(int situationId, String title, String description){
        mSituationId = situationId;
        mTitle = title;
        mDescription = description;

    }

    public int getmSituationId() {
        return mSituationId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {return mDescription;}
}
