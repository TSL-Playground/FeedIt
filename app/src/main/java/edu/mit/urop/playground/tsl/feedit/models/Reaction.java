package edu.mit.urop.playground.tsl.feedit.models;


import org.parceler.Parcel;

import java.util.List;


/**
 * POJO model class for the Reaction cards.
 *
 */

@Parcel // Parcel notation is for the external Parceler package to recognize this class (de)serializable.
public class Reaction {

    //omitted reaction id. - FirebaseDB will created this itself.

    //id of the situation this reaction is associated with.

    String mReactionId;
    String mDescription;
    int mNumberLikes;
    int mNumberDislikes;
    int mSituationId;
    String mTitle;
    String mSituationText;


    //Default constructor for read operations - and the Parceler library.
    public Reaction() {
    }

    public Reaction(String id, int situationId,
                    String title, String description,
                    int numberLikes, int numberDislikes,
                    String situationText){

        mReactionId = id;
        mSituationId = situationId;
        mTitle = title;
        mDescription = description;
        mNumberLikes = numberLikes;
        mNumberDislikes = numberDislikes;
        mSituationText = situationText;

    }

    public String getmReactionId() { return mReactionId; }

    public int getmSituationId() { return mSituationId; }

    public String getmTitle() { return mTitle; }

    public String getmDescription() {return mDescription;}

    public int getmNumberLikes() { return mNumberLikes; }

    public int getmNumberDislikes() { return mNumberDislikes; }

    public String getmSituationText(){ return mSituationText;}

}
