package kazpost.kz.mobterminal.ui.closecell;

import javax.inject.Inject;

import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.data.network.model.closebag.CloseBagBody;
import kazpost.kz.mobterminal.data.network.model.closebag.CloseBagData;
import kazpost.kz.mobterminal.data.network.model.closebag.CloseBagEnvelope;
import kazpost.kz.mobterminal.data.network.model.closebag.Envelope;
import kazpost.kz.mobterminal.ui.base.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 4/14/17.
 */

public class CloseCellPresenter<V extends CloseCellMvpView> extends BasePresenter<V> implements CloseCellMvpPresenter<V> {

    @Inject
    public CloseCellPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void openPrintActivity() {
        getMvpView().openPrintActivity();
    }


    @Override
    public void closeBagRequest(String bagBar, String sealNum, int weight) {

        getMvpView().showLoading();

        CloseBagEnvelope closeBagEnvelope = new CloseBagEnvelope();
        CloseBagBody closeBagBody = new CloseBagBody();
        CloseBagData closeBagData = new CloseBagData();

        closeBagData.setASessionId(getDataManager().getSessionId());
        closeBagData.setBBagBarcode(bagBar);
        closeBagData.setCSealNumber(sealNum);
        closeBagData.setDWeight(weight);

        closeBagBody.setCloseBagData(closeBagData);
        closeBagEnvelope.setCloseBagBody(closeBagBody);

        Observable<Envelope> observable = getDataManager().doCloseBag(closeBagEnvelope);
        observable
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(envelope -> {
                            String text = envelope.getBody().getCloseBagResponse().getResponseInfo().getResponseText();

                            getMvpView().onErrorToast(text);

                            getMvpView().hideLoading();
                        },
                        throwable -> {
                            getMvpView().onErrorToast(throwable.getMessage());
                            getMvpView().hideLoading();
                        });


    }

}
