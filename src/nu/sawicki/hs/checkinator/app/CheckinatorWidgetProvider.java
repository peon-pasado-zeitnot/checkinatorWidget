package nu.sawicki.hs.checkinator.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import nu.sawicki.hs.checkinator.api.Checkins;
import nu.sawicki.hs.checkinator.api.Client;
import nu.sawicki.hs.checkinator.api.GetResponseCallback;
import nu.sawicki.hs.checkinator.api.User;

import java.util.ArrayList;


public class CheckinatorWidgetProvider extends AppWidgetProvider{

    private final static String CHECKINATOR_WIDGET_UPDATE = "nu.sawicki.hs.checkinator.CHECKINATOR_WIDGET_UPDATE";
    private final static String CHECKINATOR_WIDGET_BACK_BUTTON_PRESSED =  "nu.sawicki.hs.checkinator."
            + "BACK_BUTTON_PRESSED";
    private static final String CHECKINATOR_WIDGET_FORWARD_BUTTON_PRESSED = "nu.sawicki.hs.checkinator."
            + "FORWARD_BUTTON_PRESSED";

    private final static Client client = new Client();



    private PendingIntent createIntent(Context context, String intentName) {
        Intent intent = new Intent(intentName);
        return PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] ids) {
        for (int appWidgetID: ids) {
            updateAppWidget(context, appWidgetManager, appWidgetID);
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.checkinator);
            views.setOnClickPendingIntent(R.id.backButton, createIntent(context,
                    CHECKINATOR_WIDGET_BACK_BUTTON_PRESSED));
            views.setOnClickPendingIntent(R.id.backButton, createIntent(context,
                    CHECKINATOR_WIDGET_FORWARD_BUTTON_PRESSED));


        }


    }


    private static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                        final int appWidgetId) {

        Log.d("CheckinatorWidgetProvider","Entered update cycle");
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.checkinator);
        client.getCheckins(new GetResponseCallback() {
            @Override
            public void onDataReceived(Checkins checkins) {
                String errorDescription = checkins.getErrorsDescription();
                if (errorDescription != null) {
                    views.setTextViewText(R.id.chekinText, errorDescription);
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                    return;

                }

                //Log.d("CheckinatorWidgetProvider.updateAppWidget", String.valueOf(checkins.getUnknownDevices()));
                ArrayList<User> users = checkins.getUsers();
                if (users != null) {
                    if (!   users.isEmpty()){
                        views.setTextViewText(R.id.chekinText, users.get(0).getLogin());
                    }
                    else{
                        views.setTextViewText(R.id.chekinText,"This Beautiful Room Is Empty");
                    }
                } else {
                    views.setTextViewText(R.id.chekinText, "error: unknown error");
                }

                appWidgetManager.updateAppWidget(appWidgetId, views);

            }
        });

    }



    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("CheckinatorWidgetProvider.onEnabled","Widget Provider enabled.  Starting timer to update widget every ten minutes");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60 * 10/600, createIntent(context,
                CHECKINATOR_WIDGET_UPDATE));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d("CheckinatorWidgetProvider.onDisabled", "Widget Provider disabled. Turning off timer");
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createIntent(context, CHECKINATOR_WIDGET_UPDATE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("CheckinatorWidgetProvider.onReceive", "Received intent " + intent);
        if (CHECKINATOR_WIDGET_UPDATE.equals(intent.getAction())) {
            Log.d("CheckinatorWidgetProvider.onReceive", "wudget update");
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID: ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }


}