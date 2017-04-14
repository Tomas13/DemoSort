package kazpost.kz.mobterminal.ui.scanner;

import kazpost.kz.mobterminal.di.PerActivity;
import kazpost.kz.mobterminal.ui.base.MvpPresenter;

/**
 * Created by root on 4/14/17.
 */

@PerActivity
public interface ScanMvpPresenter<V extends ScanMvpView> extends MvpPresenter<V> {
}
