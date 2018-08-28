package shoppingcart.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.example.liang.dayishopping.app.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import home.bean.GoodsBean;
import utils.CacheUtils;

public class CartStorage {
    public static final String JSON_CART = "json_cart";
    private static CartStorage cartStorage;
    private final Context mContext;
    private SparseArray<GoodsBean>sparseArray;

    private CartStorage(Context context){
        this.mContext=context;
        //把之前存储的数据读取出来
        sparseArray=new SparseArray<>(100);
        listToSparseArray();
    }

    //从本地读取的数据加入到SparseArray中
    private void listToSparseArray() {
        List<GoodsBean>goodsBeanList=getAllData();
        //把list数据转换成SparseArray
        for (int i = 0; i <goodsBeanList.size() ; i++) {
            GoodsBean goodsBean=goodsBeanList.get(i);
            sparseArray.put(Integer.parseInt(goodsBean.getProduct_id()),goodsBean);
        }
    }

    //获取本地所有的数据
    public List<GoodsBean> getAllData() {
        List<GoodsBean>goodsBeanList=new ArrayList<>();
        //1.从本地获取
        String json= CacheUtils.getString(mContext,JSON_CART);
        //2.使用gson转换成列表
        if (!TextUtils.isEmpty(json)){
            goodsBeanList=new Gson().fromJson(json,new TypeToken<List<GoodsBean>>(){}.getType());
        }
        return goodsBeanList;
    }

    public static CartStorage getInstance(){
        if (cartStorage==null){
            cartStorage=new CartStorage(MyApplication.getmContext());
        }
        return cartStorage;
    }

    //添加数据
    public void addData(GoodsBean goodsBean){
        //1.添加到内存中sparseArray,如果当前数据已经存在，就修改number递增
        GoodsBean tempData = sparseArray.get(Integer.parseInt(goodsBean.getProduct_id()));
        if (tempData!=null){
            //内存中有了这条
            tempData.setNumber(tempData.getNumber()+1);
        }else {
            tempData=goodsBean;
            tempData.setNumber(1);
        }
        //同步到内存中
        sparseArray.put(Integer.parseInt(tempData.getProduct_id()),tempData);

        //2.同步到本地
        saveLocal();

    }

    //保存数据到本地
    private void saveLocal() {
        //sparseArray转成list
        List<GoodsBean>goodsBeanList=sparseToList();
        //使用gson把列表转换成String类型
        String json=new Gson().toJson(goodsBeanList);
        //把string保存
        CacheUtils.saveString(mContext,JSON_CART,json);
    }

    private List<GoodsBean> sparseToList() {
        List<GoodsBean>goodsBeanList=new ArrayList<>();
        for (int i = 0; i <sparseArray.size() ; i++) {
                GoodsBean goodsBean=sparseArray.valueAt(i);
                goodsBeanList.add(goodsBean);
        }
        return goodsBeanList;
    }


    //删除数据
    public void deleteData(GoodsBean goodsBean){
        sparseArray.delete(Integer.parseInt(goodsBean.getProduct_id()));
        //把内存保存到本地
        saveLocal();
    }

    public void updataData(GoodsBean goodsBean){
        //从内存中更新，同步到本地
        sparseArray.put(Integer.parseInt(goodsBean.getProduct_id()),goodsBean);
        //把内存保存到本地
        saveLocal();
    }
}
