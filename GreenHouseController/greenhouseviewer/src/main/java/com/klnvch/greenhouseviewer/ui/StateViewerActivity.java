package com.klnvch.greenhouseviewer.ui;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.ui.states.StateActivity;
import com.klnvch.greenhouseviewer.R;
import com.klnvch.greenhouseviewer.firestore.PhoneStateHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StateViewerActivity extends StateActivity {
    private Disposable disposable = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_state, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = AppDatabase.getInstance(this).getLatestPhoneStateTime("test")
                .flatMap(PhoneStateHelper::read)
                .flatMapCompletable(AppDatabase.getInstance(this)::insert)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(StateViewerActivity.this, "Success", Toast.LENGTH_LONG).show(),
                        throwable -> Toast.makeText(StateViewerActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
