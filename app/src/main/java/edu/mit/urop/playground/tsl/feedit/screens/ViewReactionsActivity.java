package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.adapters.ReactionAdapter;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

public class ViewReactionsActivity extends AppCompatActivity implements ValueEventListener{

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


        mDatabaseRef.addValueEventListener(this);
        recyclerView = findViewById(R.id.rw_grid_container);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    /*
    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseRef.addValueEventListener(this);

    }

    */

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatabaseRef.removeEventListener(this);
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        mDatabaseRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                retrievedReactions = new ArrayList<>();

                for(DataSnapshot reactionsSnapShot : dataSnapshot.getChildren()){

                    Reaction reaction = reactionsSnapShot.getValue(Reaction.class);
                    retrievedReactions.add(reaction);
                }

                adapter = new ReactionAdapter(retrievedReactions);

                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
