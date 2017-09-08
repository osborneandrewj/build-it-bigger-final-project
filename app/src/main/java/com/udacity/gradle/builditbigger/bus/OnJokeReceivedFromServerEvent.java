package com.udacity.gradle.builditbigger.bus;

/**
 * Created by Zark on 9/8/2017.
 *
 */

public class OnJokeReceivedFromServerEvent {

    public final String mJoke;

    public OnJokeReceivedFromServerEvent(String aJoke) {
        mJoke = aJoke;
    }
}
