package com.example.azaqo.sahabatbidan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by azaqo on 12/05/2016.
 */
public class PairingDialog extends DialogFragment {
    public interface NoticeDialogFragment{
        void onSimpanPressed(DialogFragment dialog,String uname,String passwd);
    }

    NoticeDialogFragment mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pairing_dialog,null);
        final EditText uname = (EditText) view.findViewById(R.id.unameibu);
        final EditText passibu = (EditText) view.findViewById(R.id.passibu);
        builder.setView(view)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //tambahkeun
                        mListener.onSimpanPressed(PairingDialog.this,uname.getText().toString(),passibu.getText().toString());
                    }
                })
                .setTitle("Mintalah pasien untuk memasukkan data");

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogFragment) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "harus di implement bang");
        }
    }
}
