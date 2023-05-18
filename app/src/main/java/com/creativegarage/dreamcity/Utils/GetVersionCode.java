package com.creativegarage.dreamcity.Utils;

import static com.creativegarage.dreamcity.network.Constants.CURRENT_VERSION;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;

import com.creativegarage.dreamcity.VersionListner;

public class GetVersionCode extends AsyncTask<Void, String, String> {
    private Activity activity;
    private VersionListner versionListner;

    public GetVersionCode(Activity activity, VersionListner versionListner) {
        this.activity = activity;
        this.versionListner = versionListner;
    }


    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(5)
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }

    @Override
    protected void onPostExecute(String onlineVersion) {
        super.onPostExecute(onlineVersion);
        if (onlineVersion != null && !onlineVersion.isEmpty()) {
            if (Float.valueOf(CURRENT_VERSION) < Float.valueOf(onlineVersion)) {
                //  Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
            }
        }
        Log.d("ApplicationVersions", "Current version " + CURRENT_VERSION + "playstore version " + onlineVersion);
    }}