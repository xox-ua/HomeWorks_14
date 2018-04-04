package studio.coon.acynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

//static
class ValidHandler extends Handler {
    WeakReference< HandlerActivity > wrActivity;
    public ValidHandler(HandlerActivity activity) {
        wrActivity = new WeakReference< HandlerActivity >(activity);
    }

    @Override
    public void handleMessage(Message msg) {

        HandlerActivity activity = wrActivity.get();
        if (activity != null) {
            Bundle bundle = msg.getData();
            long timeS = bundle.getLong("TimeStart");
            long timeE = bundle.getLong("TimeEnd");
            activity.tvResult.setText(bundle.getString("Pass"));
            long differenceMS = timeE - timeS;
            activity.tvMilSec.setText(String.valueOf(differenceMS) + " мс");

        }
    }
}