package kazpost.kz.mobterminal.ui.login;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.ui.main.MainActivity;
import kazpost.kz.mobterminal.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        mPresenter.onAttach(LoginActivity.this);
        mPresenter.onLoginCodeScan();
    }


    @OnClick(R.id.btn_login_exit)
    public void onExitClicked() {

        finish();

    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
