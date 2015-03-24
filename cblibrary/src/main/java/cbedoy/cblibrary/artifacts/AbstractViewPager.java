package cbedoy.cblibrary.artifacts;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import java.util.HashMap;
import java.util.List;

import cbedoy.cblibrary.widgets.AbstractView;

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
public abstract class AbstractViewPager extends PagerAdapter
{
    private List<Object> mDataModel;
    private List<AbstractView> mViewModel;

    public AbstractViewPager(List<Object> dataModel, List<AbstractView> viewModel){
        mDataModel = dataModel;
        mViewModel = viewModel;
    }

    @Override
    public int getCount() {
        return mDataModel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((AbstractView)o).getView();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AbstractView abstractView = mViewModel.get(position);
        HashMap<String, Object> currentModel = (HashMap<String, Object>) mDataModel.get(position);
        abstractView.setDataModel(currentModel);
        abstractView.getView();
        abstractView.reload();
        ((ViewPager)container).addView(abstractView.getView());
        return abstractView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(((AbstractView)object).getView());
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
}
