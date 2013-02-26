package com.runningstars.widget.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runningstars.R;

public class ResizeViewLayout extends LinearLayout {
//http://stackoverflow.com/questions/2596452/how-to-scale-resize-text-to-fit-a-textview

    public ResizeViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeViewLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // oldWidth used as a fixed width when measuring the size of the text
        // view at different font sizes
        final int oldWidth = getMeasuredWidth() - getPaddingBottom() - getPaddingTop();
        final int oldHeight = getMeasuredHeight() - getPaddingLeft() - getPaddingRight();

        // Assume we only have one child and it is the text view to scale
        TextView textView = (TextView) getChildAt(0);

        // This is the maximum font size... we iterate down from this
        // I've specified the sizes in pixels, but sp can be used, just modify
        // the call to setTextSize

        float size = getResources().getDimensionPixelSize(R.dimen.solutions_view_max_font_size);

        for (int textViewHeight = Integer.MAX_VALUE; textViewHeight > oldHeight; size -= 0.1f) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

            // measure the text views size using a fixed width and an
            // unspecified height - the unspecified height means measure
            // returns the textviews ideal height
            textView.measure(MeasureSpec.makeMeasureSpec(oldWidth, MeasureSpec.EXACTLY), MeasureSpec.UNSPECIFIED);

            textViewHeight = textView.getMeasuredHeight();
        }
    }
}