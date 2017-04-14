package kazpost.kz.mobterminal.ui.scanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import kazpost.kz.mobterminal.R;

public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.et_scan_activity)
    EditText etScanActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
    }
}
