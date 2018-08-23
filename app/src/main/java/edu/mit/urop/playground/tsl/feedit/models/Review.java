package edu.mit.urop.playground.tsl.feedit.models;

import org.parceler.Parcel;

@Parcel
public class Review {

    String reactionId;
    String reviewId;
    String reviewText;


    //Default constructor for the built-in POJO (de)serializer to work.
    public Review() {
    }

    public Review(String reactionId, String reviewId, String reviewText) {

        this.reactionId = reactionId;
        this.reviewId = reviewId;
        this.reviewText = reviewText;
    }


    public String getReactionId() {
        return reactionId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }
}
