package kazpost.kz.mobterminal.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.data.network.model.request.AuthRequestBody;
import kazpost.kz.mobterminal.data.network.model.request.AuthRequestData;
import kazpost.kz.mobterminal.data.network.model.request.RequestEnvelope;
import kazpost.kz.mobterminal.ui.base.BaseActivity;
import kazpost.kz.mobterminal.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.et_login)
    EditText etLogin;
    @BindString(R.string.enter_your_pin)
    String enterPin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_code)
    EditText etCode;

    String userBarcode, userPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        mPresenter.onAttach(LoginActivity.this);
    }


    @OnTextChanged(R.id.et_code)
    public void onCodeScanned() {

        userBarcode = etLogin.getText().toString();
//        Toast.makeText(this, etCode.getText(), Toast.LENGTH_SHORT).show();

        //show edittext for entering pin
        mPresenter.onLoginCodeScan();
    }


    @OnClick(R.id.btn_login_exit)
    public void onExitClicked() {
        finish();
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {

        userPin = etCode.getText().toString();

        RequestEnvelope requestEnvelope = new RequestEnvelope();
        AuthRequestBody authRequestBody = new AuthRequestBody();
        AuthRequestData authRequestData = new AuthRequestData();
        authRequestData.setUserBarcode(userBarcode);
        authRequestData.setUserPin(userPin);
        authRequestBody.setAuthRequestData(authRequestData);

        requestEnvelope.setAuthRequestBody(authRequestBody);
        mPresenter.onLoginBtnClicked(requestEnvelope);
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPinEditText() {
        showKeyboard();
        etCode.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        etLogin.setVisibility(View.VISIBLE);

        etLogin.requestFocus();
        tvLogin.setText(enterPin);
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
