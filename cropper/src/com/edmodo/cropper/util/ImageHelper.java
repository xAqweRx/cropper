package com.edmodo.cropper.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;


public class ImageHelper {

    static Integer AXIS_X_VAL = null;
    static Integer AXIS_Y_VAL = null;

    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;

    public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight ) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if ( height > reqHeight || width > reqWidth ) {
            inSampleSize = Math.round( Math.max( height / reqHeight, width / reqWidth ) );
        }

        return inSampleSize;
    }

    public static Bitmap scaledBitmap( Context context, String path, Integer width, Integer height, Integer scale ) {
        long startTime = System.nanoTime();

        BitmapFactory.Options checkOptions = new BitmapFactory.Options();
        checkOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile( path, checkOptions );
        BitmapFactory.Options options = new BitmapFactory.Options();

        int destWidth = ( width != null ? width : getScreenResolution( context, AXIS_X ) );
        int destHeight = ( height != null ? height : getScreenResolution( context, AXIS_Y ) );

        options.inSampleSize = ImageHelper.calculateInSampleSize( checkOptions, destWidth * scale, destHeight * scale );

        Bitmap res = BitmapFactory.decodeFile( path, options );

        long stopTime = System.nanoTime();
        Log.d( "Scale photo", "Time MILLISECONDS " + TimeUnit.MILLISECONDS.convert( stopTime - startTime, TimeUnit.NANOSECONDS ) +
                        " Dest width " + destWidth +
                        " Dest height " + destHeight +
                        " Scale " + scale +
                        " Source width " + checkOptions.outWidth +
                        " Source height " + checkOptions.outHeight
        );
        return res;

    }

    public static Bitmap scaledBitmap( Context context, String path, Integer width, Integer height ) {
        return scaledBitmap( context, path, width, height, 1 );
    }

    public static Bitmap scaledBitmap( Context context, String path, Integer scale ) {
        return scaledBitmap( context, path, null, null, scale == null ? 1 : scale );
    }

    public static Bitmap scaledBitmap( Context context, String path ) {
        return scaledBitmap( context, path, null, null, 1 );
    }


    public static Bitmap scaledDrawable( Context context, Integer drawable, Integer width, Integer height, Integer scale ) {
        long startTime = System.nanoTime();

        BitmapFactory.Options checkOptions = new BitmapFactory.Options();
        checkOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeResource( context.getResources(), drawable, checkOptions );
        BitmapFactory.Options options = new BitmapFactory.Options();

        int destWidth = ( width != null ? width : getScreenResolution( context, AXIS_X ) );
        int destHeight = ( height != null ? height : getScreenResolution( context, AXIS_Y ) );

        options.inSampleSize = ImageHelper.calculateInSampleSize( checkOptions, destWidth * scale, destHeight * scale );

        Bitmap res = BitmapFactory.decodeResource( context.getResources(), drawable, options );

        long stopTime = System.nanoTime();
        Log.d( "Scale photo", "Time MILLISECONDS " + TimeUnit.MILLISECONDS.convert( stopTime - startTime, TimeUnit.NANOSECONDS ) +
                        " Dest width " + destWidth +
                        " Dest height " + destHeight +
                        " Scale " + scale +
                        " Source width " + checkOptions.outWidth +
                        " Source height " + checkOptions.outHeight
        );
        return res;
    }

    public static Bitmap scaledDrawable( Context context, Integer drawable ) {
        return scaledDrawable( context, drawable, null, null, 1 );
    }

    public static Bitmap scaledBitmapUri( Context context, Uri uri ) {
        return scaledBitmap( context, uri.getPath() );
    }

    public static Bitmap scaledBitmapUri( Context context, Uri uri, Integer width, Integer height ) {
        return scaledBitmap( context, uri.getPath(), width, height, 1 );
    }


    public static BitmapFactory.Options orignSize( String path ) {

        BitmapFactory.Options checkOptions = new BitmapFactory.Options();
        checkOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile( path, checkOptions );

        return checkOptions;
    }

    public static Integer getScreenResolution( Context context, Integer axis ) {
        switch ( axis ) {
            case AXIS_X:
                if ( AXIS_X_VAL == null )
                    AXIS_X_VAL = getDisplayMetricks( context ).widthPixels;
                return AXIS_X_VAL;
            case AXIS_Y:
                if ( AXIS_Y_VAL == null )
                    AXIS_Y_VAL = getDisplayMetricks( context ).heightPixels;
                return AXIS_Y_VAL;
            default:
                return 0;
        }

    }

    private static DisplayMetrics getDisplayMetricks( Context context ) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics( metrics );
        return metrics;
    }


}
