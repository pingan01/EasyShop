package com.zx.easyshop.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zx.easyshop.R;

/**
 * Created by Administrator on 2017/2/7 0007.
 *
 * 未登录时的Fragment
 */

public class UnLoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_un_login,container,false);
    }
}
