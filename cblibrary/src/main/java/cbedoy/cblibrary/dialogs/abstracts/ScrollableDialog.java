package cbedoy.cblibrary.dialogs.abstracts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;



import java.util.ArrayList;

import cbedoy.cblibrary.R;
import cbedoy.cblibrary.dialogs.artifacts.TutorialAdapter;
import cbedoy.cblibrary.services.ApplicationLoader;
import cbedoy.cblibrary.widgets.CircleView;
import cbedoy.cblibrary.widgets.ScaleImageView;


/**
 * Created by Carlos Bedoy  on 11/3/14.
 */
public abstract class ScrollableDialog extends AbstractDialog implements ViewPager.OnPageChangeListener
{
    private ViewPager mViewPagger;
    private ImageButton mActionClose;
    private String mEnvironmentPath;
    protected View mTitleContainer;
    protected View mDialogBackground;
    protected ArrayList<View> mViewModel;
    protected ArrayList<CircleView> mCircleModel;
    protected TutorialAdapter mTutorialAdapter;
    protected TextView mTitleView;
    protected LinearLayout mLayoutCircles;

    public ScrollableDialog(Activity activity)
    {
        super(activity);
    }

    @Override
    public View onCreateView() {
        mView = (ViewGroup) mActivity.getLayoutInflater().inflate(R.layout.dialog_flipper, null);
        mViewPagger = (ViewPager) mView.findViewById(R.id.dialog_flipper_view_pager);
        mDialogBackground = (View) mView.findViewById(R.id.overlay);
        mTitleView = (TextView) mView.findViewById(R.id.dialog_flipper_title);
        mActionClose = (ImageButton) mView.findViewById(R.id.dialog_flipper_action_close);
        mTitleContainer = mView.findViewById(R.id.dialog_flipper_title_container);
        mViewModel = new ArrayList<>();
        mCircleModel = new ArrayList<>();
        mTutorialAdapter = new TutorialAdapter(mViewModel);
        mLayoutCircles = (LinearLayout) mView.findViewById(R.id.dialog_flipper_circles);
        mViewPagger.setAdapter(mTutorialAdapter);
        mViewPagger.setOnPageChangeListener(this);
        mActionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmiss();
            }
        });
        mView.findViewById(R.id.overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmiss();
            }
        });
        mEnvironmentPath = "assets://images/";
        return mView;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index=0; index< mTutorialAdapter.getCount(); index++)
        {
            CircleView circleView = mCircleModel.get(index);
            circleView.setStatus(position == index);
            circleView.getView();
            circleView.reload();
        }
    }

    @Override
    public void onPageScrollStateChanged(int position)
    {

    }

    public void reload()
    {
        if(mLayoutCircles.getChildCount()>0)
            mLayoutCircles.removeAllViews();
        for(CircleView circleView : mCircleModel) {
            circleView.getView();
            circleView.reload();
            mLayoutCircles.addView(circleView.getView());
        }
        mTutorialAdapter.notifyDataSetChanged();
        mViewPagger.setCurrentItem(0);
    }

    protected void createImageWithResourceAndStatus(Object resource, boolean status){
        ScaleImageView imageView = new ScaleImageView(mActivity.getApplicationContext());
        String path = mEnvironmentPath + resource;
        if(resource instanceof String)
            ImageLoader.getInstance().displayImage(path, imageView, ApplicationLoader.options);
        else
            imageView.setImageResource((Integer) resource);
        CircleView circleView = new CircleView(mActivity.getApplicationContext());
        circleView.setStatus(status);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mViewModel.add(imageView);
        mCircleModel.add(circleView);
    }

    @Override
    public void onDismiss()
    {
        if(mViewModel.size() > 0)
        {
            for(View view : mViewModel)
            {
                if(view instanceof ImageView)
                {
                    Drawable drawable = ((ImageView) view).getDrawable();
                    if(drawable instanceof BitmapDrawable){
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        if(bitmap != null){
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                }
            }
        }
        mViewModel.clear();
        if(mLayoutCircles.getChildCount()>0)
            mLayoutCircles.removeAllViews();
        mTutorialAdapter.notifyDataSetChanged();
        mViewPagger = null;
        mActionClose = null;
        mTitleContainer = null;
        mDialogBackground = null;
        mViewModel = null;
        mCircleModel = null;
        mTutorialAdapter = null;
        mTitleView = null;
        mLayoutCircles = null;

        super.onDismiss();
    }
}
