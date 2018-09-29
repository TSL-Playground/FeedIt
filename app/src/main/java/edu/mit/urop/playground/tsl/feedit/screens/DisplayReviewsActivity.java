package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.adapters.OnReviewClickListener;
import edu.mit.urop.playground.tsl.feedit.adapters.ReviewAdapter;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;
import edu.mit.urop.playground.tsl.feedit.models.Review;

/**
 * Feed-it creates a forum-like platform by allowing the users to write reviews to reactions and also view other
 * users' reviews stored in the Firabase database. This Activity fetches the reviews for a particular reaction and
 * displays them.
 */

public class DisplayReviewsActivity extends AppCompatActivity implements OnReviewClickListener{

    public static final String EXTRA_RECEPTION_KEY = "LbW9xS4";

    //Reviews are displayed in a RecyclerView (good coding practice.)
    RecyclerView reviewsRecyclerView;
    ReviewAdapter adapter;

    //The reaction for which the reviews will be displayed.
    Reaction reaction;

    // Reference(client) to the Firebase Realtime Database API.
    DatabaseReference mDatabaseRef;

    //List containing the reviews fetched from the database.
    List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reviews);

        //Getting a reference to the "reviews" table in the Firebase database.
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reviews");

        reviews = new ArrayList<>();

        //Setting up the recyclerview in which the app displays the reviews.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1 );
        reviewsRecyclerView = findViewById(R.id.rw_review_grid_container);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        //This activity will be triggered by the ReactionDetailsActivity. Getting a reference to the starting intent
        //to fetch the data it passed along.
        Intent fromReactionDetailsActivity = getIntent();

        if(fromReactionDetailsActivity != null){

            //Getting the Reaction object the ReactionDetailsActivity passed to this activity.
            //This reaction object is the one for which the user wants to view the previously recorded reviews.
            reaction = Parcels.unwrap(fromReactionDetailsActivity.getParcelableExtra(EXTRA_RECEPTION_KEY));

            //Querying the firebase database for the reaction the user wants to read the reviews for.
            Query firebaseQuery = mDatabaseRef.orderByChild("reactionId").equalTo(reaction.getmReactionId());

            //Adding the ValueEventListener to update the displayed data realtime(using the Observer Pattern.)
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    /**
                     * Note to future developer: The app currently does not have a database administrator who would remove
                     * the inappropriate content. Without such authority, I did not want to allow users to delete any reviews
                     * since they might delete other's reviews from the database, as well.
                     *
                     * If you ever create a administrator authority who can delete reviews, in this method, please handle the
                     * case for review deletion because this method will be invoked in that case.
                     */
                    for(DataSnapshot reviewsSnapShot : dataSnapshot.getChildren()){

                        Review review = reviewsSnapShot.getValue(Review.class);

                        reviews.add(review);

                    }

                    /**If there are no reviews, display an alertdialog to notify the user and ask if they would
                    like to be the first one to add a review.
                     **/
                    if(reviews.size() == 0){

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                DisplayReviewsActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("There are no reviews yet, would you like to write one?");

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Write review",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {


                                        Intent toWriteReviewActivity = new Intent(DisplayReviewsActivity.this,
                                                WriteReviewActivity.class);

                                        toWriteReviewActivity.putExtra(WriteReviewActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(reaction));

                                        startActivity(toWriteReviewActivity);
                                    }
                                })
                                .setNegativeButton("Not now",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        //If this button is clicked, cancel the dialog.

                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }




                    //Update the recyclerview adapter with the new data, setAdapter() method will display the updated data.
                    adapter = new ReviewAdapter(reviews, DisplayReviewsActivity.this);
                    reviewsRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    /**
                     * Currently unhandled.
                     */
                }
            });



        }

   }

    /**
     * Just as the app allows for sharing the reaction, Feed-it also allows the user to share the reviews they liked.
     * This method handles that request by passing the content of the reviews to the user's choice of an app which can
     * handle ACTION_SHARE.
     *
     * @param reviewPosition
     */
    @Override
    public void onShareButtonClicked(int reviewPosition) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, reviews.get(reviewPosition).getReviewText());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "A valuable insight from Feed-It");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share with..."));

    }
}

