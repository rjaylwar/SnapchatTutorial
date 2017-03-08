package com.rja.snapchat.adapter;

import android.content.res.ColorStateList;
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

import com.rja.snapchat.R;
import com.rja.snapchat.listeners.OnClickAtIndex;
import com.rja.snapchat.models.ChatItem;
import com.rja.snapchat.models.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjaylward on 3/4/17
 */

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> implements OnClickAtIndex<Point> {

    private List<Chat> mItems = new ArrayList<>();
    private OnConversationClickedListener mListener;

    public void setItems(List<Chat> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConversationViewHolder viewHolder = new ConversationViewHolder(ConversationViewHolder.inflate(parent));
        viewHolder.setClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        holder.load(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnConversationClickedListener(OnConversationClickedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClickAtIndex(Point point, int index) {
        if(mListener != null)
            mListener.onConversationClicked(index, mItems.get(index), point);
    }

    public interface OnConversationClickedListener {
        void onConversationClicked(int index, Chat conversation, Point point);
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        public static View inflate(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        }

        private TextView mTitle;
        private TextView mSubtitle;
        private ImageView mImageView;
        private TextView mFriendShipView;
        private OnClickAtIndex<Point> mOnClickAtIndex;

        private View mBackground;
        private CardView mCardView;

        public ConversationViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.lic_title);
            mSubtitle = (TextView) itemView.findViewById(R.id.lic_subtitle);
            mImageView = (ImageView) itemView.findViewById(R.id.lic_status_image);
            mFriendShipView = (TextView) itemView.findViewById(R.id.lic_friendship_icon);
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

        public void load(Chat conversation) {
            if(getAdapterPosition() == 0) {
                mCardView.setVisibility(View.VISIBLE);
                mBackground.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
            else {
                mCardView.setVisibility(View.GONE);
                mBackground.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.card_background_color));
            }

            if(conversation != null && conversation.getChatMessages() != null && !conversation.getChatMessages().isEmpty()) {
                ChatItem chatMessage = conversation.getChatMessages().get(conversation.getChatMessages().size() - 1);

                mTitle.setText(Build.MODEL);
                mSubtitle.setText(Build.MANUFACTURER);

                if(chatMessage.getImageUrl() != null) {
                    mImageView.setBackgroundResource(chatMessage.isViewed() ? R.drawable.rectangle : R.drawable.rounded_rectange);
                    mImageView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.red)));
                }
                else if(chatMessage.getVideoUrl() != null) {
                    mImageView.setBackgroundResource(chatMessage.isViewed() ? R.drawable.rectangle : R.drawable.rounded_rectange);
                    mImageView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.light_purple)));
                }
                else if(chatMessage.getMessage() != null) {
                    mImageView.setBackgroundResource(R.drawable.rotated_triangle);
                    mImageView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.light_blue)));
                }
            }
        }

    }
}
