package com.example.liang.dayishopping.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.liang.dayishopping.R;

import java.util.ArrayList;

import base.BaseFragment;
import community.fragment.CommunityFragment;
import home.fragment.HomeFragment;
import shoppingcart.fragment.ShoppingcartFragment;
import type.fragment.TypeFragment;
import user.fragment.UserFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment>fragments;
    private int position=0;
    //缓存的Fragment
    private Fragment tempFragemnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         rg_main = (RadioGroup)findViewById(R.id.rg_main);
         //初始化fragment
         initFragment();
         initListener();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        position=0;
                        break;
                    case R.id.rb_type:
                        position=1;
                        break;
                    case R.id.rb_community:
                        position=2;
                        break;
                    case R.id.rb_cart:
                        position=3;
                        break;
                    case R.id.rb_user:
                        position=4;
                        break;
                    default:
                        position=0;
                        break;
                }
                //根据位置取不同的fragment
                BaseFragment fragment = getFragment(position);
                //第一个参数：上次显示的fragment.第二个参数：当前正要显示的fragment
                switchFragment(tempFragemnt,fragment);
            }
        });
        rg_main.check(R.id.rb_home);
    }

    private void initFragment() {
        fragments=new ArrayList<BaseFragment>();
        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new ShoppingcartFragment());
        fragments.add(new UserFragment());
    }

    private BaseFragment getFragment(int position){
        if (fragments!=null&&fragments.size()>0){
            BaseFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    private void switchFragment(Fragment fromFragment,BaseFragment nextFragment){
        if (tempFragemnt != nextFragment) {
            tempFragemnt = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.add(R.id.frameLayout, nextFragment).commit();
                } else { //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                    }
                }
            }
        }
}


