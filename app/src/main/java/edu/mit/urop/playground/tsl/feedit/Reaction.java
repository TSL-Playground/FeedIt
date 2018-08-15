package edu.mit.urop.playground.tsl.feedit;

public class Reaction {

    //omitted reaction id. - FirebaseDB will created this itself.

    //id of the situation this reaction is associated with.

    int mSituationId;
    String mText;

    //Default constructor for read operations.
    public Reaction() {
    }

    public Reaction(int situationId, String text){
        mSituationId = situationId;
        mText = text;

    }

    public int getmSituationId() {
        return mSituationId;
    }

    public String getmText() {
        return mText;
    }
}
