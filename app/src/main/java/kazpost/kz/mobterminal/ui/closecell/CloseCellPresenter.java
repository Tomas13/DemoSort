package kazpost.kz.mobterminal.ui.closecell;

import android.os.Bundle;

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

import static kazpost.kz.mobterminal.utils.AppConstants.BAG_TYPE;
import static kazpost.kz.mobterminal.utils.AppConstants.CLOSE_BAG_TIME;
import static kazpost.kz.mobterminal.utils.AppConstants.FROM_DEP;
import static kazpost.kz.mobterminal.utils.AppConstants.G_NUMBER;
import static kazpost.kz.mobterminal.utils.AppConstants.OPERATOR_NAME;
import static kazpost.kz.mobterminal.utils.AppConstants.SEAL_NUMBER;
import static kazpost.kz.mobterminal.utils.AppConstants.SEND_METHOD;
import static kazpost.kz.mobterminal.utils.AppConstants.TO_DEP;
import static kazpost.kz.mobterminal.utils.AppConstants.WEIGHT_RESPONSE;

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

    }


    @Override
    public void closeBagRequest(String bagBar, String sealNum, String weight) {

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


                            /*
                            <ns2:GNumber>G201701170001154</ns2:GNumber>
         <ns2:SealNumber>1234</ns2:SealNumber>
         <ns2:Weight>0.1</ns2:Weight>
         <ns2:FromDep>Участок сортировки п.Ботакара [802247]</ns2:FromDep>
         <ns2:ToDep>ЦОУ  г.Алматы [050000]</ns2:ToDep>
         <ns2:SendMethod>Наземный</ns2:SendMethod>
         <ns2:BagType>Мешок "Сақтандыру"</ns2:BagType>
         <ns2:OperatorName>Галина Боровик</ns2:OperatorName>
                            */

                            Envelope.CloseBagResponse closeBagResponse = envelope.getBody().getCloseBagResponse();

                            String gNumber = closeBagResponse.getGNumber();
                            String sealNumber = closeBagResponse.getSealNumber();
                            String weightResponse = closeBagResponse.getWeight();
                            String fromDep = closeBagResponse.getFromDep();
                            String toDep = closeBagResponse.getToDep();
                            String sendMethod = closeBagResponse.getSendMethod();
                            String bagType = closeBagResponse.getBagType();
                            String operatorName = closeBagResponse.getOperatorName();

                            Bundle bundle = new Bundle();
                            bundle.putString(G_NUMBER, gNumber);
                            bundle.putString(SEAL_NUMBER, sealNumber);
                            bundle.putString(WEIGHT_RESPONSE, weightResponse);
                            bundle.putString(FROM_DEP, fromDep);
                            bundle.putString(TO_DEP, toDep);
                            bundle.putString(SEND_METHOD, sendMethod);
                            bundle.putString(BAG_TYPE, bagType);
                            bundle.putString(OPERATOR_NAME, operatorName);

                            bundle.putString(CLOSE_BAG_TIME, closeBagResponse.getResponseInfo().getResponseGenTime());

                            getMvpView().openPrintActivity(bundle);

                            getMvpView().onErrorToast(text);

                            getMvpView().hideLoading();
                        },
                        throwable -> {
                            getMvpView().onErrorToast(throwable.getMessage());
                            getMvpView().hideLoading();
                        });


    }

}
