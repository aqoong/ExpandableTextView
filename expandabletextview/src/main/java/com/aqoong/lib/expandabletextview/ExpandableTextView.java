package com.aqoong.lib.expandabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class ExpandableTextView extends AppCompatTextView {
    private final String TAG = getClass().getSimpleName();

    private int     collapseLine;
    private String  strMore;            //option

    private boolean isOverLine;

    public enum STATE{
        COLLAPSE,
        EXPAND
    }
    private STATE state;
    private Handler mHandler;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        try{
            collapseLine    = typedArray.getInteger(R.styleable.ExpandableTextView_collapseLine, 2);
            strMore         = typedArray.getString(R.styleable.ExpandableTextView_text_more);
        }finally {
            typedArray.recycle();
            state       = STATE.COLLAPSE;
            isOverLine  = false;

            if(strMore  == null){
                strMore = "More";
            }
        }

        initView();
    }

    private void initView(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                appendMoreText(msg.what);
            }
        };

        checkOverLine();
    }

    private void checkOverLine(){
        final AppCompatTextView view = this;
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if(getLineCount() > collapseLine){
                    isOverLine = true;
                    int lineEndIndex = getLayout().getLineEnd(collapseLine-1);
                    mHandler.sendEmptyMessage(lineEndIndex);
                }
                return true;
            }
        });
    }

    private void appendMoreText(int lineEndIndex){
        SpannableString expandText = createSpannableString(strMore);
        setText(getText().subSequence(0, lineEndIndex - expandText.length() + 1));
        append("...  ");
        append(expandText);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin = getTag().toString();
                setText(origin);
                state = STATE.EXPAND;
            }
        });
        invalidate();
    }

    private SpannableString createSpannableString(String moreText){
        SpannableString spannableString = new SpannableString(moreText);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.expandable_default_more_color)), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
    public ExpandableTextView setText(String text, String moreText){
        this.setTag(text);
        this.setText(text);
        this.strMore = moreText;
        Log.i(TAG, String.format("isOverLine : %s / state : %s", isOverLine, state));
        if(state.equals(STATE.COLLAPSE)) {
            checkOverLine();
        }

        return this;
    }

}
