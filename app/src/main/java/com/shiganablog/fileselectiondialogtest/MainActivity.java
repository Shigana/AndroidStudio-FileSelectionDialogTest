package com.shiganablog.fileselectiondialogtest;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnFileSelectedListener, View.OnClickListener {

    /**
     * デフォルトシリアルバージョン ID。
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViewById(R.id.button1).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // ダイアログフラグメントを設定
        DialogFragment dialogFragment = new FileSelectDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FileSelectDialogFragment.ROOT_DIRECTORY, "/");
        bundle.putString(FileSelectDialogFragment.INITIAL_DIRECTORY, Environment.getExternalStorageDirectory().getPath());
        bundle.putString(FileSelectDialogFragment.PREVIOUS, "..");
        bundle.putString(FileSelectDialogFragment.CANCEL, "キャンセル");
        bundle.putSerializable(FileSelectDialogFragment.LISTENER, this);
        dialogFragment.setArguments(bundle);
        dialogFragment.setCancelable(false);
        dialogFragment.show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onFileSelected(String path) {
        Toast.makeText(this, path, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFileSelectCanceled() {
        Toast.makeText(this, "キャンセル", Toast.LENGTH_SHORT).show();
    }
}