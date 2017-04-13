package kazpost.kz.mobterminal.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.ui.base.BaseActivity;
import kazpost.kz.mobterminal.ui.scanner.ScannerActivity;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;
    @BindView(R.id.btn_sort)
    Button btnSort;
    @BindView(R.id.btn_close_cell)
    Button btnCloseCell;
    @BindView(R.id.btn_config_printer)
    Button btnConfigPrinter;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);
        mPresenter.onAttach(MainActivity.this);

    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    public void startActivity1(Activity activity) {
        startActivity(new Intent(MainActivity.this,  activity.getClass()));
    }


    @Override
    public void openScanActivity() {

    }

    @Override
    public void openConfigPrinter() {

    }

    @Override
    public void closeCell() {

    }

    @OnClick({R.id.btn_sort, R.id.btn_close_cell, R.id.btn_config_printer, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sort:
                mPresenter.onSortBtnClicked();
                startActivity1(new ScannerActivity());
                break;
            case R.id.btn_close_cell:
                mPresenter.onCloseCellBtnClicked();
                break;
            case R.id.btn_config_printer:
                mPresenter.onConfigPrinterBtnClicked();
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}
