package ro.meditrack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.SystemClock;
import android.view.View;

import java.io.InputStream;

/**
 * Created by motan on 5/18/14.
 */
public class GIFView extends View {

    private InputStream mStream;
    private Movie mMovie;
    private long mMoviestart;

    public GIFView(Context context, InputStream stream) {
        super(context);

        mStream = stream;
        mMovie = Movie.decodeStream(mStream);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);

        final long now = SystemClock.uptimeMillis();


        if (mMoviestart == 0) {
            mMoviestart = now;
        }

        final int reloadTime = (int)((now - mMoviestart) % mMovie.duration());
        mMovie.setTime(reloadTime);
        mMovie.draw(canvas, 10, 10);
        this.invalidate();
    }
}
