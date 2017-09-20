package com.esp.videotogif;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esp.videotogifconverter.GifEncoder;
import com.esp.videotogifconverter.VideoToGifConverter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView gifView;
    Button btnOpen, btnSave;
    VideoToGifConverter converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpen = (Button) findViewById(R.id.open);
        btnSave = (Button) findViewById(R.id.save);
        btnOpen.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        gifView = (ImageView) findViewById(R.id.gif_view);
    }

    @Override
    public void onClick(View view) {
        if (view == btnOpen) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/mp4");
            startActivityForResult(intent, 0);
        } else if (view == btnSave) {
            if (converter != null) {
                new SaveGif("test").execute();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            converter = new VideoToGifConverter(getBaseContext(), uri);
        }
    }

    class SaveGif extends AsyncTask<Void, Integer, String> {

        private String fillname;
        private ProgressDialog progressDialog;

        public SaveGif(String fillname) {
            this.fillname = fillname;
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File outFile = new File(extStorageDirectory, fillname + ".GIF");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
                bos.write(converter.generateGIF(1000));
                bos.flush();
                bos.close();
                return (outFile.getAbsolutePath() + " Saved");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values + "");
        }

        @Override
        protected void onPostExecute(String s) {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File file = new File(extStorageDirectory, fillname + ".GIF");
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            Glide.with(MainActivity.this).asGif().load(file).into(gifView);
            progressDialog.cancel();
        }
    }
}
