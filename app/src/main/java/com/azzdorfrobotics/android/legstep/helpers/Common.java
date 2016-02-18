package com.azzdorfrobotics.android.legstep.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.azzdorfrobotics.android.legstep.BaseActivity;
import com.azzdorfrobotics.android.legstep.R;
import com.azzdorfrobotics.android.legstep.model.Creature;
import com.azzdorfrobotics.android.legstep.model.Podium;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created on 17.02.2016
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Common {

    public static final String PREFS_GENERAL = "prefsGeneral";
    public static final String GENERAL_LANG = "generalLang";
    public static final String GENERAL_COUNTRY = "generalCountry";
    public static final String GENERAL_CREATURE = "generalCreature";
    public static final String GENERAL_PODIUM = "generalPodium";

    public static final int INVALID_ID = -1;

    public static Common.UserLang sUserLang = null;

    /**
     * System supported languages
     * add by android language code, not country code!!!
     */
    public enum UserLang {
        EN("en"), RU("ru"), UA("uk");

        private final String lang;
        private static final UserLang defaultUserLang = EN;

        private UserLang(String lang) {
            this.lang = lang;
        }

        public String getLang() {
            return this.lang;
        }

        public static UserLang fromString(String text) {
            if (text != null) {
                for (UserLang userLang : UserLang.values()) {
                    if (text.equalsIgnoreCase(userLang.lang)) {
                        return userLang;
                    }
                }
            }
            return defaultUserLang;
        }

        @Override
        public String toString() {
            return getLang();
        }
    }

    /**
     * Set locale to settings, or write def system locale
     *
     * @param context
     */
    public static void setLocale(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_GENERAL, Context.MODE_PRIVATE);
        String lang = settings.getString(GENERAL_LANG, context.getResources().getConfiguration().locale.getLanguage());
        String country = settings.getString(GENERAL_COUNTRY, context.getResources().getConfiguration().locale.getCountry());
        Locale locale = new Locale(lang, country); // setting or phone locale
        sUserLang = UserLang.fromString(lang);
        Locale extraLocale = null;
        if (intent.hasExtra(BaseActivity.EXTRA_LANG)) {
            UserLang userLang = UserLang.fromString(intent.getStringExtra(BaseActivity.EXTRA_LANG));
            if (userLang != null) {
                switch (userLang) {
                    case EN:
                        extraLocale = new Locale("en", "US");
                        break;
                    case RU:
                        extraLocale = new Locale("ru", "RU");
                        break;
                    case UA:
                        extraLocale = new Locale("uk", "UA");
                        break;
                }
            }
        }
        if (!locale.equals(extraLocale) && extraLocale != null) {
            locale = extraLocale;
            // save new extra
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(GENERAL_LANG, extraLocale.getLanguage());
            editor.putString(GENERAL_COUNTRY, extraLocale.getCountry());
            editor.commit();
        }
        // set exact locale
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        sUserLang = UserLang.fromString(locale.getLanguage());
        res.updateConfiguration(conf, dm);
    }

    /**
     * Serialized JSONed Creature to general settings
     *
     * @param context  application context
     * @param creature exact Creature to serialized
     */
    public static void setCreature(Context context, Creature creature) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_GENERAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String creatureJSON = gson.toJson(creature);
        editor.putString(GENERAL_CREATURE, creatureJSON);
        editor.commit();
    }

    /**
     * Get saved Creature or default one
     *
     * @param context
     * @return seaved Creature or default
     */
    public static Creature getCreature(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_GENERAL, Context.MODE_PRIVATE);
        String creatureJSON = settings.getString(GENERAL_CREATURE, "");
        Gson gson = new Gson();
        Creature creature = gson.fromJson(creatureJSON, Creature.class);
        if (creature == null) {
            creature = new Creature();
            creature.setDefault();
        }
        return creature;
    }

    /**
     * Serialized JSONed Podium to general settings
     *
     * @param context application context
     * @param podium  exact Podium to serialized
     */
    public static void setPodium(Context context, Podium podium) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_GENERAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String podiumJSON = gson.toJson(podium);
        editor.putString(GENERAL_PODIUM, podiumJSON);
        editor.commit();
    }

    /**
     * Get saved Podium or default one
     *
     * @param context
     * @return saved Podium or default
     */
    public static Podium getPodium(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_GENERAL, Context.MODE_PRIVATE);
        String podiumJSON = settings.getString(GENERAL_PODIUM, "");
        Gson gson = new Gson();
        Podium podium = gson.fromJson(podiumJSON, Podium.class);
        if (podium == null) {
            podium = new Podium();
            podium.setDefault();
        }
        return podium;
    }


    public static String formatString(String format, Object... args) {
        return String.format(format, args);
    }

    public static String formatString(int resId, Object... args) {
        return formatString(BaseActivity.getContext().getResources().getString(resId), args);
    }

    public static void showToast(String text, int length) {
        Toast.makeText(BaseActivity.getContext(), text, length).show();
    }

    public static void showToast(int resId, int length) {
        showToast(formatString(resId), length);
    }

    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) { // 21
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) { // 23
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static float roundToHalf(float x) {
        return (float) (Math.ceil(x * 2) / 2);
    }


    /**
     * Returns system wide date format
     *
     * @return
     */
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", BaseActivity.getContext().getResources().getConfiguration().locale);
    }

    /**
     * Datetime format
     *
     * @return
     */
    public static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", BaseActivity.getContext().getResources().getConfiguration().locale);
    }

    /**
     * Datetime format with days of week
     *
     * @return
     */
    public static SimpleDateFormat getDateTimeWithDayOfWeekFormat() {
        return new SimpleDateFormat("EEEE (MM-dd) - HH:mm", BaseActivity.getContext().getResources().getConfiguration().locale);
    }

    /**
     * Datetime format with days of week
     * cut day of week for labels like Today, Tomorrow, etc.
     *
     * @return
     */
    public static SimpleDateFormat getDateTimeWithDayOfWeekCutFormat() {
        return new SimpleDateFormat("(MM-dd) - HH:mm", BaseActivity.getContext().getResources().getConfiguration().locale);
    }

    /**
     * Time format
     *
     * @return
     */
    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm", BaseActivity.getContext().getResources().getConfiguration().locale);
    }

    /**
     * Returns date in words (today, tomorrow, etc.)
     *
     * @param date
     * @return
     */
    public static String dateToWords(Calendar date) {
        // TODAY
        if (DateUtils.isToday(date)) {
            return Common.formatString(R.string.date_today);
        }
        // TOMORROW
        if (DateUtils.isTomorrow(date)) {
            return Common.formatString(R.string.date_tomorrow);
        }
        return getDateFormat().format(date.getTime());
    }

    public static String dateFormatWithDayOfWeek(Calendar date) {
        // TODAY
        if (DateUtils.isToday(date)) {
            return Common.formatString(R.string.date_today) + " " + Common.getDateTimeWithDayOfWeekCutFormat().format(date.getTime());
        }
        // TOMORROW
        if (DateUtils.isTomorrow(date)) {
            return Common.formatString(R.string.date_tomorrow) + " " + Common.getDateTimeWithDayOfWeekCutFormat().format(date.getTime());
        }

        return Common.getDateTimeWithDayOfWeekFormat().format(date.getTime());
    }


}
