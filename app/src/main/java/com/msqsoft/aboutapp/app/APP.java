package com.msqsoft.aboutapp.app;

import android.app.Application;

import com.msqsoft.aboutapp.R;
import com.orhanobut.logger.Logger;

import io.rong.imkit.RongIM;

/**
 * application
 */

public class APP extends Application {

    private static APP INStANCE;

    public APP() {
        INStANCE = this;
    }

    public static APP getInstance() {
        if (INStANCE == null) {
            synchronized (APP.class) {
                if (INStANCE == null) {
                    INStANCE = new APP();
                }
            }
        }
        return INStANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RongIM.init(this);
//        LeakCanary.install(getInstance());//内存泄漏监听

//        CrashReport.initCrashReport(getApplicationContext(), "c260d28228", false);//Bugly

        Logger.init(getString(R.string.app_name));

    }
}
