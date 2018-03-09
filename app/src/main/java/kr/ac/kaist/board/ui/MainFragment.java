package kr.ac.kaist.board.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.api.ArticleListApi;
import kr.ac.kaist.board.model.Article;
import kr.ac.kaist.board.model.Attachment;
import kr.ac.kaist.board.utils.Application;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.lib_auil.AnimateFirstDisplayListener;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_WALL_BOARD_ID = "arg_wall_board_id";
    private static final String ARG_WALL_SLUG = "arg_wall_slug";
    private static final String ARG_WALL_NAME = "arg_wall_name";

    /**
     *
     */

    /**
     *
     */

    private DownloadManager downloadManager;

    private DownloadManager.Request request;
    private Uri urlToDownload;

    /**
     *
     */

    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;
    private ProgressBar mFooterPb;

    private ListView mLv;
    private LvAdapter mLvAdapter;

    /**
     *
     */
    private int mBoard_id = -1;
    private int mPage = 0;
    private String mSlug = "";
    private ArticleListApiTask mArticleListApiTask = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int id, String name, String slug) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WALL_BOARD_ID, id);
        args.putString(ARG_WALL_SLUG, slug);
        args.putString(ARG_WALL_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        View lvFooterView = inflater.inflate(R.layout.main_fragmnet_lv_footer, null, false);

        /**
         *
         */
        mSlug = getArguments().getString(ARG_WALL_SLUG);
        mBoard_id = getArguments().getInt(ARG_WALL_BOARD_ID);
        Log.d(TAG, "mBoard_id -> "+ mBoard_id);

        /**
         * UI indexing
         */
        mLv = (ListView) rootView.findViewById(R.id.lv);
        mErrorView = rootView.findViewById(R.id.error_view);
        mErrorTv = (TextView) mErrorView.findViewById(R.id.error_tv);
        mErrorPb = (ProgressBar) mErrorView.findViewById(R.id.error_pb);
        mFooterPb = (ProgressBar) lvFooterView.findViewById(R.id.pb);

        /*

         */
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mLv.addHeaderView(new View(getActivity()));
        mLv.addFooterView(lvFooterView);
        mLv.setOnScrollListener(new PauseOnScrollListener(((Application) getActivity().getApplication()).imageLoader, true, true));

        /*
         * ListView Setting
		 */
        ArrayList<Article> articles = new ArrayList<Article>();
        mLvAdapter = new LvAdapter(getActivity(), R.layout.main_fragment_lv,
                articles);
        mLv.setAdapter(mLvAdapter);
        requestRefresh();

        /**
         *
         */

        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        return rootView;
    }

    /**
     *
     */

    public void requestRefresh() {
        if (mArticleListApiTask == null) {

            mArticleListApiTask = new ArticleListApiTask();
            mArticleListApiTask.execute();
        }

    }

    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<Article> {
        private static final String TAG = "MainFragment LvAdapter";

        /**
         *
         */
        private static final int TAG_KEY_VIEWHOLDER = R.string.tag_key_viewholder;
        private static final int TAG_KEY_ID = R.string.tag_key_id;
        private static final int TAG_KEY_NAME = R.string.tag_key_name;
        private static final int TAG_KEY_URL = R.string.tag_key_url;
        private static final int TAG_KEY_MIMETYPE = R.string.tag_key_mimetype;
        private static final int TAG_KEY_DETAIL_URL = R.string.tag_key_detail_url;

        /**
         *
         */
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        /**
         *
         */
        private Resources r;
        private ViewHolder viewHolder = null;
        public ArrayList<Article> articles;
        private int textViewResourceId;

        /**
         * @param context
         * @param textViewResourceId
         * @param articles
         */
        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<Article> articles) {
            super(context, textViewResourceId, articles);

            this.textViewResourceId = textViewResourceId;
            this.articles = articles;

            this.r = context.getResources();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int position) {
            return articles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

			/*
             * UI Initiailizing : View Holder
			 */

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(textViewResourceId, null);

                viewHolder = new ViewHolder();

                /**
                 * Find View By ID
                 */

                viewHolder.mContainer = convertView.findViewById(R.id.container);

                viewHolder.mTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
                viewHolder.mTsTv = (TextView) convertView.findViewById(R.id.ts_tv);
                viewHolder.mWriterTv = (TextView) convertView.findViewById(R.id.writer_tv);
                viewHolder.mWriterImgTv = (TextView) convertView.findViewById(R.id.writer_img_tv);

                viewHolder.mImageContainerView = convertView.findViewById(R.id.image_container_view);
                viewHolder.mImageContainerIv = (ImageView) viewHolder.mImageContainerView.findViewById(R.id.iv);

                viewHolder.mAttachmentView = convertView.findViewById(R.id.attachment_view);
                viewHolder.mAttachmentDividerView = convertView.findViewById(R.id.attachment_divider_view);
                viewHolder.mAttachmentViews[0] = convertView.findViewById(R.id.attachment_view_0);
                viewHolder.mAttachmentViews[1] = convertView.findViewById(R.id.attachment_view_1);
                viewHolder.mAttachmentViews[2] = convertView.findViewById(R.id.attachment_view_2);
                viewHolder.mAttachmentTvs[0] = (TextView) convertView.findViewById(R.id.attachment_tv_0);
                viewHolder.mAttachmentTvs[1] = (TextView) convertView.findViewById(R.id.attachment_tv_1);
                viewHolder.mAttachmentTvs[2] = (TextView) convertView.findViewById(R.id.attachment_tv_2);
                viewHolder.mAttachmentIvs[0] = (ImageView) convertView.findViewById(R.id.attachment_iv_0);
                viewHolder.mAttachmentIvs[1] = (ImageView) convertView.findViewById(R.id.attachment_iv_1);
                viewHolder.mAttachmentIvs[2] = (ImageView) convertView.findViewById(R.id.attachment_iv_2);

                viewHolder.mReadTv = (TextView) convertView.findViewById(R.id.read_tv);

                viewHolder.mPlusView = convertView.findViewById(R.id.plus_view);
                viewHolder.mPlusTv = (TextView) convertView.findViewById(R.id.plus_tv);

                viewHolder.mMinusView = convertView.findViewById(R.id.minus_view);
                viewHolder.mMinusTv = (TextView) convertView.findViewById(R.id.minus_tv);

                viewHolder.mCommentView = convertView.findViewById(R.id.comment_view);
                viewHolder.mCommentTv = (TextView) convertView.findViewById(R.id.comment_tv);

                /**
                 * Init Set On Click Listener
                 */

                viewHolder.mContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailActivity.startActivity(getActivity(), mBoard_id, (String) v.getTag(TAG_KEY_DETAIL_URL));
                    }
                });

                viewHolder.mImageContainerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageActivity.startActivitiy(getActivity(), (String) v.getTag());
                    }
                });

                for (int i = 0; i < 3; i++) {
                    viewHolder.mAttachmentViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (v.getTag(TAG_KEY_DETAIL_URL) == null) {
                                urlToDownload = Uri.parse("https://portal.kaist.ac.kr" + v.getTag(TAG_KEY_URL));
                                request = new DownloadManager.Request(urlToDownload);
                                request.setTitle((String) v.getTag(TAG_KEY_NAME));
                                request.addRequestHeader("Connection", "keep-alive");
                                request.addRequestHeader("Cookie", ApiBase.getStringInPrefs(getActivity().getApplication(), Argument.PREFS_PORTAL_COOKIE, ""));
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, (String) v.getTag(TAG_KEY_NAME));
                                request.allowScanningByMediaScanner();
                                if (v.getTag(TAG_KEY_MIMETYPE) != null) {
                                    request.setMimeType((String) v.getTag(TAG_KEY_MIMETYPE));
                                }

                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                                downloadManager.enqueue(request);

                            } else {
                                DetailActivity.startActivity(getActivity(), mBoard_id, (String) v.getTag(TAG_KEY_DETAIL_URL));

                            }
                        }
                    });
                }

                viewHolder.mPlusView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "PLUS CLICKED!", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.mMinusView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "MINUS CLICKED!", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.mCommentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Comment CLICKED!", Toast.LENGTH_SHORT).show();
                    }
                });


                convertView.setTag(TAG_KEY_VIEWHOLDER, viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag(TAG_KEY_VIEWHOLDER);
            }

            final Article article = this.getItem(position);
            ArrayList<Attachment> attachments = article.getAttachments();

			/*
             * Data Import and export
			 */

            viewHolder.mContainer.setTag(TAG_KEY_DETAIL_URL, article.getDetail_url());

            //
            viewHolder.mTitleTv.setText(article.getTitle() + " " + article.getContent());
            viewHolder.mWriterTv.setText(article.getWriter());
            viewHolder.mWriterImgTv.setText(article.getWriter_img_url());

            //
            if (article.getTs() != null) {
                viewHolder.mTsTv.setText(article.getTs());
            } else {
                viewHolder.mTsTv.setText(" - ");
            }

            //
            if (article.getImgs().size() > 0) {
                String img_url = "http://portal.kaist.ac.kr" + article.getImgs().get(0).getUrl();
                ((Application) getActivity().getApplication()).imageLoader
                        .displayImage(img_url, viewHolder.mImageContainerIv, ((Application) getActivity().getApplication()).options, animateFirstListener);
                viewHolder.mImageContainerView.setTag(img_url);
                viewHolder.mImageContainerView.setVisibility(View.VISIBLE);
                viewHolder.mImageContainerIv.setVisibility(View.VISIBLE);

            } else {
                viewHolder.mImageContainerIv.clearAnimation();
                viewHolder.mImageContainerView.setVisibility(View.GONE);
                viewHolder.mImageContainerIv.setVisibility(View.GONE);
            }

            //
            if (attachments.size() == 0) {
                viewHolder.mAttachmentView.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < 3; i++) {
                    if (i < attachments.size()) {
                        viewHolder.mAttachmentViews[i].setVisibility(View.VISIBLE);
                        viewHolder.mAttachmentTvs[i].setText(attachments.get(i).getName());
                        viewHolder.mAttachmentIvs[i].setImageResource(attachments.get(i).getIcon_res_id());

                        viewHolder.mAttachmentViews[i].setTag(TAG_KEY_NAME, attachments.get(i).getName());
                        viewHolder.mAttachmentViews[i].setTag(TAG_KEY_URL, attachments.get(i).getUrl());
                        viewHolder.mAttachmentViews[i].setTag(TAG_KEY_MIMETYPE, attachments.get(i).getMime_type());

                    } else {
                        viewHolder.mAttachmentViews[i].setVisibility(View.GONE);
                    }
                }

                if (attachments.size() > 3) {
                    viewHolder.mAttachmentTvs[2].setText((attachments.size() - 2) + "개 더보기");
                    viewHolder.mAttachmentTvs[2].setTextColor(r.getColor(R.color.board_textcolor_weak));
                    viewHolder.mAttachmentIvs[2].setImageResource(R.drawable.board_file_more);
                    viewHolder.mAttachmentViews[2].setTag(TAG_KEY_ID, article.getId());
                } else if (attachments.size() == 3) {
                    viewHolder.mAttachmentTvs[2].setTextColor(r.getColor(R.color.board_textcolor_strong));
                }

                //
                viewHolder.mAttachmentView.setVisibility(View.VISIBLE);
            }

            //
            if (article.getPlus() == -1) {
                viewHolder.mPlusView.setVisibility(View.GONE);
                viewHolder.mPlusTv.setText(" - ");
            } else {
                viewHolder.mPlusView.setVisibility(View.VISIBLE);
                viewHolder.mPlusTv.setText(Integer.toString(article.getPlus()));
            }

            //
            if (article.getMinus() == -1) {
                viewHolder.mMinusView.setVisibility(View.GONE);
            } else {
                viewHolder.mMinusView.setVisibility(View.VISIBLE);
                viewHolder.mMinusTv.setText(Integer.toString(article.getMinus()));
            }

            //
            if (article.getComment_cnt() == -1) {
                viewHolder.mCommentView.setVisibility(View.GONE);
            } else {
                viewHolder.mCommentView.setVisibility(View.VISIBLE);
                viewHolder.mCommentTv.setText(Integer.toString(article.getComment_cnt()));
            }

            //
            if (article.getRead_cnt() == -1) {
                viewHolder.mReadTv.setText(" - ");
            } else {
                viewHolder.mReadTv.setText(Integer.toString(article.getRead_cnt()));
            }

            /*
                Load More Data...
             */
            if (position >= (articles.size() - 2) && mArticleListApiTask == null) {
                mArticleListApiTask = new ArticleListApiTask();
                mArticleListApiTask.execute();
            }

            //

            if (mBoard_id == Argument.ARG_BOARD_PORTAL_BOARD || mBoard_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
                viewHolder.mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_portal);
            } else if (mBoard_id == Argument.ARG_BOARD_ARA) {
                viewHolder.mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_ara);
            } else if (mBoard_id == Argument.ARG_BOARD_BF) {
                viewHolder.mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_bf);
            }

            return convertView;
        }

        private class ViewHolder {

            View mContainer;

            TextView mWriterImgTv;
            TextView mWriterTv;
            TextView mTsTv;
            TextView mTitleTv;

            View mImageContainerView;
            ImageView mImageContainerIv;

            View mAttachmentView;
            View mAttachmentDividerView;
            View mAttachmentViews[] = new View[3];
            TextView mAttachmentTvs[] = new TextView[3];
            ImageView mAttachmentIvs[] = new ImageView[3];

            TextView mReadTv;

            View mPlusView;
            TextView mPlusTv;

            View mMinusView;
            TextView mMinusTv;

            View mCommentView;
            TextView mCommentTv;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ArticleListApiTask extends AsyncTask<Void, Void, ArrayList<Article>> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        @Override
        protected void onPreExecute() {

            mFooterPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Article> doInBackground(Void... param) {
            ArrayList<Article> articles = null;

            try {

                //This Random Sleep Method makes viewpager be more faster.
                if (mPage == 0)
                    Thread.sleep((int) (700 + Math.random() * 1300));

                ArticleListApi storeListApi = new ArticleListApi(getActivity().getApplication(), mBoard_id, mSlug, mPage + 1);
                request_code = storeListApi.getRequestCode();
                articles = storeListApi.getResult();

            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }

            return articles;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> stores) {

            mArticleListApiTask = null;
            mFooterPb.setVisibility(View.GONE);

            ApiBase.showToastMsg(getActivity().getApplication(), request_code);

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {

                mPage++;
                showErrorView(false, "");
                mLvAdapter.articles.addAll(stores);
                mLvAdapter.notifyDataSetChanged();

            } else if (request_code == Argument.REQUEST_CODE_NO_DATA) {

                if (mLvAdapter.articles.size() == 0) {
                    showErrorView(true, "데이터가 없습니다");
                }

            } else if (request_code == Argument.REQUEST_CODE_NOT_LOGIN ){

                showErrorView(true, "로그인에 실패해 글을 불러오지 못했습니다");

                Log.d(TAG, "Login Fail!");

            } else {
                showErrorView(true, "오류가 발생해 글을 불러오지 못했습니다");
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mArticleListApiTask = null;

        }

    }

    /**
     * @param show
     */
    private void showErrorView(final boolean show, String msg) {

        if (show) {
            mLv.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
            mErrorTv.setText(msg);

            if (msg.equals("")) {
                mErrorPb.setVisibility(View.VISIBLE);
            } else {
                mErrorPb.setVisibility(View.GONE);
            }

        } else {
            mLv.setVisibility(View.VISIBLE);
            mErrorPb.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mArticleListApiTask != null) {
            mArticleListApiTask.cancel(true);
        }
    }
}
