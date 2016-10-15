package com.sakebook.android.sample.customtabssample.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.receivers.BottombarBroadcastReceiver;
import com.sakebook.android.sample.customtabssample.receivers.SessionBottombarBroadcastReceiver;
import com.sakebook.android.sample.customtabssample.ui.CustomBottombar;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

/**
 * Created by sakemotoshinya on 16/07/28.
 */
public class CustomTabsUtil {

    public static void launchCustomTabs(Activity activity, String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setPackage(packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                    Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + BuildConfig.APPLICATION_ID + "/http/example.com"));
        } else {
            customTabsIntent.intent.putExtra("android.intent.extra.REFERRER",
                    Uri.parse("android-app:" + "//" + BuildConfig.APPLICATION_ID + "/http/example.com"));
        }
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    public static void launchCustomTabsWithDeprecatedBottombar(Activity activity, String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        addDeprecatedToolbar(builder, activity, url);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private static void addDeprecatedToolbar(CustomTabsIntent.Builder builder, Activity activity, String url) {
        Intent browseIntent = new Intent();
        browseIntent.setAction(Intent.ACTION_VIEW);
        browseIntent.setData(Uri.parse(url));
        PendingIntent pendingBrowseIntent = PendingIntent.getActivity(activity, 100, browseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_open_in_browser_black);
        builder.addToolbarItem(1, icon, "view", pendingBrowseIntent);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingShareIntent = PendingIntent.getActivity(activity, 101, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icons = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_share_black);
        builder.addToolbarItem(2, icons, "share", pendingShareIntent);
    }


    public static void launchCustomTabsWithBottombar(Activity activity, String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        addBottombar(builder, activity, url);
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private static void addBottombar(CustomTabsIntent.Builder builder, Activity activity, String url) {
        Intent broadcastIntent = new Intent(activity, BottombarBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 110, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int[] ids = {R.id.twitter, R.id.line, R.id.facebook};
        builder.setSecondaryToolbarViews(CustomBottombar.createBottombar(), ids, pendingIntent);
        builder.setShowTitle(true);
    }

    public static void launchCustomTabsWithSessionBottombar(Activity activity, String url, @Nullable CustomTabsSession session) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder;
        if (session == null) {
            builder = new CustomTabsIntent.Builder();
        } else {
            builder = new CustomTabsIntent.Builder(session);
        }
        addSessionBottombar(builder, activity, url);

        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private static void addSessionBottombar(CustomTabsIntent.Builder builder, Activity activity, String url) {
        Intent broadcastIntent = new Intent(activity, SessionBottombarBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 120, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int[] ids = {R.id.image_favorite};
        builder.setSecondaryToolbarViews(CustomBottombar.createSessionBottombar(), ids, pendingIntent);
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
    }
}
