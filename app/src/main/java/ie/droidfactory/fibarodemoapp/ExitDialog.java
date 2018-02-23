package ie.droidfactory.fibarodemoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

/**
 * Created by kudlaty on 2018-02-23.
 */

public class ExitDialog extends AlertDialog.Builder {

    private Activity activity;

    public ExitDialog(Activity activity) {
        super(activity, R.style.CustomHoloDialog);
        this.activity=activity;
    }

    public android.support.v7.app.AlertDialog showExit() {
        android.support.v7.app.AlertDialog.Builder bld;
        bld = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.CustomHoloDialog));

        bld.setTitle("Exit App");
        bld.setMessage("Are you sure you want to exit?");

        bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
        });
        bld.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { activity.finishAffinity(); }
        });
        bld.setCancelable(true);
        return bld.create();
    }


}
