package com.example.android.newsapp.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Fragment.Business;
import com.example.android.newsapp.Fragment.Sports;
import com.example.android.newsapp.Fragment.Tech;
import com.example.android.newsapp.Fragment.World;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context m_context;

    public NewsFragmentPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        m_context = context;
    }
    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    public Fragment getItem(int pos) {
        if (pos == 0) {
            return new World();
        } else if (pos == 1) {
            return new Business();
        } else if (pos == 2) {
            return new Tech();
        } else {
            return new Sports();
        }
    }
    @Override
    public CharSequence getPageTitle(int pos) {
        if (pos == 0) {
            return m_context.getString(R.string.world_tab_title);
        } else if (pos == 1) {
            return m_context.getString(R.string.business_tab_title);
        } else if (pos == 2) {
            return m_context.getString(R.string.technology_tab_title);
        } else {
            return m_context.getString(R.string.sport_tab_title);
        }
    }
}