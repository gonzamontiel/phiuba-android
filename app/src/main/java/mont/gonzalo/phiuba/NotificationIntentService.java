package mont.gonzalo.phiuba;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import mont.gonzalo.phiuba.api.DataFetcher;
import mont.gonzalo.phiuba.layout.ActivityContext;
import mont.gonzalo.phiuba.layout.EventsFragment;
import mont.gonzalo.phiuba.layout.MainActivity;
import mont.gonzalo.phiuba.model.Event;

/**
 * Created by Gonzalo Montiel on 7/12/17.
 */
public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityContext.get());
        SharedPreferences.Editor prefsEditor = prefs.edit();

        String keywords = prefs.getString("event_keywords", "");
        String[] keywordsArr = keywords.split(" ");

        List<Event> eventList =  DataFetcher.getInstance().searchEventsSync(keywords);
        Integer lastSearchedEventsLength = prefs.getInt("last_searched_events", 0);

        Log.d("Settings", "fetch data del server y trajo " + eventList.size() +  " resultados, antes había " + lastSearchedEventsLength);

        if (eventList.size() > lastSearchedEventsLength) {
            prefsEditor.putInt("last_searched_events", eventList.size());
            prefsEditor.commit();
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle("Hay novedades de la FIUBA que te pueden interesar!")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.accent, null))
                    .setContentText("Hay nuevos eventos que coinciden con: " + keywords)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp);


            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtra(MainActivity.SPECIFIC_FRAGMENT, EventsFragment.class);
            mainActivityIntent.putExtra(MainActivity.SPECIFIC_FRAGMENT_DATA, keywords);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    mainActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}