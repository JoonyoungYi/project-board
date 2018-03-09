package kr.ac.kaist.board.utils.lib_auil;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yearnning on 2014. 8. 6..
 */
public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {

            ImageView imageView = (ImageView) view;
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                if (imageView.getVisibility() == View.VISIBLE) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                }
                displayedImages.add(imageUri);
            }
            displayedImages.add(imageUri);

        }
    }
}