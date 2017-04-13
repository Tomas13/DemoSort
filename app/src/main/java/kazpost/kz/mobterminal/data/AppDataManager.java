package kazpost.kz.mobterminal.data;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import kazpost.kz.mobterminal.data.network.ApiHelper;
import kazpost.kz.mobterminal.data.prefs.PreferencesHelper;
import kazpost.kz.mobterminal.data.realm.RealmHelper;
import kazpost.kz.mobterminal.di.ApplicationContext;

/**
 * Created by root on 4/12/17.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
//    private final ApiHelper mApiHelper;


    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper
                          ) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
//        mApiHelper = apiHelper;
    }


    public Context getmContext() {
        return mContext;
    }


    public PreferencesHelper getmPreferencesHelper() {
        return mPreferencesHelper;
    }

}
