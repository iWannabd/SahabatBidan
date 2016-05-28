package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Created by azaqo on 27/05/2016.
 */
public class HapusKehamilanDialog extends DialogFragment {
    public interface SetelahHapus {
        public void HapusPressed(String selected);
    }

    public static HapusKehamilanDialog newInstance(String selected) {

        Bundle args = new Bundle();
        args.putString("selected",selected);
        HapusKehamilanDialog fragment = new HapusKehamilanDialog();
        fragment.setArguments(args);
        return fragment;
    }

    SetelahHapus mListener;
    String selected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Apakah anda ingin menghapusnya?")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.HapusPressed(selected);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (SetelahHapus) getTargetFragment();
        } catch (ClassCastException a){
            throw new ClassCastException("Harus implemen interfase yang ada di kelas ini");
        }
        if (getArguments()!=null){
            selected = getArguments().getString("selected");
        }
    }
}
