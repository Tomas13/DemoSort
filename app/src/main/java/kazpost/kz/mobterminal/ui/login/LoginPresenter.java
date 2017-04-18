package kazpost.kz.mobterminal.ui.login;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.data.network.model.Envelope;
import kazpost.kz.mobterminal.data.network.model.request.AuthRequestBody;
import kazpost.kz.mobterminal.data.network.model.request.AuthRequestData;
import kazpost.kz.mobterminal.data.network.model.request.RequestEnvelope;
import kazpost.kz.mobterminal.ui.base.BasePresenter;
import kazpost.kz.mobterminal.utils.CommonUtils;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by root on 4/12/17.
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onLoginCodeScan() {
        showPin();
    }

    @Override
    public void onLoginBtnClicked(RequestEnvelope requestEnvelope) {

        getMvpView().showLoading();

        Observable<Envelope> observable = getDataManager().doAuthorizeOnServer(requestEnvelope);

        observable
                .subscribeOn(Schedulers.io())
                .subscribe(
                        envelope -> {
                            Log.d(TAG, "GEN " + envelope.getBody().getAuthorizeResponse().getResponseInfo().getResponseGenTime());
                            getMvpView().hideLoading();
                        },
                        throwable -> {
                            Log.d(TAG, "throwable " + throwable.getMessage());
                            getMvpView().hideLoading();
                        });



        /*if (CommonUtils.isPinValid(requestEnvelope)) {
            getMvpView().openMainActivity();
        } else {
            getMvpView().onErrorToast("Неверный пин-код");
        }*/
    }


    private void showPin() {
        getMvpView().showPinEditText();
    }

}
