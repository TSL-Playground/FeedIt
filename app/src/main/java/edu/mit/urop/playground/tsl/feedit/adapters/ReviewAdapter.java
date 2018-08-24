package edu.mit.urop.playground.tsl.feedit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Review;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{


    List<Review> mReviewList;
    OnReviewClickListener mReviewClickListener;

    //This list of reviews will be passed by the parent activity.
    public ReviewAdapter(List<Review> reviews, OnReviewClickListener listener) {
        mReviewList = reviews;
        mReviewClickListener = listener;

    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int rowId = R.layout.review_row_layout;
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view  = inflater.inflate(rowId, parent, false);

        ReviewViewHolder holder = new ReviewViewHolder(view);

        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.reviewText.setText(mReviewList.get(position).getReviewText());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView reviewText;
        Button shareButton;


        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewText = itemView.findViewById(R.id.tw_review_display);
            shareButton = itemView.findViewById(R.id.btn_share_review);

            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

              mReviewClickListener.onShareButtonClicked(getAdapterPosition());
        }
    }
}
