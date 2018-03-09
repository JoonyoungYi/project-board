package kr.ac.kaist.board.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by yearnning on 15. 1. 22..
 */
public class EmailManager {

    private Context mContext;
    private String mEmail;
    private String mTitle;
    private String mContent;

    public EmailManager(Context context, String email, String title, String content) {
        mContext = context;
        mEmail = email;
        mTitle = title;
        mContent = content;
    }

    public void startIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, mTitle);
        intent.putExtra(Intent.EXTRA_TEXT, mContent);
        if (installed("com.google.android.apps.inbox")) {
            intent.setPackage("com.google.android.apps.inbox");
        } else if (installed("com.google.android.gm")) {
            intent.setPackage("com.google.android.gm");
        }
        mContext.startActivity(Intent.createChooser(intent, "메일 보내기"));
    }

    private boolean installed(String uri) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = mContext.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(uri))
                return true;
        }
        return false;
    }
}
