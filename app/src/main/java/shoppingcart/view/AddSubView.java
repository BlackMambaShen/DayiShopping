package shoppingcart.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liang.dayishopping.R;

public class AddSubView extends LinearLayout implements View.OnClickListener {
    private  ImageView iv_add;
    private  ImageView iv_sub;
    private  TextView tv_value;

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();
        if (!TextUtils.isEmpty(valueStr)){
            value=Integer.parseInt(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private int value=1;
    private int minValue=1;
    private int maxValue=5;
    public AddSubView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context,R.layout.add_sub_view,this);
         iv_add = (ImageView)findViewById(R.id.iv_add);
        iv_sub = (ImageView)findViewById(R.id.iv_sub);
        tv_value = (TextView)findViewById(R.id.tv_value);

        int value = getValue();
         setValue(value);
        //设置点击事件
        iv_add.setOnClickListener(this);
        iv_sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_sub:
                subNumber();
                break;
            case R.id.iv_add:
                addNumber();
                break;
        }
    }

    private void addNumber() {
        if (value<maxValue){
            value++;
        }
        setValue(value);
        if (onNumberChangeListener!=null){
            onNumberChangeListener.onNumberChange(value);
        }
    }

    private void subNumber() {
        if (value>minValue){
            value--;
        }
        setValue(value);
        if (onNumberChangeListener!=null){
            onNumberChangeListener.onNumberChange(value);
        }
    }

    //当数量改变的时候回调
    public interface OnNumberChangeListener{
        public void onNumberChange(int value);
    }

    private OnNumberChangeListener onNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }
}
