package io.nothing.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.nothing.R;
/**
 * Created by sanvi on 5/20/14.
 */
public class ProgressImageView extends RelativeLayout {
  private Context mContext;
  private TextView progressText;
  private ImageView progressImageView;

  public ProgressImageView(Context context) {
    super(context);
    initViews(context);
  }

  public ProgressImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initViews(context);
  }

  public ProgressImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setProgress(int progress) {
    progressText.setText(String.format("%d", progress) + "%");
  }

  public void setProgressVisibility(int visibility) {
    progressText.setVisibility(visibility);
  }
  public ImageView getImageView(){
    return progressImageView;
  }

  private void initViews(Context context) {
    this.mContext = context;
    LayoutInflater.from(mContext).inflate(R.layout.widget_progress_imageview, this, true);
    progressText = (TextView) findViewById(R.id.widget_progress_text);
    progressImageView = (ImageView) findViewById(R.id.widget_progress_imageview);

  }
}