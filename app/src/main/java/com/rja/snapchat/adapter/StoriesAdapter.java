package com.rja.snapchat.adapter;

import android.graphics.Point;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rja.snapchat.R;
import com.rja.snapchat.listeners.OnClickAtIndex;
import com.rja.snapchat.models.Story;
import com.rja.snapchat.models.StoryItem;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by rjaylward on 3/6/17
 */

public class StoriesAdapter extends RecyclerView.Adapter implements OnClickAtIndex<Point> {

    private List<Story> mItems = new ArrayList<>();
    private OnStoryClickedListener mListener;

    public void setItems(List<Story> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StoryViewHolder viewHolder = new StoryViewHolder(StoryViewHolder.inflate(parent));
        viewHolder.setClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof StoryViewHolder) {
            ((StoryViewHolder) holder).load(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnStoryClickedListener(OnStoryClickedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClickAtIndex(Point point, int index) {
        if(mListener != null)
            mListener.onStoryClicked(index, mItems.get(index), point);
    }

    public interface OnStoryClickedListener {
        void onStoryClicked(int index, Story conversation, Point point);
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {

        public static View inflate(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_story, parent, false);
        }

        private TextView mTitle;
        private TextView mSubtitle;
        private ImageView mImageView;
        private OnClickAtIndex<Point> mOnClickAtIndex;

        private View mBackground;
        private CardView mCardView;

        public StoryViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.lic_title);
            mSubtitle = (TextView) itemView.findViewById(R.id.lic_subtitle);
            mImageView = (ImageView) itemView.findViewById(R.id.lic_status_image);
            mCardView = (CardView) itemView.findViewById(R.id.lic_card_background);
            mBackground = itemView.findViewById(R.id.lic_card_root);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mOnClickAtIndex != null) {
                        Point point = new Point((int) mImageView.getX(), (int) mImageView.getY());
                        mOnClickAtIndex.onClickAtIndex(point, getAdapterPosition());
                    }
                }

            });
        }

        public void setClickListener(OnClickAtIndex<Point> listener) {
            mOnClickAtIndex = listener;
        }

        public void load(Story story) {
            if(getAdapterPosition() == 0) {
                mCardView.setVisibility(View.VISIBLE);
                mBackground.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
            else {
                mCardView.setVisibility(View.GONE);
                mBackground.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.card_background_color));
            }

            StoryItem storyItem = story.getCurrentStoryItem();
            String imageUrl = storyItem != null ? storyItem.getImageUrl() != null ? storyItem.getImageUrl() : storyItem.getVideoUrl() : null;
            if(imageUrl != null) {
                Glide.with(itemView.getContext()).load(imageUrl)
                        .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                        .placeholder(R.drawable.black_circle)
                        .into(mImageView);
            }
            else {
                mImageView.setImageResource(R.drawable.loading_circle);
            }

            mTitle.setText(Build.MANUFACTURER);
            mSubtitle.setText(Build.MODEL);
        }

    }

}
