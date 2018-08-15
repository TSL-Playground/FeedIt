package edu.mit.urop.playground.tsl.feedit;

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
