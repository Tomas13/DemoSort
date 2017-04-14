package kazpost.kz.mobterminal.ui.login;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.ui.base.BasePresenter;
import kazpost.kz.mobterminal.utils.CommonUtils;

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
    public void onLoginBtnClicked(String pinCode) {

        if (CommonUtils.isPinValid(pinCode)) {
            getMvpView().openMainActivity();
        } else {
            getMvpView().onErrorToast("Pin not valid");
        }
    }


    private void showPin() {

        getMvpView().showPinEditText();
    }

}
