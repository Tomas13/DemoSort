package kazpost.kz.mobterminal.ui.print;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.ui.base.BaseActivity;

import static kazpost.kz.mobterminal.utils.AppConstants.BAG_TYPE;
import static kazpost.kz.mobterminal.utils.AppConstants.CLOSE_BAG_TIME;
import static kazpost.kz.mobterminal.utils.AppConstants.FROM_DEP;
import static kazpost.kz.mobterminal.utils.AppConstants.G_NUMBER;
import static kazpost.kz.mobterminal.utils.AppConstants.OPERATOR_NAME;
import static kazpost.kz.mobterminal.utils.AppConstants.PRINT_ACTIVITY;
import static kazpost.kz.mobterminal.utils.AppConstants.SEAL_NUMBER;
import static kazpost.kz.mobterminal.utils.AppConstants.SEND_METHOD;
import static kazpost.kz.mobterminal.utils.AppConstants.TO_DEP;
import static kazpost.kz.mobterminal.utils.AppConstants.WEIGHT_RESPONSE;

public class PrintActivity extends BaseActivity {


    @BindView(R.id.tv_g_number)
    TextView tvGNumber;
    @BindView(R.id.tv_seal_number)
    TextView tvSealNumber;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_operator_name)
    TextView tvOperatorName;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.tv_close_bag_time)
    TextView tvCloseBagTime;
    @BindView(R.id.tv_to_dep_index)
    TextView tvToDepIndex;
    @BindView(R.id.tv_to_dep_name)
    TextView tvToDepName;
    @BindView(R.id.ll_main_print)
    LinearLayout llMainPrint;
    @BindView(R.id.et_code_g)
    EditText etCodeG;
    @BindView(R.id.btn_back_closecell)
    Button btnBackClosecell;
    @BindView(R.id.btn_next_closecell)
    Button btnNextClosecell;
    @BindView(R.id.ll_print_buttons)
    LinearLayout llPrintButtons;

    @BindString(R.string.g_number)
    String gNumberTitle;
    @BindString(R.string.seal_number)
    String sealNumberTitle;
    @BindString(R.string.bag_weight)
    String weightTitle;
    @BindString(R.string.operator_name)
    String operatorTitle;
    @BindString(R.string.to_dep_index)
    String toDepIndexTitle;
    @BindString(R.string.to_dep_name)
    String toDepNameTitle;
    @BindString(R.string.bag_close_time)
    String closeTimeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getBundleExtra(PRINT_ACTIVITY);


        if (bundle != null) {
            String gNumber = bundle.getString(G_NUMBER, "G000000000");
            String sealNumber = bundle.getString(SEAL_NUMBER, "00000");
            String weightResponse = bundle.getString(WEIGHT_RESPONSE, "0");
            String fromDep = bundle.getString(FROM_DEP, "fromDep");
            String toDep = bundle.getString(TO_DEP, "toDep");
            String sendMethod = bundle.getString(SEND_METHOD, "sendMethod");
            String bagType = bundle.getString(BAG_TYPE, "bagType");
            String operatorName = bundle.getString(OPERATOR_NAME, "operatorName");
            String closeTime = bundle.getString(CLOSE_BAG_TIME, "00:00");
            String date2 = "date";

            SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
            try {
                Date date = formatter3.parse(closeTime);

                SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss", Locale.US);
                date2 = formatter2.format(date);

//                Toast.makeText(this, date2.toString(), Toast.LENGTH_SHORT).show();
                Log.d("PrintActivity: ", date2);

            } catch (ParseException e) {
                Log.d("PrintActivity: ", e.toString());

            }


            tvGNumber.setText(gNumberTitle + " " + gNumber);
            tvSealNumber.setText(sealNumberTitle + " " + sealNumber);
            tvWeight.setText(weightTitle + " " + weightResponse);
//            tvToDepIndex.setText(toDepIndexTitle + " " + toDep);
            tvToDepName.setText(toDepNameTitle + " " + toDep);
            tvCloseBagTime.setText(closeTimeTitle + " " + date2);
            tvOperatorName.setText(operatorTitle + " " + operatorName);

        }
    }
}
