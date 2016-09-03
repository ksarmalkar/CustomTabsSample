package com.sakebook.android.sample.customtabssample.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

/**
 * Created by sakemotoshinya on 16/07/28.
 */
public class CustomTabsUtil {

    public static void launchCustomTabs(Activity activity, String url, @Nullable CustomTabsSession session) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            // Supported Chrome is none.
            // Fail back
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url + "?timestamp=" + SystemClock.currentThreadTimeMillis())));
        }
        CustomTabsIntent customTabsIntent;
        if (session == null) {
            customTabsIntent = new CustomTabsIntent.Builder().build();
        } else {
            customTabsIntent = new CustomTabsIntent.Builder(session).build();
        }
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app:" + "//" + BuildConfig.APPLICATION_ID + "/http/example.com"));
//                Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + BuildConfig.APPLICATION_ID));
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static void launchCustomTabsWithBottombar(Activity activity, String url, @Nullable CustomTabsSession session) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            // Supported Chrome is none.
            // Fail back
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url + "?timestamp=" + SystemClock.currentThreadTimeMillis())));
        }
        CustomTabsIntent.Builder builder;
        if (session == null) {
            builder = new CustomTabsIntent.Builder();
        } else {
            builder = new CustomTabsIntent.Builder(session);
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_VIEW);
        shareIntent.setData(Uri.parse("http://google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 100, shareIntent, 0);
//        builder.addToolbarItem(1, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher), "dec", pendingIntent);

        int[] ids = {R.id.remote_text};
//        builder.setSecondaryToolbarViews(CustomBottombarActivity.createRemoteViews(), ids, pendingIntent);


        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.ic_menu_share);
        builder.addToolbarItem(1, icon, "1", pendingIntent);
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();

        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static void launchCustomTabsWithOldBottombar(Activity activity, String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            // Supported Chrome is none.
            // Fail back
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url + "?timestamp=" + SystemClock.currentThreadTimeMillis())));
        }
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        Intent browseIntent = new Intent();
        browseIntent.setAction(Intent.ACTION_VIEW);
        browseIntent.setData(Uri.parse(url));
        PendingIntent pendingBrowseIntent = PendingIntent.getActivity(activity, 100, browseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.ic_menu_view);
        builder.addToolbarItem(1, icon, "view", pendingBrowseIntent);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingShareIntent = PendingIntent.getActivity(activity, 101, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icons = BitmapFactory.decodeResource(activity.getResources(), android.R.drawable.ic_menu_share);
        builder.addToolbarItem(2, icons, "share", pendingShareIntent);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

}
