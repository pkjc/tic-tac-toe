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

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals("o")) {
            this.setBackgroundResource(R.drawable.o);
        }else {
            this.setBackgroundResource(R.drawable.x);
        }
    }
}
