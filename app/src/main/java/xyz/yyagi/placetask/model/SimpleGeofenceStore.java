package xyz.yyagi.placetask.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yaginuma on 14/11/04.
 */
public class SimpleGeofenceStore {
    // Keys for flattened geofences stored in SharedPreferences
    public static final String PACKAGE = "xyz.yyagi.placestask.model";
    public static final String KEY_LATITUDE = PACKAGE + "KEY_LATITUDE";
    public static final String KEY_LONGITUDE = PACKAGE + "KEY_LONGITUDE";
    public static final String KEY_RADIUS = PACKAGE + "KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION = PACKAGE + "KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE = PACKAGE + "KEY_TRANSITION_TYPE";
    // The prefix for flattened activity_geofence keys
    public static final String KEY_PREFIX = PACKAGE + "KEY";

    /*
     * Invalid values, used to test activity_geofence storage when
     * retrieving geofences
     */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
    // The SharedPreferences object in which geofences are stored
    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences
    private static final String SHARED_PREFERENCES = "SharedPreferences";


    // Create the SharedPreferences storage with private access only
    public SimpleGeofenceStore(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Returns a stored activity_geofence by its id, or returns null
     * if it's not found.
     *
     * @param id The ID of a stored activity_geofence
     * @return A activity_geofence defined by its center and radius. See
     */
    public SimpleGeofence getGeofence(String id) {
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE), INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS), INVALID_FLOAT_VALUE);
        long expirationDuration = mPrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION), INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE), INVALID_INT_VALUE);
        if (lat != INVALID_FLOAT_VALUE && lng != INVALID_FLOAT_VALUE &&
            radius != INVALID_FLOAT_VALUE && expirationDuration != INVALID_LONG_VALUE &&
            transitionType != INVALID_INT_VALUE) {

            // Return a true Geofence object
            return new SimpleGeofence(id, lat, lng, radius, expirationDuration, transitionType);
        } else {
            return null;
        }
    }
    /**
     * Save a activity_geofence.
     * @param geofence The SimpleGeofence containing the
     * values you want to save in SharedPreferences
     */
    public void setGeofence(String id, SimpleGeofence geofence) {
        SharedPreferences.Editor editor = mPrefs.edit();
        // Write the Geofence values to SharedPreferences
        editor.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) geofence.getLatitude());
        editor.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) geofence.getLongitude());
        editor.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), geofence.getRadius());
        editor.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION), geofence.getExpirationDuration());
        editor.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE), geofence.getTransitionType());
        // Commit the changes
        editor.commit();
    }

    public void clearGeofence(String id) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
        editor.remove(getGeofenceFieldKey(id, KEY_RADIUS));
        editor.remove(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION));
        editor.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
        editor.commit();
    }

    /**
     * Given a Geofence object's ID and the name of a field
     * (for example, KEY_LATITUDE), return the key name of the
     * object's values in SharedPreferences.
     *
     * @param id The ID of a Geofence object
     * @param fieldName The field represented by the key
     * @return The full key name of a value in SharedPreferences
     */
    private String getGeofenceFieldKey(String id, String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }
}
