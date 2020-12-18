package com.shiganablog.fileselectiondialogtest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * ファイルを選択するダイアログフラグメントクラスです。
 */
public class FileSelectDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    /**
     * ルートディレクトリを表します。
     */
    public static final String ROOT_DIRECTORY = "rootDirectory";
    /**
     * ダイアログ表示直後のディレクトリを表します。
     */
    public static final String INITIAL_DIRECTORY = "initialDirectory";
    /**
     * 親ディレクトリのテキストを表します。
     */
    public static final String PREVIOUS = "previous";
    /**
     * キャンセルボタンの文字列を表します。
     */
    public static final String CANCEL = "cancel";
    /**
     * リスナを表します。
     */
    public static final String LISTENER = "listener";
    /**
     * 現在選択中のディレクトリを表します。
     */
    private static final String DIRECTORY = "directory";
    /**
     * アダプター。
     */
    private ArrayAdapter<String> adapter;
    /**
     * ファイル情報。
     */
    private List<FileInformation> fileInformations = new ArrayList<FileInformation>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = this.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        ListView listView = new ListView(activity);
        this.adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1);
        listView.setAdapter(this.adapter);

        this.initializeArguments();

        // ビューを更新する
        this.updateView();

        listView.setOnItemClickListener(this);
        builder.setView(listView);

        Bundle bundle = this.getArguments();
        builder.setTitle(bundle.getString(DIRECTORY) + File.separator);
        builder.setNegativeButton(bundle.getString(CANCEL), new CancelListener());

        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dialog dialog = this.getDialog();
        Bundle bundle = this.getArguments();
        String directory = bundle.getString(DIRECTORY);

        if(position == 0) {
            // 一番上を選択した場合
            if(directory.length() > bundle.getString(ROOT_DIRECTORY).length()) {
                // 通常は戻る処理をする
                directory = directory.substring(0, directory.lastIndexOf(File.separator));
                bundle.putString(DIRECTORY, directory);

                // ダイアログとビューを更新
                dialog.setTitle(directory + File.separator);
                this.updateView();
            } else {
                // トップディレクトリの場合は何もしない
            }
        } else {
            directory = directory + File.separator + this.fileInformations.get(position - 1).getFile().getName();
            File file = new File(directory);
            if(file.isDirectory()) {
                // ディレクトリの場合はその中へ移動
                bundle.putString(DIRECTORY, directory);

                // ダイアログとビューを更新
                dialog.setTitle(directory + File.separator);
                this.updateView();
            } else {
                // ファイルが確定
                OnFileSelectedListener listener = (OnFileSelectedListener)this.getArguments().getSerializable(LISTENER);
                listener.onFileSelected(directory);

                // このダイアログを終了
                this.dismiss();
            }
        }
    }

    /**
     * 要素情報を初期化します。
     */
    private void initializeArguments() {
        Bundle bundle = this.getArguments();
        if(bundle.getString(ROOT_DIRECTORY) == null) {
            bundle.putString(ROOT_DIRECTORY, File.separator);
        }
        if(bundle.getString(INITIAL_DIRECTORY) == null) {
            bundle.putString(INITIAL_DIRECTORY, bundle.getString(ROOT_DIRECTORY));
        }
        if(bundle.getString(DIRECTORY) == null) {
            bundle.putString(DIRECTORY, bundle.getString(INITIAL_DIRECTORY));
        }
        if(bundle.getString(PREVIOUS) == null) {
            bundle.putString(PREVIOUS, "..");
        }
    }
    /**
     * ビューを更新します。
     */
    private void updateView() {
        this.adapter.clear();

        Bundle bundle = this.getArguments();
        this.adapter.add(bundle.getString(PREVIOUS));

        String directory = bundle.getString(DIRECTORY);
        if(directory.equals("")) {
            directory = File.separator;
        }

        this.fileInformations.clear();
        File[] files = new File(directory).listFiles();

        if(files != null) {
            for(File file : files) {
                this.fileInformations.add(new FileInformation(file));
            }

            // ソート
            Collections.sort(this.fileInformations);

            for(FileInformation fileInformation : this.fileInformations) {
                File file = fileInformation.getFile();
                if(file.isDirectory()) {
                    this.adapter.add(file.getName() + File.separator);
                } else {
                    this.adapter.add(file.getName());
                }
            }
        }
    }

    /**
     * キャンセル処理をするリスナです。
     */
    private class CancelListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            OnFileSelectedListener listener = (OnFileSelectedListener)FileSelectDialogFragment.this.getArguments().getSerializable(LISTENER);
            listener.onFileSelectCanceled();
        }
    }
}
