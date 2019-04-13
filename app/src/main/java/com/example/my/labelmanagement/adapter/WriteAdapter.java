package com.example.my.labelmanagement.adapter;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.WriteActivity;
import com.example.my.labelmanagement.WriteSingleDeviceFragment;
import com.example.my.labelmanagement.WriteSingleRackFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * @author 张智超
 * @date 2019/4/13
 */
public class WriteAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private int[] versions = {R.string.write_single_device, R.string.write_single_rack};
    private WriteActivity mContext;

    public WriteAdapter(FragmentManager fragmentManager, WriteActivity mContext) {
        super(fragmentManager);
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return versions.length;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = mContext.getLayoutInflater().inflate(R.layout.menu_tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(mContext.getString(versions[position]));

        int witdh = getTextWidth(textView);
        int padding = 4;
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.1f是根据上面字体大小变化的倍数1.1f设置
        textView.setWidth((int) (witdh * 1.1f) + padding);

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        if (position == 0) {
            return new WriteSingleDeviceFragment().setArguments(position + "");
        } else {
            return new WriteSingleRackFragment().setArguments(position + "");
        }

    }


    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_UNCHANGED;
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }

}

