package community.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import base.BaseFragment;

public class CommunityFragment extends BaseFragment {
    private TextView textView;
    public View initView() {
        textView=new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("发现内容");
    }
}
