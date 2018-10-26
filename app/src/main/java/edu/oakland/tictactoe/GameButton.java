package edu.oakland.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

/**
 * TODO: document your custom view class.
 */
public class GameButton extends ConstraintLayout implements Observer {
    private int btnIndex;
    private Button button = null;
    private int btnCounter = 0;

    public int getBtnIndex() {
        System.out.println("btnIndex" +btnIndex);
        return btnIndex;
    }

    public void setBtnIndex(int btnIndex) {
        this.btnIndex = btnIndex;
    }

    public GameButton(Context context) {
        super(context);
        init(null, 0);

    }

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameButton, defStyle, 0);

        btnIndex = Integer.parseInt(a.getString(R.styleable.GameButton_btnIndex));

        a.recycle();
    }

    /*private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }*/

    /*protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }*/

    @Override
    public void update(Observable o, Object arg) {
        //this.setLabel(arg.toString());
    }
}
