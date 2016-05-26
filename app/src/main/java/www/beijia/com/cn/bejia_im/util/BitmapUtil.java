package www.beijia.com.cn.bejia_im.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.BoringLayout;

/**
 * Created by Wangyingbao on 2016/5/20.
 */
public class BitmapUtil {
    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }

        Bitmap mBitmap = bitmap;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();


        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);

    }
}
