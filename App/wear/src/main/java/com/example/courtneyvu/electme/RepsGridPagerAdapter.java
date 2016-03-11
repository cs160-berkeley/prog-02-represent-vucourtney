package com.example.courtneyvu.electme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.Gravity;

import java.util.ArrayList;


public class RepsGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<ClickableRepCardFragment> representatives;

    public RepsGridPagerAdapter(Context ctx, FragmentManager fm, ArrayList<ClickableRepCardFragment> reps) {
        super(fm);
        mContext = ctx;
        this.representatives = reps;
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
            return representatives.get(col);
    }

    // Obtain the background image for the row
    @Override
    public Drawable getBackgroundForRow(int row) {
        return GridPagerAdapter.BACKGROUND_NONE;
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return 1;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return representatives.size();
    }
}