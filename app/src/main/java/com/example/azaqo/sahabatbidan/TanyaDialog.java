package com.example.azaqo.sahabatbidan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class TanyaDialog extends DialogFragment {

    public interface ApaYangTerjadi{
       public void hapuspressed(String uname);
    }

    ApaYangTerjadi mListener;

    public static TanyaDialog newInstance(String uname) {

        Bundle args = new Bundle();
        args.putString("uname",uname);
        TanyaDialog fragment = new TanyaDialog();
        fragment.setArguments(args);
        return fragment;
    }

    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
        username = getArguments().getString("uname");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Apakah anda ingin menghapusnya?")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.hapuspressed(username);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (ApaYangTerjadi) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
