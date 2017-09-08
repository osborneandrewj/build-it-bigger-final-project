package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.JokeTellerClass;
import com.example.android.jokedisplay.JokeDisplay;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.bus.OnJokeReceivedFromServerEvent;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String KEY_JOKE = "key-joke";
    private Bus mBus = BusProvider.getInstance();


    @BindView(R.id.btn_joke) Button mJokeButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);

        mJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Pass the joke from jokeTellerClass to JokeDisplay

                //intent.putExtra(KEY_JOKE, joke);
                //startActivity(intent);
                new EndpointsAsyncTask().execute(getContext());
            }
        });

        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    @Subscribe
    public void onNewJokeReceived(OnJokeReceivedFromServerEvent event) {
        startJokeDisplayActivity(event.mJoke);
    }

    public void startJokeDisplayActivity(String joke) {
        Intent intent = new Intent(getContext(), JokeDisplay.class);
        intent.putExtra(KEY_JOKE, joke);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }
}
