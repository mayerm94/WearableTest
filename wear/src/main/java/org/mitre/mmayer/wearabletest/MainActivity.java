package org.mitre.mmayer.wearabletest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.mitre.hossenlopp.buttongameshared.ArcView;

public class MainActivity extends Activity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String COUNT_KEY = "org.mitre.button.press";
    private static final String WATCH_COUNT_KEY = "org.mitre.watchbutton.press";


    private GoogleApiClient mGoogleApiClient;
    private int count = 1;
    private int theirCount = 1;

    private ArcView mTugbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);

        Button pushButton = (Button) findViewById(R.id.button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCountUpdate();
                updateCount();
            }
        });

        mTugbar = (ArcView) findViewById(R.id.tugbar);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void sendCountUpdate(){
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/watchcount");
        putDataMapReq.getDataMap().putInt(WATCH_COUNT_KEY, count++);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    @Override
    protected void onResume() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Log.d("YAY!","CONNECTED");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                /*byte[] dat = item.getData();
                for(int x=0;x<dat.length;x++){
                    Log.d("YAYDATA",Byte.toString(dat[x]));

                }*/
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    theirCount = dataMap.getInt(COUNT_KEY);
                    updateCount();
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    // Our method to update the count
    private void updateCount() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView coun = (TextView) findViewById(R.id.text);
                coun.setText(Integer.toString(theirCount));


                float precent = ((float) count)/((float) theirCount);


                mTugbar.setPrecentage(precent);

                coun.invalidate();
                mTugbar.invalidate();

            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
