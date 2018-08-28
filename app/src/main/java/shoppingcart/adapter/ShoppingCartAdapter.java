package shoppingcart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.liang.dayishopping.R;

import java.util.List;

import home.bean.GoodsBean;
import shoppingcart.utils.CartStorage;
import shoppingcart.view.AddSubView;
import utils.Constants;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private  CheckBox cbAll;
    private  CheckBox checkboxAll;
    private  TextView tvShopcartTotal;
    private  Context mContext;
    private  List<GoodsBean> datas;

    public ShoppingCartAdapter(Context mContext, List<GoodsBean> goodsBeanList, TextView tvShopcartTotal, CheckBox checkboxAll, CheckBox cbAll) {
        this.mContext=mContext;
        this.datas=goodsBeanList;
        this.checkboxAll=checkboxAll;
        this.tvShopcartTotal=tvShopcartTotal;
        //完成状态下的删除
        this.cbAll=cbAll;
        showTotalPrice();
        //设置点击事件
        setListener();
        checkAll();
    }

    private void setListener() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                //根据位置找到对应的bean对象
                GoodsBean goodsBean = datas.get(position);
                //2.设置取反
                goodsBean.setSelected(!goodsBean.isSelected());
                //3.刷新状态
                notifyItemChanged(position);
                //校验是否全选
                checkAll();
                showTotalPrice();
            }
        });

        //CheckBox点击事件
        checkboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.得到状态
                boolean checked = checkboxAll.isChecked();
                //2.根据状态设置全选和非全选
                checkALL_none(checked);
                //3.计算总价格
                showTotalPrice();
            }
        });

        cbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.得到状态
                boolean checked = cbAll.isChecked();
                //2.根据状态设置全选和非全选
                checkALL_none(checked);
            }
        });
    }

    //设置全选和非全选
    public void checkALL_none(boolean checked) {
        if (datas!=null&&datas.size()>0){
            for (int i = 0; i <datas.size() ; i++) {
                GoodsBean goodsBean = datas.get(i);
                goodsBean.setSelected(checked);
                notifyItemChanged(i);
            }
        }
    }

    public void checkAll() {
        if (datas!=null&&datas.size()>0){
            int number=0;
            for (int i = 0; i <datas.size() ; i++) {
                GoodsBean goodsBean = datas.get(i);
                if (!goodsBean.isSelected()){
                    //非全选
                    checkboxAll.setChecked(false);
                    cbAll.setChecked(false);
                }else {
                    number++;
                }
            }
            if (number==datas.size()){
                //全选
                checkboxAll.setChecked(true);
                cbAll.setChecked(true);
            }
        }
    }

    public void showTotalPrice() {
        tvShopcartTotal.setText("合计："+getTotalPrice());
    }

    private double getTotalPrice() {
        double totalPrice=0.0;
        if (datas!=null&&datas.size()>0){
            for (int i = 0; i <datas.size() ; i++) {
                GoodsBean goodsBean = datas.get(i);
                if (goodsBean.isSelected()){
                    totalPrice=totalPrice+Double.valueOf(goodsBean.getNumber())*Double.valueOf(goodsBean.getCover_price());
                }
            }
        }
        return totalPrice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=View.inflate(mContext, R.layout.item_shop_cart,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final GoodsBean goodsBean = datas.get(position);
        holder.cb_gov.setChecked(goodsBean.isSelected());
        Glide.with(mContext).load(Constants.IMAGE_URL+goodsBean.getFigure()).into(holder.iv_gov);
        holder.tv_desc_gov.setText(goodsBean.getName());
        holder.tv_price_gov.setText("$"+goodsBean.getCover_price());
        holder.addSubView.setValue(goodsBean.getNumber());
        holder.addSubView.setMinValue(1);
        holder.addSubView.setMaxValue(5);
        //设置商品数量的变化
        holder.addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                //1.内存中和本地要更新
                goodsBean.setNumber(value);
                CartStorage.getInstance().updataData(goodsBean);
                notifyItemChanged(position);
                showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void deleteData() {
        if (datas!=null&&datas.size()>0){
            for (int i = 0; i <datas.size() ; i++) {
                //删除选中的
                GoodsBean goodsBean = datas.get(i);
                if (goodsBean.isSelected()){
                    //内存中移除,刷新
                    datas.remove(goodsBean);
                    CartStorage.getInstance().deleteData(goodsBean);
                    notifyItemRemoved(i);
                    i--;
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private  ImageView iv_gov;
        private  TextView tv_desc_gov;
        private  TextView tv_price_gov;
        private  CheckBox cb_gov;
        private AddSubView addSubView;
        public ViewHolder(View itemView) {
            super(itemView);
             cb_gov = (CheckBox)itemView.findViewById(R.id.cb_gov);
             iv_gov = (ImageView)itemView.findViewById(R.id.iv_gov);
             tv_desc_gov = (TextView)itemView.findViewById(R.id.tv_desc_gov);
             tv_price_gov = (TextView)itemView.findViewById(R.id.tv_price_gov);
            addSubView = (AddSubView) itemView.findViewById(R.id.addSubView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.OnItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        public void OnItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
