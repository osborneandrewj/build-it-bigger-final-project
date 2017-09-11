package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.JokeTellerClass;
import com.example.android.jokedisplay.JokeDisplay;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.bus.OnJokeReceivedFromServerEvent;
import com.udacity.gradle.builditbigger.idlingResources.SimpleIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {

    private static final String KEY_JOKE = "key-joke";
    private Bus mBus = BusProvider.getInstance();
    @Nullable private SimpleIdlingResource mIdlingResource;


    @BindView(R.id.btn_joke) Button mJokeButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        mJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }

                //new EndpointsAsyncTask().execute(getContext());
                new EndpointsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getContext());
            }
        });

        return root;
    }

    @Subscribe
    public void onNewJokeReceived(OnJokeReceivedFromServerEvent event) {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
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
