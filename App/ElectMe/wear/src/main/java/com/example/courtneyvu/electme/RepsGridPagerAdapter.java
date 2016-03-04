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
    //private String[] repNames = new String[]{"Sen. Serena Walters (Republican)", "Sen. Sean Johnson (Independent)", "Rep. George Hamilton (Democrat)"};

    public RepsGridPagerAdapter(Context ctx, FragmentManager fm, ArrayList<ClickableRepCardFragment> reps) {
        super(fm);
        mContext = ctx;
        this.representatives = reps;
        //this.repNames = new String[]{"Sen", "Rep", "Sen"};
    }

    // A simple container for static data in each page
    private static class Page {
        // static resources
        String textRes;
        int iconRes;
        int cardGravity = Gravity.BOTTOM;
        boolean expansionEnabled = true;

        public Page(String text, int icon) {
            textRes = text;
            iconRes = icon;
        }
    }

    /* Create a static set of pages in a 2D array
    Page p1 = new Page(this.repNames[0], R.drawable.woman1);
    Page p2 = new Page(this.repNames[1], R.drawable.man1);
    Page p3 = new Page(this.repNames[2], R.drawable.man2);
    Page p4 = new Page("Those are your Congressional representatives!", 0);
    private final Page[][] PAGES = new Page[][]{{p1, p2, p3, p4}};*/

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
            /*Page page = PAGES[row][col];
            String text = !page.textRes.equals("") ? page.textRes : null;

            Bundle info = new Bundle();
            info.putCharSequence("name", text);
            info.putInt("photo", page.iconRes);

            ClickableRepCardFragment fragment = new ClickableRepCardFragment();
            fragment.setArguments(info);
            fragment.setCardGravity(page.cardGravity);
            fragment.setExpansionEnabled(page.expansionEnabled);*/
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