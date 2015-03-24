package cbedoy.cblibrary.dialogs.artifacts;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

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
public class TutorialAdapter extends PagerAdapter
{

    private List<View> viewModel;

    public TutorialAdapter(List<View> viewModel)
    {
        this.viewModel = viewModel;

    }

    @Override
    public int getCount() {
        return viewModel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((ImageView) o);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView tutorialView = (ImageView) viewModel.get(position);
        ((ViewPager)container).addView(tutorialView);
        return tutorialView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(((View)object));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
