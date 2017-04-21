package kazpost.kz.mobterminal.ui.scanner;

import android.util.Log;

import javax.inject.Inject;

import kazpost.kz.mobterminal.data.DataManager;
import kazpost.kz.mobterminal.data.network.model.findplan.Envelope;
import kazpost.kz.mobterminal.data.network.model.findplan.FindPlanBody;
import kazpost.kz.mobterminal.data.network.model.findplan.FindPlanData;
import kazpost.kz.mobterminal.data.network.model.findplan.FindPlanEnvelope;
import kazpost.kz.mobterminal.ui.base.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 4/17/17.
 */

public class ScanPresenter<V extends ScanMvpView> extends BasePresenter<V> implements ScanMvpPresenter<V> {

    private static final String TAG = "ScanPresenter";


    @Inject
    public ScanPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onScan(String number) {

        getMvpView().showLoading();

        FindPlanEnvelope findPlanEnvelope = new FindPlanEnvelope();

        FindPlanBody findPlanBody = new FindPlanBody();
        FindPlanData findPlanData = new FindPlanData();
//        findPlanData.setParcelBarcode(number);
        findPlanData.setASessionId(getDataManager().getSessionId());
        findPlanData.setBParcelBarcode("RR460877842BY");

        findPlanBody.setFindPlanData(findPlanData);
        findPlanEnvelope.setFindPlanBody(findPlanBody);

        Observable<Envelope> observable = getDataManager().doFindPlan(findPlanEnvelope);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        envelope -> {
                            Envelope.ResponseInfo responseInfo = envelope.getBody().getFindPlanResponse().getResponseInfo();


                            if (responseInfo.getResponseCode().equals("0")) {

//                                String responseGenTime = responseInfo.getResponseGenTime();

                                getMvpView().showBagTrackNumber(envelope.getBody().getFindPlanResponse().getBagBarcode(),
                                        envelope.getBody().getFindPlanResponse().getBagNumber());

                            } else if (responseInfo.getResponseCode().equals("106")) {  //106 - session time expired

                                getMvpView().onErrorToast(responseInfo.getResponseText());

                            } else {
                                Log.d(TAG, "throwable " + responseInfo.getResponseText());

                                getMvpView().onErrorToast(responseInfo.getResponseText());
                            }

                            getMvpView().hideLoading();
                        },
                        throwable -> {
                            Log.d(TAG, "throwable " + throwable.getMessage());

                            getMvpView().onErrorToast(throwable.getMessage());
                            getMvpView().hideLoading();
                        });


    }

    private void showCell(String s) {
        getMvpView().onError(s);
        getMvpView().hideLoading();
    }
}