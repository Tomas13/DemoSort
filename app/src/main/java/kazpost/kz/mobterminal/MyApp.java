package kazpost.kz.mobterminal;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by root on 4/11/17.
 */

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
