package home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.liang.dayishopping.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnLoadImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;

import home.bean.ResultBeanData;
import utils.Constants;

public class HomeFragmentAdapter extends RecyclerView.Adapter {
    /*
    当前类型
     */
    public static final int BANNER=0;
    public static final int CHANNEL=1;
    public static final int ACT=2;
    public static final int SECKILL=3;
    public static final int RECOMMEND=4;
    public static final int HOT=5;
    private  LayoutInflater mLayoutInflater;
    private  Context mContext;
    private  ResultBeanData.ResultBean resultBean;
    private int currentType=BANNER;

    public HomeFragmentAdapter(Context mContext, ResultBeanData.ResultBean resultBean) {
        this.mContext=mContext;
        this.resultBean=resultBean;
         mLayoutInflater = LayoutInflater.from(mContext);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==BANNER){
            return new BannerViewHolder(mContext,mLayoutInflater.inflate(R.layout.banner_viewpager,null));
        }else if (viewType==CHANNEL){
            return new ChannelViewHolder(mContext,mLayoutInflater.inflate(R.layout.channel_item,null));
        }else if (viewType==ACT){
            return new ActViewHolder(mContext,mLayoutInflater.inflate(R.layout.act_item,null));
        }else if (viewType==SECKILL){
            return new SeckillViewHolder(mContext,mLayoutInflater.inflate(R.layout.seckill_item,null));
        }else if (viewType==RECOMMEND){
            return new RecommendViewHolder(mContext,mLayoutInflater.inflate(R.layout.recommend_item,null));
        }else if (viewType==HOT){
            return new HotViewHolder(mContext,mLayoutInflater.inflate(R.layout.hot_item,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==BANNER){
            BannerViewHolder bannerViewHolder= (BannerViewHolder) holder;
            bannerViewHolder.setData(resultBean.getBanner_info());
        }else if (getItemViewType(position)==CHANNEL){
            ChannelViewHolder channelViewHolder= (ChannelViewHolder) holder;
            channelViewHolder.setData(resultBean.getChannel_info());
        }else if (getItemViewType(position)==ACT){
            ActViewHolder actViewHolder= (ActViewHolder) holder;
            actViewHolder.setData(resultBean.getAct_info());
        }else if (getItemViewType(position)==SECKILL){
            SeckillViewHolder seckillViewHolder= (SeckillViewHolder) holder;
            seckillViewHolder.setData(resultBean.getSeckill_info());
        }else if (getItemViewType(position)==RECOMMEND){
            RecommendViewHolder recommendViewHolder= (RecommendViewHolder) holder;
            recommendViewHolder.setData(resultBean.getRecommend_info());
        }else if (getItemViewType(position)==HOT){
            HotViewHolder hotViewHolder= (HotViewHolder) holder;
            hotViewHolder.setData(resultBean.getHot_info());
        }
    }
    class HotViewHolder extends RecyclerView.ViewHolder{

        private  Context mContext;
        private TextView tv_more_hot;
        private GridView gv_hot;
        private HotGridViewAdapter adapter;
        public HotViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext=mContext;
             tv_more_hot = (TextView)itemView.findViewById(R.id.tv_more_hot);
            gv_hot = (GridView)itemView.findViewById(R.id.gv_hot);
            gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position="+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.HotInfoBean> hot_info) {
            adapter=new HotGridViewAdapter(mContext,hot_info);
            gv_hot.setAdapter(adapter);
        }
    }
    class RecommendViewHolder extends RecyclerView.ViewHolder{

        private  GridView gv_recommend;
        private  Context mContext;
        private TextView tv_more_recommend;
        private RecommendGridViewAdapter adapter;
        public RecommendViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext=mContext;
             tv_more_recommend =(TextView) itemView.findViewById(R.id.tv_more_recommend);
            gv_recommend =(GridView) itemView.findViewById(R.id.gv_recommend);
            gv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position="+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.RecommendInfoBean> recommend_info) {
            //1.有数据了
            //2.设置适配器
            adapter=new RecommendGridViewAdapter(mContext,recommend_info);
            gv_recommend.setAdapter(adapter);
        }
    }


    class SeckillViewHolder extends RecyclerView.ViewHolder{
        private final Context mContext;
        private RecyclerView rv_seckill;
        private TextView tv_more_seckill;
        private TextView tv_time_seckill;
        private SeckillRecyclerViewAdapter adapter;
        private long dt=0;
        @SuppressLint("HandlerLeak")
        private android.os.Handler handler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dt=dt-1000;
                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                String time = format.format(new Date(dt));
                tv_time_seckill.setText(time);
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0,1000);
                if (dt<0){
                    //把消息移除
                    handler.removeCallbacksAndMessages(null);
                }
            }
        };

        public SeckillViewHolder(Context mContext, View itemView) {
            super( itemView);
            this.mContext=mContext;
             tv_time_seckill = (TextView) itemView.findViewById(R.id.tv_time_seckill);
            tv_more_seckill = (TextView) itemView.findViewById(R.id.tv_more_seckill);
            rv_seckill = (RecyclerView) itemView.findViewById(R.id.rv_seckill);
        }

        public void setData(ResultBeanData.ResultBean.SeckillInfoBean seckill_info) {
            //得到数据 设置数据
            adapter=new SeckillRecyclerViewAdapter(mContext,seckill_info.getList());
            rv_seckill.setAdapter(adapter);

            //设置布局管理器
            rv_seckill.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));

            //秒杀倒计时  毫秒值
            dt=Integer.valueOf(seckill_info.getEnd_time())-Integer.valueOf(seckill_info.getStart_time());
            SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
            String time = format.format(new Date(dt));
            tv_time_seckill.setText(time);
            handler.sendEmptyMessageDelayed(0,1000);
        }
    }
    class ActViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private ViewPager act_viewpager;
        public ActViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext=mContext;
             act_viewpager = (ViewPager) itemView.findViewById(R.id.act_viewpager);
        }

        public void setData(final List<ResultBeanData.ResultBean.ActInfoBean> act_info) {
            act_viewpager.setPageMargin(20);
            //有数据 设置适配器
            act_viewpager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return act_info.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view==object;
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    container.removeView((View) object);
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                    ImageView imageView=new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(mContext).load(Constants.IMAGE_URL+act_info.get(position).getIcon_url()).into(imageView);
                    container.addView(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "position"+position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return imageView;
                }
            });
        }
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private GridView gv_channel;
        private ChannelAdapter adapter;
        public ChannelViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext=mContext;
            gv_channel=(GridView) itemView.findViewById(R.id.gv_channel);
            gv_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position"+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.ChannelInfoBean> channel_info) {
            //得到数据 设置gridView的适配器
            adapter=new ChannelAdapter(mContext,channel_info);
            gv_channel.setAdapter(adapter);
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private View itemView;
        private Banner banner;
        public BannerViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext=mContext;
            this.banner=(Banner) itemView.findViewById(R.id.banner);
        }

        public void setData(List<ResultBeanData.ResultBean.BannerInfoBean> banner_info) {
            //设置banner的数据
            //得到图片地址
            List<String> imagesUrl=new ArrayList<>();
            for (int i = 0; i <banner_info.size() ; i++) {
                String imageUrl = banner_info.get(i).getImage();
                imagesUrl.add(imageUrl);
            }
            //设置循环指示点
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //手风琴
            banner.setBannerAnimation(Transformer.Accordion);
            banner.setImages(imagesUrl, new OnLoadImageListener() {
                @Override
                public void OnLoadImage(ImageView view, Object url) {
                    Glide.with(mContext).load(Constants.IMAGE_URL+url).into(view);
                }
            });

            //设置item的点击事件
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(mContext, "position="+position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position){
            case BANNER:
                currentType=BANNER;
                break;
            case CHANNEL:
                currentType=CHANNEL;
                break;
            case ACT:
                currentType=ACT;
                break;
            case SECKILL:
                currentType=SECKILL;
                break;
            case RECOMMEND:
                currentType=RECOMMEND;
                break;
            case HOT:
                currentType=HOT;
                break;
        }
        return currentType;
    }

    @Override
    public int getItemCount() {
        //开发过程中从1-->2
        return 6;
    }
}
