package com.esp.videotogifconverter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;

public class VideoToGifConverter {

    long maxDur;
    MediaMetadataRetriever mediaMetadataRetriever = null;

    public VideoToGifConverter(Context context, Uri uri) {
        MediaMetadataRetriever tRetriever = new MediaMetadataRetriever();
        tRetriever.setDataSource(context, uri);
        mediaMetadataRetriever = tRetriever;
        String DURATION = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION);
        maxDur = (long) (1000 * Double.parseDouble(DURATION));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public VideoToGifConverter(MediaDataSource mediaDataSource) {
        MediaMetadataRetriever tRetriever = new MediaMetadataRetriever();
        tRetriever.setDataSource(mediaDataSource);
        mediaMetadataRetriever = tRetriever;
        String DURATION = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION);
        maxDur = (long) (1000 * Double.parseDouble(DURATION));
    }

    public VideoToGifConverter(FileDescriptor fileDescriptor) {
        MediaMetadataRetriever tRetriever = new MediaMetadataRetriever();
        tRetriever.setDataSource(fileDescriptor);
        mediaMetadataRetriever = tRetriever;
        String DURATION = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION);
        maxDur = (long) (1000 * Double.parseDouble(DURATION));
    }

    /**
     * Sets the delay time between each frame, or changes it for subsequent frames
     * (applies to last frame added).
     *
     * @param delay int delay time in milliseconds
     */
    public byte[] generateGIF(int delay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        GifEncoder animatedGifEncoder = new GifEncoder();
        animatedGifEncoder.setDelay(delay);

        Bitmap bmFrame;
        animatedGifEncoder.start(bos);
        for (int i = 0; i < 100; i += 10) {
            long frameTime = maxDur * i / 100;
            bmFrame = mediaMetadataRetriever.getFrameAtTime(frameTime);
            animatedGifEncoder.addFrame(bmFrame);
        }

        //last from at end
        bmFrame = mediaMetadataRetriever.getFrameAtTime(maxDur);
        animatedGifEncoder.addFrame(bmFrame);

        animatedGifEncoder.finish();
        return bos.toByteArray();
    }
}
