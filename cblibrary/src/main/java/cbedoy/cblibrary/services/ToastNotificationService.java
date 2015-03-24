package cbedoy.cblibrary.services;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cbedoy.cblibrary.interfaces.IToastRepresentationHandler;


/**
 * Created by Carlos Bedoy on 1/5/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public class ToastNotificationService implements IToastRepresentationHandler
{

    private TextView textView;
    private LinearLayout linearLayout;
    private Toast toast;

    public ToastNotificationService()
    {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ImageService.getScreenWidth(), (int) ImageService.getPXsFromDPs(48));
        textView = new TextView(ApplicationLoader.mainContext);
        FontSupportedService.supportTextViewWithFontSize(textView, 16);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ApplicationLoader.regularFont);

        linearLayout = new LinearLayout(ApplicationLoader.mainContext);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.parseColor("#dd000000"));
        linearLayout.addView(textView);
        linearLayout.setContentDescription("mToastNotification");

        toast = new Toast(ApplicationLoader.mainContext);
        toast.setView(linearLayout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
    }


    @Override
    public void showToastWithMessage(String message) {
        textView.setText(message);
        toast.show();
    }

    @Override
    public void showToastWithMessageAndCallback(String message, final IToastRepresentationCallback callback) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.run();
            }
        });
        textView.setText(message);
        toast.show();
    }
}
