package edu.mit.urop.playground.tsl.feedit.models;

public class Reaction {

    //omitted reaction id. - FirebaseDB will created this itself.

    //id of the situation this reaction is associated with.

    String mDescription;
    int mNumberLikes;
    int mNumberDislikes;
    int mSituationId;
    String mTitle;


    //Default constructor for read operations.
    public Reaction() {
    }

    public Reaction(int situationId, String title, String description, int numberLikes, int numberDislikes){
        mSituationId = situationId;
        mTitle = title;
        mDescription = description;
        mNumberLikes = numberLikes;
        mNumberDislikes = numberDislikes;

    }

    public int getmSituationId() {
        return mSituationId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {return mDescription;}

    public int getmNumberLikes() {
        return mNumberLikes;
    }

    public int getmNumberDislikes() {
        return mNumberDislikes;
    }
}
