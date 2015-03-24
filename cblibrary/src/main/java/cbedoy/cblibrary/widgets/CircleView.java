package cbedoy.cblibrary.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cbedoy.cblibrary.R;
import cbedoy.cblibrary.services.ImageService;


/**
 * Created by Carlos Bedoy on 28/12/2014.
 *
 * Mobile App Developer
 * CBLibrary
 *
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class CircleView
{
    private View view;
    private View circleView;
    private Context context;
    private boolean status;

    public CircleView(Context context){
        this.context = context;
    }

    public View getView(){
        if(view == null)
            view = init();
        return view;
    }

    public View init(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.tutorial_cell_circle, null);
        circleView = view.findViewById(R.id.circle_view);
        int pXsFromDPs = (int) ImageService.getPXsFromDPs(12);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pXsFromDPs, pXsFromDPs);
        layoutParams.leftMargin = pXsFromDPs / 2;
        layoutParams.rightMargin = pXsFromDPs / 2;
        circleView.setLayoutParams(layoutParams);
        return view;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void reload(){
        circleView.setBackgroundResource(status ? R.drawable.circle_tutorial_visible : R.drawable.circle_tutorial_no_visible);
    }
}
