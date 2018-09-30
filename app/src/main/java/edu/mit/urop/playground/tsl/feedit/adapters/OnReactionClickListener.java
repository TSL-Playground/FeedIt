package edu.mit.urop.playground.tsl.feedit.adapters;


/**
 * This is the interface through which the ReactionAdapter communicates the user taps
 * on the reaction cards with the ViewReactionsActivity.
 *
 */
public interface OnReactionClickListener {

    void onReactionCardTapped(int reactionIdx);
    void onReactionShareTapped(int reactionIdx);
}
