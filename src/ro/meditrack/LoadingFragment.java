package ro.meditrack;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ro.meditrack.R;
import ro.meditrack.gif.GifImageView;

import java.io.IOException;
import java.io.InputStream;
/**
 * Created by motan on 5/17/14.
 */
public class LoadingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.loading, null);
    }

}


