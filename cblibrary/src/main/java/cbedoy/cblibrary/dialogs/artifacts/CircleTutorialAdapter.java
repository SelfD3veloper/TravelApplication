package cbedoy.cblibrary.dialogs.artifacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cbedoy.cblibrary.R;

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
public class CircleTutorialAdapter extends BaseAdapter
{
    private List<Boolean> mViewModel;
    private Context mContext;

    public CircleTutorialAdapter(Context context, List<Boolean> viewModel){
        this.mContext = context;
        this.mViewModel = viewModel;
    }

    @Override
    public int getCount() {
        return mViewModel.size();
    }

    @Override
    public Object getItem(int i) {
        return mViewModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tutorial_cell_circle, null);
            viewHolder = new ViewHolder();
            viewHolder.view = convertView.findViewById(R.id.circle_view);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        boolean isCurrentVisible = mViewModel.get(position);
        if(isCurrentVisible)
        {
            viewHolder.view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_tutorial_visible));
        }
        else
        {
            viewHolder.view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.circle_tutorial_no_visible));
        }
        return convertView;
    }

    static class ViewHolder{
        View view;
    }
}
