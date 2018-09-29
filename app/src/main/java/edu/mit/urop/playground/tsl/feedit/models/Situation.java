package edu.mit.urop.playground.tsl.feedit.models;

/**
 * POJO model class for the Reaction cards.
 *
 */


/**
 * This model class for situation cards are redundant in the current version of the app.
 *
 * This model class will serve as a template for future developments in case the app is modified to
 * allow for adding new situations to the database and possibly adding reactions and commends for them.
 */
public class Situation {

    int mId;
    String mText;

    //Default constructor for read operations from the DB.
    public Situation() {
    }

    public Situation(String text, int id){
        mText = text;
        mId = id;
    }


    public int getmId() {
        return mId;
    }

    public String getmText() {
        return mText;
    }
}
