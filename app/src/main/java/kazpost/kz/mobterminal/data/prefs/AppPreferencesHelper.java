package kazpost.kz.mobterminal.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import kazpost.kz.mobterminal.di.ApplicationContext;
import kazpost.kz.mobterminal.di.PreferenceInfo;

/**
 * Created by root on 4/12/17.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
}
