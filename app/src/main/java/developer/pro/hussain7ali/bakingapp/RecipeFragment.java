package developer.pro.hussain7ali.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class RecipeFragment extends Fragment implements ExoPlayer.EventListener, View.OnClickListener {

    private static final String  TAG = "HussainAli";

    private final static String GITHUB_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RequestQueue requestQueue;

    private TextView viewRecipeShortDescTxt, viewRecipeDescriptionTxt;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private ImageView preStep, nextStep;
    private int item_pos;
    private Uri builtUri;
    private String recipeID;
    private int arraySize;

    private String videoURL;

    private final static String callbackShortDesc = "callbackShortDesc";
    private final static String callbackDescription = "callbackDescription";
    private final static String callbackVideoURL = "callbackVideoURL";

    private boolean vedioPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        viewRecipeShortDescTxt = view.findViewById(R.id.viewRecipe_short_desc_txt);
        viewRecipeDescriptionTxt = view.findViewById(R.id.viewRecipe_description_txt);

        mPlayerView = view.findViewById(R.id.playerView);

        preStep = view.findViewById(R.id.pre_step);
        nextStep = view.findViewById(R.id.next_step);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.exo_controls_play));
        initializeMediaSession();

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();
            item_pos = bundle.getInt("item_pos", 0);
            recipeID = bundle.getString(getText(R.string.recipeID).toString());

            getRecipe(builtUri, recipeID, item_pos);
        } else if (Objects.requireNonNull(getActivity()).getIntent().hasExtra(getText(R.string.recipeID).toString())) {

            builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();
            recipeID = getActivity().getIntent().getStringExtra(getText(R.string.recipeID).toString());
            item_pos = getActivity().getIntent().getIntExtra("item_pos", 0);

            getRecipe(builtUri, recipeID, item_pos);
        }

        preStep.setOnClickListener(this);
        nextStep.setOnClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(callbackDescription, String.valueOf(viewRecipeDescriptionTxt));
        outState.putString(callbackShortDesc, String.valueOf(viewRecipeShortDescTxt));
        outState.putString(callbackVideoURL, videoURL);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);

    }

    private void getRecipe(Uri builtUri, final String recipeID, final int item_pos) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                builtUri.toString(),
                (String) null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                String recipe_id = jsonObject.getString("id");

                                if (recipe_id.equals(recipeID)) {

                                    JSONArray jsonstepArray = jsonObject.getJSONArray("steps");
                                    arraySize = jsonstepArray.length();
                                    for (int j = 0; j <= jsonstepArray.length(); j++) {
                                        JSONObject jsonstepObject = jsonstepArray.getJSONObject(j);
                                        String ItemPosFromJSON = jsonstepObject.getString("id");

                                        if (ItemPosFromJSON.equals(item_pos + "")) {

                                            String shortDescription = jsonstepObject.getString("shortDescription");
                                            String description = jsonstepObject.getString("description");
                                            videoURL = jsonstepObject.getString("videoURL");

                                            if (!videoURL.isEmpty() && videoURL != null) {
                                                initializePlayer(Uri.parse(videoURL).buildUpon().build());
                                                vedioPlayer = true;

                                            } else {
                                                releasePlayer();
                                                mPlayerView.setVisibility(View.GONE);
                                                vedioPlayer = false;
                                            }

                                            if (item_pos == 0) {
                                                preStep.setVisibility(View.GONE);
                                            } else if (item_pos == jsonstepArray.length() - 1) {
                                                nextStep.setVisibility(View.GONE);
                                            } else {
                                                preStep.setVisibility(View.VISIBLE);
                                                nextStep.setVisibility(View.VISIBLE);
                                            }

                                            viewRecipeShortDescTxt.setText(shortDescription);
                                            viewRecipeDescriptionTxt.setText(description);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new RecipeFragment.MySessionCallback());

        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
        vedioPlayer = true;
        mExoPlayer.addListener(this);

        String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    private void releasePlayer() {
        if (vedioPlayer && mExoPlayer != null) {

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pre_step:
                if (item_pos >= 0) {

                    mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                            (getResources(), R.drawable.exo_controls_play));
                    initializeMediaSession();

                    builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();
                    recipeID = getActivity().getIntent().getStringExtra(getText(R.string.recipeID).toString());

                    item_pos = item_pos - 1;

                    getRecipe(builtUri, recipeID, item_pos);
                }
                break;
            case R.id.next_step:
                if (item_pos >= 0 && item_pos <= arraySize) {

                    mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                            (getResources(), R.drawable.exo_controls_play));
                    initializeMediaSession();

                    builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();

                    item_pos = item_pos + 1;
                    getRecipe(builtUri, recipeID, item_pos);
                    recipeID = getActivity().getIntent().getStringExtra(getText(R.string.recipeID).toString());
                    break;
                }
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }


}
