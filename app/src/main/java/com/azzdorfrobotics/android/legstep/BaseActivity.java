package com.azzdorfrobotics.android.legstep;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.azzdorfrobotics.android.legstep.helpers.Common;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_LANG = "com.azzdorfrobotics.android.legstep.USER_LANG";

    public static Context applicationContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (applicationContext == null || getIntent().hasExtra(EXTRA_LANG)) {
            Common.setLocale(this.getApplicationContext(), getIntent());
            applicationContext = this.getApplicationContext();
        }

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public static Context getContext() {
        return applicationContext;
    }

    /**
     * Get user locale
     * could be null
     *
     * @return
     */
    private static Common.UserLang getUserLang() {
        return Common.sUserLang;
    }

    /**
     * Get user locale
     *
     * @return user locale
     */
    public Common.UserLang getUserLang(Context context) {
        if (getUserLang() == null) {
            Common.setLocale(context, null);
        }
        return Common.sUserLang;
    }
}
