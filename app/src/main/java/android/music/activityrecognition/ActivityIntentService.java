package android.music.activityrecognition;

import java.util.ArrayList;
import java.lang.reflect.Type;

import android.annotation.SuppressLint;
import android.content.Context;
import com.google.gson.Gson;
import android.content.Intent;
import android.app.IntentService;
import android.preference.PreferenceManager;
import android.content.res.Resources;
import com.google.gson.reflect.TypeToken;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityIntentService extends IntentService {
    protected static final String TAG = "Activity";

    public ActivityIntentService() {
        super(TAG);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(MainActivity.DETECTED_ACTIVITY,
                            detectedActivitiesToJson(detectedActivities))
                    .apply();

        }
    }

    @SuppressLint("StringFormatInvalid")
    static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.vehicle);
            default:
                return resources.getString(R.string.unknown_activity, detectedActivityType);
        }
    }
    static final int[] POSSIBLE_ACTIVITIES = {

            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };
    static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        return new Gson().toJson(detectedActivitiesList, type);
    }
    static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
        Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        if (detectedActivities == null) {
            detectedActivities = new ArrayList<>();
        }
        return detectedActivities;
    }
}
