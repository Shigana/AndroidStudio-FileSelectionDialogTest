package com.shiganablog.fileselectiondialogtest;

import java.io.File;
import java.util.Locale;

/**
 * ファイル情報を扱うクラスです。パッケージ内クラス。
 */
class FileInformation implements Comparable<FileInformation> {
    /**
     * ファイル。
     */
    private File file;

    /**
     * コンストラクタ。
     * @param file ファイル
     */
    public FileInformation(File file) {
        this.file = file;
    }

    @Override
    public int compareTo(FileInformation opponent) {
        if(this.file.isDirectory() && ! opponent.file.isDirectory()) {
            return -1;
        } else if(! this.file.isDirectory() && opponent.file.isDirectory()) {
            return 1;
        } else {
            return this.file.getName().toLowerCase(Locale.US).compareTo(opponent.file.getName().toLowerCase(Locale.US));
        }
    }

    /**
     * ファイルを返します。
     * @return ファイル
     */
    public File getFile() {
        return this.file;
    }
}