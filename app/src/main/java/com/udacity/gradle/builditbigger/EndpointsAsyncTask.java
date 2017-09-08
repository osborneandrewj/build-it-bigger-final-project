package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

//import com.example.zark.myapplication.backend.myApi.MyApi;
//import com.example.zark.myapplication.backend.myApi.MyApi;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.example.zark.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.squareup.otto.Bus;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.bus.OnJokeReceivedFromServerEvent;

/**
 * Created by Zark on 9/8/2017.
 *
 */

public class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {

    private MyApi myApiService = null;
    private Context context;
    private Bus mBus;

    @Override
    protected String doInBackground(Context... params) {

        mBus = BusProvider.getInstance();

        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                            request.setDisableGZipContent(true);
                        }
                    });
            myApiService = builder.build();
        }

        context = params[0];

        try {
            return myApiService.tellJoke().execute().getJoke();
        } catch (IOException e) {
            return e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String s) {
        mBus.post(new OnJokeReceivedFromServerEvent(s));

    }
}
