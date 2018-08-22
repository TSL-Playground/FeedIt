package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import edu.mit.urop.playground.tsl.feedit.OnReactionClickListener;
import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.adapters.ReactionAdapter;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

public class ViewReactionsActivity extends AppCompatActivity implements ValueEventListener, OnReactionClickListener{

    private static final String TAG = ViewReactionsActivity.class.getSimpleName();
    public static final String EXTRA_SITUATION_ID = "TBD";
    int mScannedSituationId;
    ArrayList<Reaction> retrievedReactions;
    ReactionAdapter adapter;
    RecyclerView recyclerView;


    DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reactions);

        Intent fromScanResultsActivity = getIntent();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reactions");

        if(fromScanResultsActivity != null && fromScanResultsActivity.hasExtra(EXTRA_SITUATION_ID)){
            mScannedSituationId = fromScanResultsActivity.getIntExtra(EXTRA_SITUATION_ID, 0);
        }
        else
            Log.d(TAG, "Error in passing the intent from ScanResultActivity to ViewReactionsActivity");



        adapter = new ReactionAdapter(null, this);

        recyclerView = findViewById(R.id.rw_grid_container);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);




        Query firebaseQuery = mDatabaseRef.orderByChild("mSituationId").equalTo(mScannedSituationId);
        firebaseQuery.addValueEventListener(this);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatabaseRef.removeEventListener(this);
    }



    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                retrievedReactions = new ArrayList<>();

                for(DataSnapshot reactionsSnapShot : dataSnapshot.getChildren()){

                    Reaction reaction = reactionsSnapShot.getValue(Reaction.class);

                    retrievedReactions.add(reaction);
                }

                adapter.updateDataSource(retrievedReactions);

                recyclerView.setAdapter(adapter);


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        Toast.makeText(this, "View request has been cancelled, try again.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onReactionCardTapped(int reactionIdx) {

        Reaction tappedReaction = retrievedReactions.get(reactionIdx);
        Intent toReactionDetailActivity = new Intent(this, ReactionDetailsActivity.class);
        toReactionDetailActivity.putExtra(ReactionDetailsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(tappedReaction));
        startActivity(toReactionDetailActivity);

    }




}
