package home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.liang.dayishopping.R;

import java.util.List;

import home.bean.ResultBeanData;
import utils.Constants;

public class ChannelAdapter extends BaseAdapter {
    private  List<ResultBeanData.ResultBean.ChannelInfoBean> datas;
    private  Context mContext;

    public ChannelAdapter(Context mContext, List<ResultBeanData.ResultBean.ChannelInfoBean> channel_info) {
        this.mContext=mContext;
        this.datas=channel_info;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ResultBeanData.ResultBean.ChannelInfoBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(mContext, R.layout.item_channel,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_channel);
            viewHolder.tv_title=(TextView) convertView.findViewById(R.id.tv_channel);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //根据位置得到bean对象
        ResultBeanData.ResultBean.ChannelInfoBean channelInfoBean = datas.get(position);
        System.out.println("翻车"+channelInfoBean.toString());
        Glide.with(mContext).load(Constants.IMAGE_URL+channelInfoBean.getImage()).into(viewHolder.iv_icon);
        viewHolder.tv_title.setText(channelInfoBean.getChannel_name());
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }
}
