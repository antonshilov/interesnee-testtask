package gq.vaccum121.testtask.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import gq.vaccum121.testtask.R;
import gq.vaccum121.testtask.service.SyncService;
import gq.vaccum121.testtask.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {
    public static final String EXTRA_TRIGGER_SYNC_FLAG =
            "gq.vaccum121.testtask.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    MainPresenter mMainPresenter;

    public static Intent getStartIntent(Context context, boolean triggerSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        boolean autoSyncEnabled = getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true);
        if (autoSyncEnabled) {
            startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }
}
