package com.example.my.labelmanagement.adapter;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my.labelmanagement.FileFragment;
import com.example.my.labelmanagement.MainActivity;
import com.example.my.labelmanagement.R;
import com.example.my.labelmanagement.ReadFragment;
import com.example.my.labelmanagement.WriteFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * 横向滚动菜单适配器
 * create by 张智超 on 2019/2/26
 */
public class MenuAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private int[] versions = {R.string.menu_tab_read, R.string.menu_tab_write, R.string.menu_tab_file};
    private MainActivity mContext;

    public MenuAdapter(FragmentManager fragmentManager, MainActivity mContext) {
        super(fragmentManager);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return versions.length;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container){
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
            return new ReadFragment().setArguments(position + "");
        } else if (position == 1) {
            return new WriteFragment().setArguments(position + "");
        } else {
            return new FileFragment().setArguments(position + "");
        }
    }

    @Override
    public int getItemPosition(Object object) {
        //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
        // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
        return PagerAdapter.POSITION_UNCHANGED;
    }

    private int getTextWidth(TextView textView){
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
