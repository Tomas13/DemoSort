package kazpost.kz.mobterminal.ui.scanner;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.ui.base.BaseActivity;

public class ScanActivity extends BaseActivity implements ScanMvpView {

    @Inject
    ScanMvpPresenter<ScanMvpView> presenter;

    @BindView(R.id.tv_scan_top)
    TextView tvScanTop;
    @BindView(R.id.tv_tracknumber)
    TextView tvTracknumber;
    @BindView(R.id.ll_found_plan)
    LinearLayout llFoundPlan;
    @BindView(R.id.tv_scan_bag)
    TextView tvScanBag;
    @BindView(R.id.et_scan_activity)
    EditText etScanActivity;

    @BindString(R.string.cell_tracknumber)
    String cellTrack;
    @BindView(R.id.tv_bag_barcode)
    TextView tvBagBarcode;
    @BindView(R.id.tv_bag_number)
    TextView tvBagNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);
        presenter.onAttach(ScanActivity.this);

    }

    @OnTextChanged(R.id.et_scan_activity)
    public void onScan() {
        presenter.onScan(etScanActivity.getText().toString());
    }

    @Override
    public void clearEditText() {
        etScanActivity.setText("");
    }

    @Override
    public void showBagTrackNumber(String bagBarcode, String bagNumber) {

        tvScanTop.setText(cellTrack);
        llFoundPlan.setVisibility(View.VISIBLE);
        tvTracknumber.setVisibility(View.VISIBLE);

        tvBagBarcode.setText(bagBarcode);
        tvBagNumber.setText(bagNumber);
    }
}
