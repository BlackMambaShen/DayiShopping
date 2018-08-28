package home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.liang.dayishopping.R;
import com.example.liang.dayishopping.app.GoodsInfoActivity;

import java.util.List;

import home.bean.GoodsBean;
import home.bean.ResultBeanData;
import utils.Constants;

public class SeckillRecyclerViewAdapter extends RecyclerView.Adapter<SeckillRecyclerViewAdapter.ViewHolder> {

    private final List<ResultBeanData.ResultBean.SeckillInfoBean.ListBean> list;
    private final Context mContext;
    private ResultBeanData.ResultBean.SeckillInfoBean.ListBean listBean;
    private static final String GOODS_BEAN = "goodsBean";

    public SeckillRecyclerViewAdapter(Context mContext, List<ResultBeanData.ResultBean.SeckillInfoBean.ListBean> list) {
        this.list=list;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=View.inflate(mContext, R.layout.item_seckill,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //根据位置得到对应的数据
         listBean=list.get(position);
        Glide.with(mContext).load(Constants.IMAGE_URL+listBean.getFigure()).into(holder.iv_figure);
        holder.tv_cover_price.setText("$"+listBean.getCover_price());
        holder.tv_origin_price.setText("$"+listBean.getOrigin_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private  TextView tv_cover_price;
        private  TextView tv_origin_price;
        private ImageView iv_figure;
        public ViewHolder(View itemView) {
            super(itemView);
             iv_figure = (ImageView) itemView.findViewById(R.id.iv_figure);
            tv_cover_price = (TextView) itemView.findViewById(R.id.tv_cover_price);
            tv_origin_price = (TextView) itemView.findViewById(R.id.tv_origin_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "秒杀="+getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    GoodsBean goodsBean=new GoodsBean();
                    goodsBean.setCover_price(listBean.getCover_price());
                    goodsBean.setFigure(listBean.getFigure());
                    goodsBean.setName(listBean.getName());
                    goodsBean.setProduct_id(listBean.getProduct_id());
                    Intent intent=new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODS_BEAN,goodsBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
