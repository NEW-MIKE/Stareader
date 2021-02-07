package com.kaya.stareader.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.kaya.stareader.R;
import com.kaya.stareader.StareaderApplication;
import com.kaya.stareader.data.local.prefs.PreferencesHelper;
import com.kaya.stareader.di.component.AppComponent;
import com.kaya.stareader.di.component.DaggerAppComponent;
import com.kaya.stareader.di.module.AppModule;
import com.kaya.stareader.utils.StatusBarCompat;
import com.kaya.stareader.views.loadding.CustomDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    public Toolbar mCommonToolbar;

    protected Context mContext;
    private Unbinder unbinder;
    protected int statusBarColor = 0;
    protected View statusBarView = null;
    private boolean mNowMode;
    private AppComponent mAppComponet;
    private CustomDialog dialog;//进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        if (statusBarColor == 0) {
            statusBarView = StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        } else if (statusBarColor != -1) {
            statusBarView = StatusBarCompat.compat(this, statusBarColor);
        }
        transparent19and20();
        mContext = this;
        unbinder = ButterKnife.bind(this);
        setupActivityComponent(StareaderApplication.getsInstance().getApplicationComponent());
        mCommonToolbar =  findViewById(R.id.common_toolbar);
        if (mCommonToolbar != null) {
            initToolBar();
            setSupportActionBar(mCommonToolbar);
        }
        initDatas();
        configViews();
        //mNowMode = SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT);
        mNowMode = ((StareaderApplication)getApplication()).getmAppPreferencesHelper().isDayMode();
        if (((StareaderApplication)getApplication()).getmAppPreferencesHelper().isDayMode()) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }

    protected void transparent19and20() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCommonToolbar.setElevation(elevation);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((StareaderApplication)getApplication()).getmAppPreferencesHelper().isDayMode() != mNowMode) {
            mNowMode = ((StareaderApplication)getApplication()).getmAppPreferencesHelper().isDayMode();
            if (((StareaderApplication)getApplication()).getmAppPreferencesHelper().isDayMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                System.out.println("Yes");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                System.out.println("No");
            }
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    //    dismissDialog();
    }

    public abstract int getLayoutId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract void initToolBar();

    public abstract void initDatas();
    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    // dialog
    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(this);
            dialog.setCancelable(true);
        }
        return dialog;
    }

    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showDialog() {
        getDialog().show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if(statusBarView != null){
           // statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if(statusBarView != null){
            statusBarView.setBackgroundColor(statusBarColor);
        }
    }

}
