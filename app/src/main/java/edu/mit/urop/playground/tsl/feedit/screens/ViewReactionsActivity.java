package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import edu.mit.urop.playground.tsl.feedit.adapters.OnReactionClickListener;
import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.adapters.ReactionAdapter;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

/**
 * This screen fetches from the Firebase RealTime database the reactions for a scanned situation card and displays them to
 * the user with options to write and view reviews and up/down vote the reaction and the review.
 *
 */

public class ViewReactionsActivity extends AppCompatActivity implements ValueEventListener, OnReactionClickListener{

    private static final String TAG = ViewReactionsActivity.class.getSimpleName();

    public static final String EXTRA_SITUATION_ID = "TBD"; // arbitrary constant.

    int mScannedSituationId;
    ArrayList<Reaction> retrievedReactions; // stores the list of reactions retrieved from the Firebase DB.

    //Displaying thre reactions with a recyclerview for memory economy and runtime efficiency.
    ReactionAdapter adapter;
    RecyclerView recyclerView;


    DatabaseReference mDatabaseRef; //Client object to the Firebase DB API.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reactions);

        //Getting the intent that started this activity.
        Intent fromScanResultsActivity = getIntent();

        //Instantiating the reference to the Firebase DB.
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reactions");


        if(fromScanResultsActivity != null && fromScanResultsActivity.hasExtra(EXTRA_SITUATION_ID)){
            mScannedSituationId = fromScanResultsActivity.getIntExtra(EXTRA_SITUATION_ID, 0);
        }
        else
            Log.d(TAG, "Error in passing the intent from ScanResultActivity to ViewReactionsActivity");


        //Setting up the recyclerview.
        adapter = new ReactionAdapter(null, this);
        recyclerView = findViewById(R.id.rw_grid_container);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);



        //Querying the Firebase database with the situation id for reactions to the situation.
        Query firebaseQuery = mDatabaseRef.orderByChild("mSituationId").equalTo(mScannedSituationId);

        //ValueEventListener will provide real-time content update from the Firebase real-time database.
        firebaseQuery.addValueEventListener(this);

    }


    //Removing the event listener to prevent memory leaks and to eliminate inefficient usage of limited system resources.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatabaseRef.removeEventListener(this);
    }


    //ValueEventListener's callback method for the changes in the observed data.
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //When data changes,

                retrievedReactions = new ArrayList<>();

                for(DataSnapshot reactionsSnapShot : dataSnapshot.getChildren()){

                    Reaction reaction = reactionsSnapShot.getValue(Reaction.class);

                    retrievedReactions.add(reaction);
                }

                adapter.updateDataSource(retrievedReactions);

                recyclerView.setAdapter(adapter);


    }

    //If the user interrupts the web operation intentionally.
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        Toast.makeText(this, "View request has been cancelled, try again.", Toast.LENGTH_SHORT).show();

    }


    /**
     * Implementation of the callback method from OnReactionClickListener. The ReactionAdapter communicates with the
     * activities through this interface.
     *
     * This method passes the index of th reaction card tapped by the user to the ReactionDetailsActivity.
     *
     * @param reactionIdx index of the reaction card that was tapped by the user.
     */
    @Override
    public void onReactionCardTapped(int reactionIdx) {

        Reaction tappedReaction = retrievedReactions.get(reactionIdx);
        Intent toReactionDetailActivity = new Intent(this, ReactionDetailsActivity.class);
        toReactionDetailActivity.putExtra(ReactionDetailsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(tappedReaction));
        toReactionDetailActivity.putExtra("source", TAG);


        startActivity(toReactionDetailActivity);

    }

    /**
     * Feed-It allows the user to share a reaction they deem to be appropriate for a situation.
     * This method creates a share intent and facilitates the process by passing the content of the reaction to
     * the apps that are currently installed on the device and support the share action.
     *
     * @param reactionIdx index of the reaction in the retrievedReactions array.
     */
    @Override
    public void onReactionShareTapped(int reactionIdx) {


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "When this happened: "+"\n\n"+
                retrievedReactions.get(reactionIdx).getmSituationText()+"\n\n"+
                "A group of people who played Feed-It decided to react by: "+ "\n\n"+
                retrievedReactions.get(reactionIdx).getmTitle() + "\n\n"+ "Here's their explanation: "
                        + retrievedReactions.get(reactionIdx).getmDescription());

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "A valuable insight from Feed-It");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share with..."));

    }


}
