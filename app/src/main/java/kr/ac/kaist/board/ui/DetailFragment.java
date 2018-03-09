package kr.ac.kaist.board.ui;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.api.ArticleDetailApi;
import kr.ac.kaist.board.model.Article;
import kr.ac.kaist.board.model.Comment;
import kr.ac.kaist.board.utils.Argument;

public class DetailFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_BOARD_ID = "arg_board_id";
    private static final String ARG_DETAIL_URL = "arg_detail_url";

    /**
     *
     */

    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;

    private TextView mTitleTv;
    private TextView mContentTv;
    private TextView mWriterImgTv;
    private TextView mWriterTv;
    private TextView mTsTv;
    private View mPlusView;
    private TextView mPlusTv;
    private View mMinusView;
    private TextView mMinusTv;
    private TextView mReadCntTv;
    private TextView mCommentCntTv;

    private ListView mLv;
    private LvAdapter mLvAdapter;

    /**
     *
     */
    private int mBoard_id = -1;
    private String mDetail_url = "";


    /**
     *
     */
    private ArticleDetailApiTask mArticleDetailApiTask = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DetailFragment newInstance(int board_id, String detail_url) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOARD_ID, board_id);
        args.putString(ARG_DETAIL_URL, detail_url);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        View headerView = inflater.inflate(R.layout.detail_fragment_lv_header, null);

        mBoard_id = getArguments().getInt(ARG_BOARD_ID);
        mDetail_url = getArguments().getString(ARG_DETAIL_URL);

        /**
         * findViewById
         */
        mLv = (ListView) rootView.findViewById(R.id.lv);
        mErrorView = rootView.findViewById(R.id.error_view);
        mErrorTv = (TextView) mErrorView.findViewById(R.id.error_tv);
        mErrorPb = (ProgressBar) mErrorView.findViewById(R.id.error_pb);

        mTitleTv = (TextView) headerView.findViewById(R.id.title_tv);
        mContentTv = (TextView) headerView.findViewById(R.id.content_tv);
        mWriterImgTv = (TextView) headerView.findViewById(R.id.writer_img_tv);

        mWriterTv = (TextView) headerView.findViewById(R.id.writer_tv);
        mTsTv = (TextView) headerView.findViewById(R.id.ts_tv);

        mPlusView = headerView.findViewById(R.id.plus_view);
        mPlusTv = (TextView) headerView.findViewById(R.id.plus_tv);
        mMinusView = headerView.findViewById(R.id.minus_view);
        mMinusTv = (TextView) headerView.findViewById(R.id.minus_tv);

        mReadCntTv = (TextView) headerView.findViewById(R.id.read_tv);
        mCommentCntTv = (TextView) headerView.findViewById(R.id.comment_cnt_tv);

        /*

         */
        if (mBoard_id == Argument.ARG_BOARD_PORTAL_BOARD || mBoard_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_portal);
        } else if (mBoard_id == Argument.ARG_BOARD_ARA) {
            mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_ara);
        } else if (mBoard_id == Argument.ARG_BOARD_BF) {
            mWriterImgTv.setBackgroundResource(R.drawable.board_writer_img_iv_bg_bf);
        }

        /*

         */
        mLv.addHeaderView(headerView);
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        //View v = new View(getActivity());
        //v.setLayoutParams(params);
        //mLv.addFooterView(v);

        /*
         * ListView Setting
		 */
        ArrayList<Comment> comments = new ArrayList<Comment>();
        mLvAdapter = new LvAdapter(getActivity(), R.layout.detail_fragment_lv, comments);
        mLv.setAdapter(mLvAdapter);

        /*

         */
        mArticleDetailApiTask = new ArticleDetailApiTask();
        mArticleDetailApiTask.execute(mDetail_url);

        return rootView;
    }

    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<Comment> {
        private static final String TAG = "MainFragment LvAdapter";

        private ViewHolder viewHolder = null;
        public ArrayList<Comment> comments;
        private int textViewResourceId;

        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<Comment> comments) {
            super(context, textViewResourceId, comments);

            this.textViewResourceId = textViewResourceId;
            this.comments = comments;

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return comments.size();
        }

        @Override
        public Comment getItem(int position) {
            return comments.get(position);
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

                viewHolder.mContentTv = (TextView) convertView.findViewById(R.id.content_tv);
                viewHolder.mTsTv = (TextView) convertView.findViewById(R.id.ts_tv);
                viewHolder.mWriterTv = (TextView) convertView.findViewById(R.id.writer_tv);
                viewHolder.mWriterImgTv = (TextView) convertView.findViewById(R.id.writer_img_tv);

                viewHolder.mActionView = convertView.findViewById(R.id.action_view);
                viewHolder.mPlusView = convertView.findViewById(R.id.plus_view);
                viewHolder.mPlusTv = (TextView) convertView.findViewById(R.id.plus_tv);
                viewHolder.mMinusView = convertView.findViewById(R.id.minus_view);
                viewHolder.mMinusTv = (TextView) convertView.findViewById(R.id.minus_tv);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Comment comment = this.getItem(position);

			/*
             * Data Import and export
			 */

            viewHolder.mContentTv.setText(Html.fromHtml(comment.getContent()));
            viewHolder.mTsTv.setText(comment.getTs());
            viewHolder.mWriterTv.setText(comment.getWriter());
            viewHolder.mWriterImgTv.setText(comment.getWriter_img_url());

            if (comment.getPlus() == -1 && comment.getMinus() == -1) {
                viewHolder.mActionView.setVisibility(View.GONE);
            } else {
                viewHolder.mActionView.setVisibility(View.VISIBLE);
                viewHolder.mPlusTv.setText(Integer.toString(comment.getPlus()));
                viewHolder.mMinusTv.setText(Integer.toString(comment.getMinus()));
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

            TextView mWriterTv;
            TextView mWriterImgTv;
            TextView mTsTv;
            TextView mContentTv;

            View mActionView;
            View mPlusView;
            TextView mPlusTv;
            View mMinusView;
            TextView mMinusTv;
        }

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ArticleDetailApiTask extends AsyncTask<String, Void, Article> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        @Override
        protected Article doInBackground(String... mDetail_url) {
            Article article = null;

            try {
                ArticleDetailApi articleDetailApi = new ArticleDetailApi(getActivity().getApplication(), mBoard_id, mDetail_url[0]);
                request_code = articleDetailApi.getRequestCode();
                if (request_code == Argument.REQUEST_CODE_SUCCESS)
                    article = articleDetailApi.getResult();

            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }

            return article;
        }

        @Override
        protected void onPostExecute(Article article) {
            mArticleDetailApiTask = null;

            ApiBase.showToastMsg(getActivity().getApplication(), request_code);

            if (request_code == Argument.REQUEST_CODE_SUCCESS) {

                if (article == null) {
                    showErrorView(true, "오류가 발생해 글을 불러오지 못했습니다");

                } else {

                    updateLvHeader(article);
                    showErrorView(false, "");

                }
            } else {
                showErrorView(true, "오류가 발생해 글을 불러오지 못했습니다");
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mArticleDetailApiTask = null;

        }
    }

    /**
     * @param article
     */
    private void updateLvHeader(Article article) {

        mTitleTv.setText(article.getTitle());
        mContentTv.setText(Html.fromHtml(article.getContent()));
        mWriterTv.setText(article.getWriter());
        mWriterImgTv.setText(article.getWriter_img_url());
        mTsTv.setText(article.getTs());

        if (article.getPlus() == -1) {
            mPlusView.setVisibility(View.GONE);
        } else {
            mPlusTv.setText(String.format("%d", article.getPlus()));
        }

        if (article.getMinus() == -1) {
            mMinusView.setVisibility(View.GONE);
        } else {
            mMinusTv.setText(String.format("%d", article.getMinus()));
        }

        if (article.getRead_cnt() == -1) {
            mReadCntTv.setText(" - ");
        } else {
            mReadCntTv.setText(Integer.toString(article.getRead_cnt()));
        }

        mCommentCntTv.setText(Integer.toString(article.getComments().size()));
        mLvAdapter.comments.addAll(article.getComments());
        mLvAdapter.notifyDataSetChanged();

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

        if (mArticleDetailApiTask != null) {
            mArticleDetailApiTask.cancel(true);
        }

    }
}