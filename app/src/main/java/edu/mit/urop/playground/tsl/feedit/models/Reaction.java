package edu.mit.urop.playground.tsl.feedit.models;


import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Reaction {

    //omitted reaction id. - FirebaseDB will created this itself.

    //id of the situation this reaction is associated with.

    String reactionId;
    String mDescription;
    int mNumberLikes;
    int mNumberDislikes;
    int mSituationId;
    String mTitle;
    List<String> mReviews;


    //Default constructor for read operations - and the Parceler library.
    public Reaction() {
    }

    public Reaction(String id, int situationId,
                    String title, String description,
                    int numberLikes, int numberDislikes, List<String> reviews){

        reactionId = id;
        mSituationId = situationId;
        mTitle = title;
        mDescription = description;
        mNumberLikes = numberLikes;
        mNumberDislikes = numberDislikes;
        mReviews = reviews;
    }

    public String getReactionId() { return reactionId; }

    public int getSituationId() {
        return mSituationId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {return mDescription;}

    public int getNumberLikes() { return mNumberLikes; }

    public int getNumberDislikes() { return mNumberDislikes; }

    public List<String> getReviews(){ return mReviews; }

}
