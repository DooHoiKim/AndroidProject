package com.dh.pro1822.pwassistance;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class ExitDialogFragment extends DialogFragment {

    private DataBaseFileManager backUp;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        backUp = new DataBaseFileManager();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("App Close");
        builder.setMessage("종료하시겠습니까?");
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message = backUp.exportDB();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                getActivity().findViewById(R.id.list_header).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.search_list).setVisibility(View.INVISIBLE);
                ActivityCompat.finishAffinity(getActivity());
                dialog.dismiss();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        finish();
                    String message = backUp.exportDB();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    getActivity().findViewById(R.id.list_header).setVisibility(View.INVISIBLE);
                    getActivity().findViewById(R.id.search_list).setVisibility(View.INVISIBLE);
                    ActivityCompat.finishAffinity(getActivity());
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        builder.setNegativeButton("취소", null);
//        builder.show();
        return builder.create();
    }
}
