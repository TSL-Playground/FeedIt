package edu.mit.urop.playground.tsl.feedit.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

public class ReactionAdapter extends RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder> {

    //This is where the read query results will be stored, the data source of the adapter.
    static List<Reaction> mReactionDataSource;

    public ReactionAdapter(List<Reaction> dataSource) {

        mReactionDataSource = dataSource;
    }

    @NonNull
    @Override
    public ReactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int rowLayoutId = R.layout.reaction_item_layout;

        View view = inflater.inflate(rowLayoutId, parent, false);
        ReactionViewHolder holder = new ReactionViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReactionViewHolder holder, int position) {

        holder.mReactionTitle.setText(mReactionDataSource.get(position).getmTitle());

    }

    @Override
    public int getItemCount() {
        return mReactionDataSource.size();
    }

    class ReactionViewHolder extends RecyclerView.ViewHolder{

        TextView mReactionTitle;

        public ReactionViewHolder(View itemView) {
            super(itemView);

            mReactionTitle = itemView.findViewById(R.id.tw_reaction_item_title);
        }
    }



    public void updateDataSource(List<Reaction> reactions){

        mReactionDataSource = reactions;
        notifyDataSetChanged();
    }
}