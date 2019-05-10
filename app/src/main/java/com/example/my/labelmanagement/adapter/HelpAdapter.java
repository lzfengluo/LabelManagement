package com.example.my.labelmanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.my.labelmanagement.R;

import java.util.List;


/**
 *
 * @author 张智超_
 * @date 2019/01/21
 * Email 981547125@qq.com
 */
public class HelpAdapter extends ArrayAdapter<HelpListUtil> {

    //子项布局的ID
    private int resourceId;
    private int mCurrentItem = 0;
    private boolean isClick = false;

    public HelpAdapter(Context context, int textViewResourceId, List<HelpListUtil> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 获取当前项的HelpListUtil实例
        HelpListUtil helpListUtil = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null){
            // inflate出子项布局，实例化其中的图片控件和文本控件
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            // 通过id得到图片控件实例
//            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            // 通过id得到问题文本空间实例
            viewHolder.helpTitle = (TextView) view.findViewById(R.id.help_title);
            // 缓存图片控件和文本控件的实例
            view.setTag(viewHolder);
        }else{
            view = convertView;
            // 取出缓存
            viewHolder = (ViewHolder) view.getTag();
        }

        // 直接使用缓存中的图片控件和文本控件的实例
        // 图片控件设置图片资源
//        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        // 文本控件设置文本内容
        viewHolder.helpTitle.setText(helpListUtil.getHelpTitle());

        if(mCurrentItem == position) {
            viewHolder.helpTitle.setBackgroundColor(Color.parseColor("#f9fcff"));
        }else {
            viewHolder.helpTitle.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return view;
    }

    //内部类
    class ViewHolder{
        TextView helpTitle;
    }

    public void setmCurrentItem(int mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
