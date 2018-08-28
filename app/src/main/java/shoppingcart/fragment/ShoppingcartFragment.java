package shoppingcart.fragment;

import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liang.dayishopping.R;

import java.util.List;

import base.BaseFragment;
import home.bean.GoodsBean;
import shoppingcart.adapter.ShoppingCartAdapter;
import shoppingcart.utils.CartStorage;

public class ShoppingcartFragment extends BaseFragment implements View.OnClickListener
{
    private TextView tvShopcartEdit;
    private RecyclerView recyclerview;
    private LinearLayout llCheckAll;
    private CheckBox checkboxAll;
    private TextView tvShopcartTotal;
    private Button btnCheckOut;
    private LinearLayout llDelete;
    private CheckBox cbAll;
    private Button btnDelete;
    private Button btnCollection;
    private ImageView ivEmpty;
    private TextView tvEmptyCartTobuy;
    private LinearLayout ll_empty_shopcart;
    private ShoppingCartAdapter adapter;

    //编辑状态
    private static final int ACTION_EDIT=1;
    //完成状态
    private static final int ACTION_COMPLETE=2;
    private MyReceiver myReceiver;

    public void onClick(View v) {
        if ( v == btnCheckOut ) {
            // Handle clicks for btnCheckOut
        } else if ( v == btnDelete ) {
            // Handle clicks for btnDelete
            //删除选中的，校验状态
            adapter.deleteData();
            adapter.checkAll();
            //数据大小为0
            if (adapter.getItemCount()==0){
                emptyShoppingCart();
            }
            adapter.showTotalPrice();
        } else if ( v == btnCollection ) {
            // Handle clicks for btnCollection
        }
    }

    public View initView() {
        View view=View.inflate(mContext, R.layout.fragment_shopping_cart,null);
        tvShopcartEdit = (TextView)view.findViewById( R.id.tv_shopcart_edit );
        recyclerview = (RecyclerView)view.findViewById( R.id.recyclerview );
        llCheckAll = (LinearLayout)view.findViewById( R.id.ll_check_all );
        checkboxAll = (CheckBox)view.findViewById( R.id.checkbox_all );
        tvShopcartTotal = (TextView)view.findViewById( R.id.tv_shopcart_total );
        btnCheckOut = (Button)view.findViewById( R.id.btn_check_out );
        llDelete = (LinearLayout)view.findViewById( R.id.ll_delete );
        cbAll = (CheckBox)view.findViewById( R.id.cb_all );
        btnDelete = (Button)view.findViewById( R.id.btn_delete );
        btnCollection = (Button)view.findViewById( R.id.btn_collection );
        ivEmpty = (ImageView)view.findViewById( R.id.iv_empty );
        tvEmptyCartTobuy = (TextView)view.findViewById( R.id.tv_empty_cart_tobuy );

        ll_empty_shopcart = (LinearLayout)view.findViewById( R.id.ll_empty_shopcart );
        btnCheckOut.setOnClickListener( this );
        btnDelete.setOnClickListener( this );
        btnCollection.setOnClickListener( this );
        initListener();
        return view;
    }

    private void initListener() {
        //设置默认的编辑状态
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action= (int) v.getTag();
                if (action==ACTION_EDIT){
                    //切换为完成状态
                    showDelete();
                }else {
                    //切换成编辑状态
                    hideDelete();
                }
            }
        });
    }

    private void hideDelete() {
        //设置状态和文本
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        //变为非勾选
        if (adapter!=null){
            adapter.checkALL_none(true);
            adapter.checkAll();
        }
        //删除视图显示
        llDelete.setVisibility(View.GONE);
        //结算视图隐藏
        llCheckAll.setVisibility(View.VISIBLE);
    }

    private void showDelete() {
        //设置状态和文本
        tvShopcartEdit.setTag(ACTION_COMPLETE);
        tvShopcartEdit.setText("完成");
        //变为非勾选
        if (adapter!=null){
            adapter.checkALL_none(false);
            adapter.checkAll();
        }
        //删除视图显示
        llDelete.setVisibility(View.VISIBLE);
        //结算视图隐藏
        llCheckAll.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        showData();
         myReceiver = new MyReceiver(adapter);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.liang");
        mContext.registerReceiver(myReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    //显示数据
    private void showData() {
        List<GoodsBean> goodsBeanList = CartStorage.getInstance().getAllData();
        if (goodsBeanList!=null&&goodsBeanList.size()>0){
            ll_empty_shopcart.setVisibility(View.GONE);
            tvShopcartEdit.setVisibility(View.VISIBLE);
            //有数据,把当没有数据显示的布局 隐藏
            //设置适配器
            adapter=new ShoppingCartAdapter(mContext,goodsBeanList,tvShopcartTotal,checkboxAll,cbAll);
            adapter.notifyDataSetChanged();
            recyclerview.setAdapter(adapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        }else {
            //没有数据，显示数据为空的布局
            emptyShoppingCart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private void emptyShoppingCart() {
        ll_empty_shopcart.setVisibility(View.VISIBLE);
        tvShopcartEdit.setVisibility(View.GONE);
        llDelete.setVisibility(View.GONE);
    }
}
