package edu.mit.urop.playground.tsl.feedit.models;

import org.parceler.Parcel;

/**
 * POJO model class for reviews.
 *
 */

@Parcel // Parcel notation is for the external Parceler package to recognize this class (de)serializable.
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

    //In case the developer needs it for future modifications.
    public String getReactionId() {
        return reactionId;
    }

    //In case the developer needs it for future modifications.
    public String getReviewId() {
        return reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }
}
