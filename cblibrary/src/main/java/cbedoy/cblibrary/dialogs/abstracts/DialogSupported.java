package cbedoy.cblibrary.dialogs.abstracts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by Carlos Bedoy on 2/17/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public class DialogSupported extends Dialog
{
    private IDialogSupportedCallback callback;
    private boolean wasDismiss;

    public void setCallback(IDialogSupportedCallback callback) {
        this.callback = callback;
    }

    public DialogSupported(Context context) {
        super(context);
        setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    hide();
                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public void dismiss()
    {
        if(wasDismiss)
            callback.onDismiss();
        wasDismiss = false;

        super.dismiss();
    }

    @Override
    public void show()
    {
        callback.onShow();

        wasDismiss = true;

        super.show();
    }

    @Override
    public void hide()
    {
        if(wasDismiss)
            callback.onDismiss();
        wasDismiss = false;

        super.hide();
    }

    public  interface  IDialogSupportedCallback
    {
        public void onDismiss();
        public void onShow();
    }


}
