package home.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.liang.dayishopping.R;

import java.io.IOException;

import base.BaseFragment;
import home.adapter.HomeFragmentAdapter;
import home.bean.ResultBeanData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.Constants;

public class HomeFragment extends BaseFragment {
    private TextView tv_search_home;
    private TextView tv_message_home;
    private ImageView ib_top;
    private RecyclerView rv_home;
    private ResultBeanData.ResultBean resultBean;
    private HomeFragmentAdapter adpter;

    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_home, null);
         tv_search_home = (TextView) view.findViewById(R.id.tv_search_home);
        tv_message_home = (TextView) view.findViewById(R.id.tv_message_home);
        ib_top = (ImageView) view.findViewById(R.id.ib_top);
        rv_home = (RecyclerView) view.findViewById(R.id.rv_home);

        //设置点击事件
        initListener();
        return view;
    }

    private void initListener() {
        ib_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_home.scrollToPosition(0);
            }
        });

        tv_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "搜索！", Toast.LENGTH_SHORT).show();
            }
        });

        tv_message_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "进入消息中心!  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        String url= Constants.HOME_URL;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(result);
                    }
                });
            }
        });
    }

    private void processData(String result) {
        ResultBeanData resultBeanData= JSON.parseObject(result,ResultBeanData.class);
        resultBean = resultBeanData.getResult();
        if (resultBean!=null){
            //有数据
            //设置适配器
             adpter = new HomeFragmentAdapter(mContext,resultBean);

             rv_home.setAdapter(adpter);
             rv_home.setLayoutManager(new GridLayoutManager(mContext,1));
        }else {
            //没有数据
        }
    }
}
