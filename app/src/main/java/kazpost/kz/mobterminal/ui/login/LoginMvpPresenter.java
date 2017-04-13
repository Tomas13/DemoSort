package kazpost.kz.mobterminal.ui.login;

import kazpost.kz.mobterminal.di.PerActivity;
import kazpost.kz.mobterminal.ui.base.MvpPresenter;

/**
 * Created by root on 4/12/17.
 */

@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

//    void onServerLoginClick(String email, String password);

    void onLoginCodeScan();

}
