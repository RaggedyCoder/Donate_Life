package com.project.bluepandora.donatelife.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;


public class DialogBuilder {

    private String TAG;
    private Activity activity;
    private Dialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    private ProgressDialog.Builder progressDialogBuilder;
    private Dialog progressDialog;

    public DialogBuilder(Activity activity, String TAG) {
        this.activity = activity;
        progressDialogBuilder = new ProgressDialog.Builder(getActivity());
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        this.TAG = TAG;
    }

    public void createAlertDialog(String message) {
        createAlertDialog(null, message);
    }

    public void createAlertDialog(String title, String message) {
        createAlertDialog(title, message, null);
    }

    public void createAlertDialog(String title, String message,
                                  DialogInterface.OnClickListener neutralClickListener) {
        createAlertDialog(title, message, null, null, neutralClickListener, null, null, "ok");
    }

    public void createAlertDialog(String title, String message,
                                  DialogInterface.OnClickListener yesClickListener,
                                  DialogInterface.OnClickListener noClickListener) {
        createAlertDialog(title, message, yesClickListener, noClickListener, null, "yes", "no", null);
    }

    public void createAlertDialog(String title, String message,
                                  DialogInterface.OnClickListener yesClickListener,
                                  DialogInterface.OnClickListener noClickListener,
                                  DialogInterface.OnClickListener neutralClickListener,
                                  String yesTitle, String noTitle, String neutralTitle) {
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton(neutralTitle, neutralClickListener);
        alertDialogBuilder.setPositiveButton(yesTitle, yesClickListener);
        alertDialogBuilder.setNegativeButton(noTitle, noClickListener);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.e(TAG, "Alert Dialog Created."
                + "Title:" + title
                + ". Message:" + message
                + ". negativeButtonClickListener:" + noTitle + (noClickListener != null ? " not null." : " null.")
                + ". positiveButtonClickListener:" + yesTitle + (yesClickListener != null ? " not null." : " null.")
                + ". neutralButtonClickListener:" + neutralTitle + (neutralClickListener != null ? " not null." : " null."));
    }

    public void createProgressDialog(String message) {
        createProgressDialog(null, message);
    }

    public void createProgressDialog(String title, String message) {
        progressDialogBuilder.setTitle(title);
        progressDialogBuilder.setMessage(message);
        progressDialogBuilder.setCancelable(false);
        progressDialog = progressDialogBuilder.create();
        progressDialogBuilder.show();
        Log.e(TAG, "Progress Dialog Created. Title:" + title + ". Message:" + message);
    }

    public Dialog getAlertDialog() {
        return alertDialog;
    }

    public Dialog getProgressDialog() {
        return progressDialog;
    }

    public Activity getActivity() {
        return activity;
    }
}
