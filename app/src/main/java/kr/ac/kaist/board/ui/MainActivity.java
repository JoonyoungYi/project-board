package kr.ac.kaist.board.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.api.LoginAraApi;
import kr.ac.kaist.board.api.LoginBfApi;
import kr.ac.kaist.board.api.LoginPortalApi;
import kr.ac.kaist.board.model.Wall;
import kr.ac.kaist.board.utils.Application;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.EmailManager;
import kr.ac.kaist.board.utils.lib_auil.AnimateFirstDisplayListener;
import me.drakeet.materialdialog.MaterialDialog;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    /**
     *
     */
    private static final String TAG = "MainActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private MainNavigationFragment mBoardNavigationFragment;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mViewPagerTabStrip;
    private View mLoginView;
    private TextView mLoginViewTitleTv;
    private TextView mLoginViewExplainTv;
    private Button mLoginButton;
    private View mPb;
    private View mEditTextView;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    final MaterialDialog mMaterialDialog = new MaterialDialog(this);

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private boolean doubleBackToExitPressedOnce = false;
    private int current_boardId = -1;

    /**
     *
     */
    private ViewPagerAdapter mViewPagerAdapter;

    /**
     * @param savedInstanceState
     */
    private LoginApiTask mLoginApiTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /**
         *
         */
        mTitle = getTitle();

        /**
         *
         */
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mLoginView = findViewById(R.id.login_view);
        mLoginViewTitleTv = (TextView) mLoginView.findViewById(R.id.title_tv);
        mLoginViewExplainTv = (TextView) mLoginView.findViewById(R.id.explain_tv);
        mLoginButton = (Button) mLoginView.findViewById(R.id.login_btn);

        /**
         *
         */
        mViewPager.setOffscreenPageLimit(3);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerTabStrip.setViewPager(mViewPager);

        /**
         *  Set up the drawer.
         */
        mBoardNavigationFragment = (MainNavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mBoardNavigationFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    /**
     *
     */
    private void startLoginDialog(final int board_id) {

        View mDialogView = getLayoutInflater().inflate(R.layout.main_login_dialog, null, false);
        mPb = mDialogView.findViewById(R.id.pb);
        mEditTextView = mDialogView.findViewById(R.id.edittext_view);
        mUsernameEditText = (EditText) mDialogView.findViewById(R.id.username_edittext);
        mPasswordEditText = (EditText) mDialogView.findViewById(R.id.password_edittext);
        TextView mTitleTv = (TextView) mDialogView.findViewById(R.id.title_tv);

        /**
         *
         */
        switch (board_id) {
            case Argument.ARG_BOARD_PORTAL_BOARD:
            case Argument.ARG_BOARD_PORTAL_NOTICE: {
                mTitleTv.setText(R.string.main_login_dialog_portal_title);
                mUsernameEditText.setHint(R.string.main_login_dialog_portal_username_edit_text_hint);
                mPasswordEditText.setHint(R.string.main_login_dialog_portal_password_edit_text_hint);
                break;
            }
            case Argument.ARG_BOARD_ARA: {
                mTitleTv.setText(R.string.main_login_dialog_ara_title);
                mUsernameEditText.setHint(R.string.main_login_dialog_ara_username_edit_text_hint);
                mPasswordEditText.setHint(R.string.main_login_dialog_ara_password_edit_text_hint);
                break;
            }
            case Argument.ARG_BOARD_BF: {
                mTitleTv.setText(R.string.main_login_dialog_bf_title);
                mUsernameEditText.setHint(R.string.main_login_dialog_bf_username_edit_text_hint);
                mPasswordEditText.setHint(R.string.main_login_dialog_bf_password_edit_text_hint);
                break;
            }
            default:
                mLoginViewTitleTv.setText(R.string.main_activity_login_title);
                mLoginViewExplainTv.setText(R.string.main_activity_login_explain);
                break;
        }

        /**
         *
         */
        mMaterialDialog.setView(mDialogView);
        mMaterialDialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginApiTask == null) {
                    mLoginApiTask = new LoginApiTask();
                    mLoginApiTask.execute(MainActivity.this.current_boardId);
                }
            }
        });
        mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });

        /**
         *
         */
        mMaterialDialog.show();
    }

    /**
     * @param id
     * @param name
     * @param slug
     */
    public void onNavigationDrawerItemSelected(int id, String name, String slug) {
        Log.d(TAG, "onNavigationDrawerItemSelected!");
        mTitle = name;
        restoreActionBar();

        changeWalls(id);
    }

    public void restoreActionBar() {
        Log.d(TAG, "Restore ActionBar : " + mTitle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mBoardNavigationFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingDeveloperActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_report) {
            EmailManager em = new EmailManager(MainActivity.this, "forasterisk.board@gmail.com", "[" + getString(R.string.app_name) + "]에 의견드립니다", "어떤 의견이 있으신가요?");
            em.startIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle Press Back key twice.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르시면, \"KAIST 보드\"에서 빠져나갑니다.", Toast.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }

    /**
     * @param board_id
     */
    private void changeWalls(int board_id) {

        this.current_boardId = board_id;

        if (ApiBase.getStringInPrefs(getApplication(), getArgumentPrefsLogin(board_id), "false").equals("false")) {

            changeLoginView(board_id);
            mLoginView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mViewPagerTabStrip.setVisibility(View.GONE);

        } else {

            mLoginView.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            mViewPagerTabStrip.setVisibility(View.VISIBLE);

            mViewPagerAdapter.walls = null;
            mViewPagerAdapter.walls = (((Application) getApplication()).findBoardById(board_id).getWalls());
            mViewPagerAdapter.notifyDataSetChanged();
            mViewPagerTabStrip.notifyDataSetChanged();
            mViewPager.setCurrentItem(0);
        }
    }

    private String getArgumentPrefsLogin(int id) {
        if (id == Argument.ARG_BOARD_PORTAL_BOARD || id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            return Argument.PREFS_PORTAL_LOGIN;
        } else if (id == Argument.ARG_BOARD_ARA) {
            return Argument.PREFS_ARA_LOGIN;
        } else {
            return null;
        }
    }

    private void changeLoginView(final int board_id) {

        switch (board_id) {
            case Argument.ARG_BOARD_PORTAL_BOARD:
            case Argument.ARG_BOARD_PORTAL_NOTICE: {
                mLoginViewTitleTv.setText(R.string.main_activity_login_portal_title);
                mLoginViewExplainTv.setText(R.string.main_activity_login_portal_explain);
                break;
            }
            case Argument.ARG_BOARD_ARA: {
                mLoginViewTitleTv.setText(R.string.main_activity_login_ara_title);
                mLoginViewExplainTv.setText(R.string.main_activity_login_ara_explain);
                break;
            }
            case Argument.ARG_BOARD_BF: {
                mLoginViewTitleTv.setText(R.string.main_activity_login_bf_title);
                mLoginViewExplainTv.setText(R.string.main_activity_login_bf_explain);
                break;
            }
            default:
                mLoginViewTitleTv.setText(R.string.main_activity_login_title);
                mLoginViewExplainTv.setText(R.string.main_activity_login_explain);
                break;
        }

        /**
         *
         */
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginDialog(board_id);
            }
        });
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/mViewPagerTabStrip/pages.
     */
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private android.support.v4.app.FragmentManager fm;
        private ArrayList<Wall> walls = new ArrayList<Wall>();

        /**
         * @param fm
         */
        public ViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        /**
         * getItem is called to instantiate the fragment for the given page.
         * Return a PlaceholderFragment (defined as a static inner class below)
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            Wall wall = walls.get(position);
            return MainFragment.newInstance(wall.getBoard_id(), wall.getTitle(), wall.getSlug());
        }

        /**
         * @param object
         * @return
         */
        @Override
        public int getItemPosition(Object object) {

            //if (object instanceof MainFragment) {
            //    ((MainFragment) object).requestRefresh();
            //}
            return POSITION_NONE;
        }

        /**
         * Show 4 total pages.
         *
         * @return
         */
        @Override
        public int getCount() {
            return walls.size();
        }

        /**
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return walls.get(position).getTitle();
        }


        /**
         * @param container
         * @param position
         * @return
         */
        public Fragment getFragment(ViewPager container, int position) {
            String tag = makeFragmentName(container.getId(), position);
            return this.fm.findFragmentByTag(tag);
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }
    }

    /**
     * Login Api Task
     */
    public class LoginApiTask extends AsyncTask<Integer, Integer, Void> {
        int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        String username = "";
        String password = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);

            username = mUsernameEditText.getText().toString();
            password = mPasswordEditText.getText().toString();
        }

        /**
         * @param board_id
         * @return
         */
        @Override
        protected Void doInBackground(Integer... board_id) {

            Log.d("LoginApiTask", "board_id -> " + board_id[0]);

            /*

             */
            try {

                switch (board_id[0]) {
                    case Argument.ARG_BOARD_PORTAL_BOARD:
                    case Argument.ARG_BOARD_PORTAL_NOTICE: {
                        LoginPortalApi loginPortalApi = new LoginPortalApi(getApplication(), username, password);
                        request_code = loginPortalApi.getRequestCode();
                        if (request_code == Argument.REQUEST_CODE_SUCCESS)
                            loginPortalApi.getResult();
                    }
                    break;
                    case Argument.ARG_BOARD_ARA: {
                        LoginAraApi loginPortalApi = new LoginAraApi(getApplication(), username, password);
                        request_code = loginPortalApi.getRequestCode();
                        if (request_code == Argument.REQUEST_CODE_SUCCESS)
                            loginPortalApi.getResult();
                    }
                    break;
                    case Argument.ARG_BOARD_BF: {
                        LoginBfApi loginPortalApi = new LoginBfApi(getApplication(), username, password);
                        request_code = loginPortalApi.getRequestCode();
                        if (request_code == Argument.REQUEST_CODE_SUCCESS)
                            loginPortalApi.getResult();
                    }
                    break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mLoginApiTask = null;
            ApiBase.showToastMsg(getApplication(), request_code);

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                changeWalls(current_boardId);
                mMaterialDialog.dismiss();

            } else {
                mPasswordEditText.requestFocus();
                mPasswordEditText.setError("아이디와 비밀번호를 다시 한 번 확인해 주세요");
                showProgress(false);
            }


        }

        @Override
        protected void onCancelled() {

            showProgress(false);

            super.onCancelled();
            mLoginApiTask = null;
        }

        /**
         * Shows the progress UI and hides the login form.
         */
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        private void showProgress(final boolean show) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mPb.setVisibility(View.VISIBLE);
                mPb.animate()
                        .setDuration(shortAnimTime)
                        .alpha(show ? 1 : 0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mPb.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                            }
                        });

                mEditTextView.setVisibility(View.VISIBLE);
                mEditTextView.animate()
                        .setDuration(shortAnimTime)
                        .alpha(show ? 0 : 1)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mEditTextView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                            }
                        });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mPb.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                mEditTextView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AnimateFirstDisplayListener.displayedImages.clear();

        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }
    }


}
