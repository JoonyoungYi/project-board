package kr.ac.kaist.board.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.utils.lib_photo_view.HackyViewPager;

public class ImageActivity extends ActionBarActivity {


    private static final String ARG_IMG_URLS = "arg_img_urls";

    /**
     *
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     *
     */
    public DisplayImageOptions options;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    HackyViewPager mViewPager;

    /**
     *
     */

    private String mImg_urls[] = new String[1];

    /**
     * @param activity
     */
    public static void startActivitiy(Activity activity, String img_url) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(ARG_IMG_URLS, img_url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        mImg_urls[0] = getIntent().getExtras().getString(ARG_IMG_URLS);

        /**
         *
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /**
         *
         */
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(android.R.color.black)
                .showImageOnFail(android.R.color.black)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)

                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (HackyViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ImageFragment.newInstance(mImg_urls[position]);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
