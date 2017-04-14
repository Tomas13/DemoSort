package kazpost.kz.mobterminal.ui.closecell;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.ui.base.BaseActivity;

public class CloseCellActivity extends BaseActivity implements CloseCellMvpView {

    @BindView(R.id.et_code_bag)
    EditText etCodeBag;
    @BindView(R.id.et_number_seal)
    EditText etNumberSeal;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.btn_back_closecell)
    Button btnBackClosecell;
    @BindView(R.id.btn_further_closecell)
    Button btnFurtherClosecell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_cell);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back_closecell, R.id.btn_further_closecell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back_closecell:
                super.onBackPressed();
                break;
            case R.id.btn_further_closecell:
                break;
        }
    }
}
