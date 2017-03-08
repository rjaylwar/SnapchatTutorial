package com.rja.snapchat.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import com.rja.snapchat.util.Print;

/**
 * Created by rjaylward on 3/5/17
 */

public class SurfaceVideoHelper implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = SurfaceVideoHelper.class.getSimpleName();

    private Surface mSurface;
    private TextureView mTextureView;

    private boolean mIsSeekComplete = true;
    private boolean mIsPrepared;
    private boolean mIsSurfaceAvailable;

    private MediaPlayer mMediaPlayer;
    private int mCurrentPosition;
    private int mState;
    private String mCurrentMediaSource;

    private boolean mPlayOnPrepared;
    private Callback mCallback;
    private final WifiManager.WifiLock mWifiLock;
    private int mCurrentDuration;

    private ViewGroup mRoot;

    public SurfaceVideoHelper(@NonNull ViewGroup rootLayout) {
        mRoot = rootLayout;

        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        mWifiLock = ((WifiManager) rootLayout.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "sample_lock");
    }

    private void resetTextureView(boolean removeOldView) {
        if(removeOldView)
            removeTextureView();

        TextureView tv = new TextureView(mRoot.getContext());
        mRoot.addView(tv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setTextureView(tv);
    }

    private void removeTextureView() {
        if(mTextureView != null) {
            mRoot.removeView(mTextureView);
            mTextureView.setSurfaceTextureListener(null);
        }
        mTextureView = null;
        mSurface = null;
        if(mMediaPlayer != null)
            mMediaPlayer.setSurface(null);

        mIsSurfaceAvailable = false;
    }

    private void setTextureView(@NonNull TextureView textureView) {
        mTextureView = textureView;
        mTextureView.setSurfaceTextureListener(this);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private void createMediaPlayerIfNecessary() {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);

            if(mSurface != null)
                mMediaPlayer.setSurface(mSurface);
        }
    }

    public void prepare(String url, boolean playOnPrepared) {
        if(mTextureView == null)
            resetTextureView(false);

        boolean mediaHasChanged = !TextUtils.equals(url, mCurrentMediaSource);
        if(mediaHasChanged) {
            mCurrentPosition = 0;
            mCurrentMediaSource = url;
        }

        mPlayOnPrepared = playOnPrepared;

        if(mState == PlaybackStateCompat.STATE_PAUSED && !mediaHasChanged && mMediaPlayer != null)
            playIfNecessary();
        else {
            mState = PlaybackStateCompat.STATE_STOPPED;
            relaxResources(false); // release everything except MediaPlayer

            try {
                createMediaPlayerIfNecessary();

                mState = PlaybackStateCompat.STATE_BUFFERING;
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(url);

                // Calls onPrepared()

                mIsPrepared = false;
                mMediaPlayer.prepareAsync();

                // Prevents the Wifi radio from going to
                // sleep while the song is playing.
                mWifiLock.acquire();

            }
            catch(Exception ex) {
                Print.e(TAG, ex, false);
                if(mCallback != null)
                    mCallback.onError(ex);
            }

            notifyStateChanged();
        }
    }

    public void play() {
        if(mCurrentMediaSource != null)
            prepare(mCurrentMediaSource, true);
        else
            Print.e(TAG, "You must call prepare before calling play");
    }

    public void pause() {
        mPlayOnPrepared = false;

        if(mState != PlaybackStateCompat.STATE_STOPPED) {
            if(mState == PlaybackStateCompat.STATE_PLAYING) {
                // Pause media player and cancel the 'foreground service' state.
                if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mCurrentPosition = mMediaPlayer.getCurrentPosition();
                }
                // while paused, retain the MediaPlayer but give up audio focus
                relaxResources(false);
            }

            mState = PlaybackStateCompat.STATE_PAUSED;
            notifyStateChanged();
        }
    }

    private void playIfNecessary() {
        if(isReadyToPlay()) {
            if(mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                Print.d(TAG, "configMediaPlayerState startMediaPlayer. seeking to ", mCurrentPosition);
                if(mCurrentPosition == mMediaPlayer.getCurrentPosition()) {
                    mMediaPlayer.start();
                    mState = PlaybackStateCompat.STATE_PLAYING;
                }
                else {
                    mState = PlaybackStateCompat.STATE_BUFFERING;
                    mIsSeekComplete = false;
                    mMediaPlayer.seekTo(mCurrentPosition);
                    //this will call onSeekComplete when it finishes and play
                }

                mCurrentDuration = mMediaPlayer.getDuration();

                notifyStateChanged();
            }
        }
    }

    public int getCurrentStreamPosition() {
        return mMediaPlayer != null && mIsPrepared ? mMediaPlayer.getCurrentPosition() : mCurrentPosition;
    }

    public int getCurrentDuration() {
        return mMediaPlayer != null && mIsPrepared ? mMediaPlayer.getDuration() : mCurrentDuration;
    }

    public void seekTo(int position) {
        if(mMediaPlayer == null) {
            // If we do not have a current media player, simply update the current position
            mCurrentPosition = position;
        }
        else {
            if(mMediaPlayer.isPlaying())
                mState = PlaybackStateCompat.STATE_BUFFERING;

            mIsSeekComplete = false;
            mMediaPlayer.seekTo(position);
            notifyStateChanged();
        }
    }

    public void stop(boolean notifyListeners, boolean destroyPlayer) {
        mState = PlaybackStateCompat.STATE_STOPPED;
        if(notifyListeners)
            notifyStateChanged();

        mCurrentPosition = getCurrentStreamPosition();
        // Give up Audio focus

        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        relaxResources(destroyPlayer);
    }

    public void destroy() {
        relaxResources(true);
    }

    private void relaxResources(boolean releaseMediaPlayer) {
        Print.d(TAG, "relaxResources. releaseMediaPlayer=", releaseMediaPlayer);

        // stop and release the Media Player, if it's available
        if(releaseMediaPlayer) {
            mCurrentPosition = 0;
            mCurrentDuration = 0;
            mIsPrepared = false;

            if(mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }

        // we can also release the Wifi lock, if we're holding it
        if(mWifiLock != null && mWifiLock.isHeld())
            mWifiLock.release();
    }

    private void notifyStateChanged() {
        if(mCallback != null)
            mCallback.onPlaybackStatusChanged(mState);
    }

    /**
     * Surface View methods
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mIsSurfaceAvailable = true;
        mSurface = new Surface(surface);
        if(mMediaPlayer != null) {
            mMediaPlayer.setSurface(mSurface);
            playIfNecessary();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        mIsSurfaceAvailable = false;
        stop(true, false);

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

    /**
     * Media player callback methods
     */

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsPrepared = true;
        if(mPlayOnPrepared)
            playIfNecessary();

        mPlayOnPrepared = false;

        if(mCallback != null)
            mCallback.onMediaPlayerPrepared(mCurrentMediaSource);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mCurrentPosition = mp.getCurrentPosition();
        mIsSeekComplete = true;

        if(mState == PlaybackStateCompat.STATE_BUFFERING)
            playIfNecessary();
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        Print.d("onCompletion from MediaPlayer");
        // The media player finished playing the current song, so we go ahead
        // and start the next.

        mCurrentPosition = 0;
        mIsPrepared = false;

        mState = PlaybackStateCompat.STATE_STOPPED;
        if(mCallback != null)
            mCallback.onPlaybackStatusChanged(mState);

        if(mCallback != null)
            mCallback.onCompletion();

        resetTextureView(true);
    }

    public boolean isReadyToPlay() {
        return mIsPrepared && mIsSurfaceAvailable && mIsSeekComplete;
    }

    private void updateTextureViewSize(int mVideoWidth, int mVideoHeight) {
        float viewWidth = mTextureView.getWidth();
        float viewHeight = mTextureView.getHeight();

        float scaleX = 1.0f;
        float scaleY = 1.0f;

        Print.d(TAG, "updateTextureViewSize", "video", mVideoWidth, mVideoHeight, "view", viewWidth, viewHeight);

        //TODO scale correctly

        // Calculate pivot points, in our case crop from center
        int pivotPointX;
        int pivotPointY;

        pivotPointX = (int) (viewWidth / 2);
        pivotPointY = (int) (viewHeight / 2);

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

        mTextureView.setTransform(matrix);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        //center crops the video
        updateTextureViewSize(width, height);
    }

    public interface Callback {
        void onMediaPlayerPrepared(String url);
        void onPlaybackStatusChanged(int playbackState);
        void onError(Exception e);
        void onCompletion();
    }
}
