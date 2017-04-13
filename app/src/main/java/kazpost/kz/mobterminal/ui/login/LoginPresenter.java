package kazpost.kz.mobterminal.ui.login;

import android.support.annotation.MainThread;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.ui.base.BasePresenter;

/**
 * Created by root on 4/12/17.
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }


    Disposable disposable;

    @Override
    public void onLoginCodeScan() {

        getMvpView().showLoading();

        Observable<Long> observable = Observable.interval(3, TimeUnit.SECONDS);
        disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            showPin();
        });

    }


    private void showPin() {
        getMvpView().onError("Hello");
        getMvpView().showPinEditText();
        getMvpView().hideLoading();
        disposable.dispose();
    }

}
