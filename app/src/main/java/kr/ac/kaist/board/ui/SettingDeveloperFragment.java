package kr.ac.kaist.board.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.model.Comment;


public class SettingDeveloperFragment extends Fragment {
    private final static String TAG = "Setting Developer Fragment";

    /**
     *
     */
    private ListView mLv;
    private ArrayList<Comment> developers = new ArrayList<Comment>();

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_developer_fragment, container, false);
        View footerView = inflater.inflate(R.layout.setting_developer_fragment_lv_footer, null, false);

        /*

         */
        init_data();

        /*

         */
        mLv = (ListView) rootView.findViewById(R.id.lv);

        /*

         */
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        View v = new View(getActivity());
        v.setLayoutParams(params);
        mLv.addHeaderView(v);

        /*

         */
        mLv.addFooterView(footerView);
        LvAdapter mLvAdapter = new LvAdapter(getActivity(), R.layout.setting_developer_fragment_lv, developers);
        mLv.setAdapter(mLvAdapter);

        return rootView;

    }

    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<Comment> {
        private static final String TAG = "SettingDeveloperFragment LvAdapter";

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
                viewHolder.mIv = (ImageView) convertView.findViewById(R.id.iv);
                viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.name_tv);
                viewHolder.mDescriptionTv = (TextView) convertView.findViewById(R.id.description_tv);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

			/*
             * Data Import and export
			 */
            Comment comment = comments.get(position);
            viewHolder.mIv.setImageResource(Integer.parseInt(comment.getId()));
            viewHolder.mNameTv.setText(comment.getWriter());
            viewHolder.mDescriptionTv.setText(comment.getContent());

            return convertView;
        }


        private class ViewHolder {
            ImageView mIv;
            TextView mNameTv;
            TextView mDescriptionTv;
        }

    }

    /**
     *
     */
    private void init_data() {

        for (int i = 0; i < 2; i++) {
            Comment comment = new Comment();

            switch (i) {
                case 0:
                    comment.setId(Integer.toString(R.drawable.setting_developer_05));
                    comment.setWriter("이준영");
                    comment.setContent("ANDROID / KAIST 전기및전자공학과 11'");
                    break;

                case 1:
                    comment.setId(Integer.toString(R.drawable.setting_developer_04));
                    comment.setWriter("조현철");
                    comment.setContent("DESIGN / KAIST 화학과 11'");
                    break;

            }

            developers.add(comment);
        }

        Collections.shuffle(developers);
    }
}
