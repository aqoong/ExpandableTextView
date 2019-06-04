package com.aqoong.lib.expandabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class ExpandableTextView extends AppCompatTextView {
    private final String TAG = getClass().getSimpleName();

    private int     collapseLine;
    private String  strMore;            //option
    private int     index;

    private boolean isOverLine = false;

    public enum STATE{
        COLLAPSE,
        EXPAND
    }
    private STATE state = STATE.COLLAPSE;

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

            if(strMore  == null){
                strMore = "More";
            }
        }

        initView();
    }

    private void initView(){
        checkOverLine();
    }

    public void checkOverLine(){
        final AppCompatTextView view = this;
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if(getLineCount() > collapseLine){
                    isOverLine = true;
                    int lineEndIndex = getLayout().getLineEnd(collapseLine);
                    switch (state){
                        case COLLAPSE:
                            appendMoreText(lineEndIndex);
                            break;
                        case EXPAND:
                            if(getTag() != null)
                                setText(getTag().toString());
                            else
                                setText("error");
                            break;
                    }
                }
                return true;
            }
        });
    }

    public void setExpandableTextView(ExpandableTextView view){
        this.collapseLine = view.getCollapseLine();
        this.isOverLine = view.isOverLine();
        this.state = view.getState();

        setText(view.getIndex(), view.getText(), view.getMoreText());
    }

    private void appendMoreText(int lineEndIndex){
        SpannableString expandText = createSpannableString(strMore);
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private SpannableString createSpannableString(String moreText){
        SpannableString spannableString = new SpannableString(moreText);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.expandable_default_more_color)), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, moreText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    //for list
    public ExpandableTextView setText(int index, String text, String moreText){
        this.index = index;
        this.setText(text, moreText);

        return this;
    }

    public ExpandableTextView setText(String text, String moreText){
        this.setTag(text);
        this.setText(text);
        this.strMore = moreText;

        if(state.equals(STATE.COLLAPSE)) {
            checkOverLine();
        }else{
            this.setText(this.getTag().toString());
        }
        return this;
    }
    public ExpandableTextView setMoreText(String moreText){
        this.strMore = moreText;
        this.setText(getText(), strMore);

        return this;
    }


    public ExpandableTextView setCollapseLine(int line){
        this.collapseLine = line;
        return this;
    }
    public void setState(STATE state){
        this.state = state;
        initView();
    }
    public String getText(){
        return this.getTag() != null ? this.getTag().toString() : "";
    }
    public String getMoreText(){return this.strMore;}
    public int getCollapseLine() { return collapseLine; }
    public boolean isOverLine() { return isOverLine; }
    public STATE getState() { return state; }

    public int getIndex(){
        return this.index;
    }
}
