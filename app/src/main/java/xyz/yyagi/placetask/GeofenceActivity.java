package xyz.yyagi.placetask;

/**
 * Created by yaginuma on 14/11/07.
 */

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;

public class GeofenceActivity extends FragmentActivity {

    private final GeofenceActivity self = this;
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        // LocationClient の生成
        mLocationClient = new LocationClient(this, mConnectionCallbacks, mOnConnectionFailedListener);
        mLocationClient.connect();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Toast.makeText(self, "onNewIntent", Toast.LENGTH_LONG).show();
        super.onNewIntent(intent);
        int transitionType = LocationClient.getGeofenceTransition(intent);
        int color = transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ? Color.GREEN : Color.RED;
        findViewById(android.R.id.content).setBackgroundColor(color);

        String text = transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ? "Success!!!!!!" : "Failture!!!!!!";
        TextView textView = (TextView)findViewById(R.id.geoText);
        textView.setText(text);
    }

    private void addGeofence() {
        Toast.makeText(self, "addGeofence", Toast.LENGTH_LONG).show();
        // Geofence の作成
        // 緯度
        double latitude = 35.808037;
        // 経度
        double longitude = 139.678042;
        // 半径(メートル)
        float radius = 1000;

        Geofence.Builder builder = new Geofence.Builder();
        builder.setRequestId("geo_fence");
        builder.setCircularRegion(latitude, longitude, radius);
        builder.setExpirationDuration(Geofence.NEVER_EXPIRE);
        builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

        ArrayList<Geofence> geofences = new ArrayList<Geofence>();
        geofences.add(builder.build());

        // PendingIntent の生成
        Intent intent = new Intent(self, GeofenceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Geofences の登録
        mLocationClient.addGeofences(geofences, pendingIntent, mOnAddGeofencesResultListener);
    }

    /**
     * 接続時・切断時のコールバック.
     */
    private GooglePlayServicesClient.ConnectionCallbacks mConnectionCallbacks = new GooglePlayServicesClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Toast.makeText(self, "onConnected", Toast.LENGTH_LONG).show();
            // Geofenceを登録
            addGeofence();
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(self, "onDisconnected", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 接続失敗時のコールバック.
     */
    private GooglePlayServicesClient.OnConnectionFailedListener mOnConnectionFailedListener = new GooglePlayServicesClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Toast.makeText(self, "onConnectionFailed", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Geofenceを登録したときのコールバック.
     */
    private LocationClient.OnAddGeofencesResultListener mOnAddGeofencesResultListener = new LocationClient.OnAddGeofencesResultListener() {
        @Override
        public void onAddGeofencesResult(int statusCode, String[] strings) {
            // TODO: status codeのチェック
            Toast.makeText(self, "onAddGeofencesResult statusCode: " + statusCode, Toast.LENGTH_LONG).show();
        }
    };
}
