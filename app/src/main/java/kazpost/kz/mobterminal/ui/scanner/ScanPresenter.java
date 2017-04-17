package kazpost.kz.mobterminal.ui.scanner;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.ui.base.BasePresenter;

/**
 * Created by root on 4/17/17.
 */

public class ScanPresenter<V extends ScanMvpView> extends BasePresenter<V> implements ScanMvpPresenter<V> {

    @Inject
    public ScanPresenter(DataManager dataManager) {
        super(dataManager);
    }

    Disposable disposable;

    @Override
    public void onScan(String number) {

        getMvpView().showLoading();

        Observable<Long> observable = Observable.interval(3, TimeUnit.SECONDS);
        disposable = observable.observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            showCell(number);
        });

    }

    private void showCell(String s) {
        getMvpView().onError(s);
        getMvpView().hideLoading();
        disposable.dispose();
    }
}