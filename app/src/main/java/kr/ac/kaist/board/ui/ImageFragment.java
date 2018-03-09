package kr.ac.kaist.board.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.utils.Application;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "ImageFragment";
    private static final String ARG_IMG_URL = "arg_img_url";

    /**
     *
     */
    View mPb;
    PhotoView photoView;


    /**
     *
     */
    String img_url = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageFragment newInstance(String img_url) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMG_URL, img_url);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);

        mPb = rootView.findViewById(R.id.pb);
        photoView = (PhotoView) rootView.findViewById(R.id.photo_view);

        img_url = getArguments().getString(ARG_IMG_URL, null);

        ((Application) getActivity().getApplication()).imageLoader.displayImage(img_url, photoView,
                ((ImageActivity) getActivity()).options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mPb.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "Input/Output error";
                                break;
                            case DECODING_ERROR:
                                message = "Image can't be decoded";
                                break;
                            case NETWORK_DENIED:
                                message = "Downloads are denied";
                                break;
                            case OUT_OF_MEMORY:
                                message = "Out Of Memory error";
                                break;
                            case UNKNOWN:
                                message = "Unknown error";
                                break;
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        mPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mPb.setVisibility(View.GONE);
                    }
                }
        );


        return rootView;
    }
}