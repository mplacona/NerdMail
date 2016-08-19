package com.bignerdranch.android.nerdmail.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bignerdranch.android.nerdmail.NerdMailApplication;
import com.bignerdranch.android.nerdmail.R;
import com.bignerdranch.android.nerdmail.model.DataManager;
import com.bignerdranch.android.nerdmailservice.Email;

import javax.inject.Inject;

public class EmailListItemView extends View implements View.OnTouchListener {
    private static final int PADDING_SIZE = 16;
    private static final int BODY_PADDING_SIZE = 4;
    private static final int LARGE_TEXT_SIZE = 16;
    private static final int SMALL_TEXT_SIZE = 14;
    private static final int STAR_SIZE = 24;
    private static final int DIVIDER_SIZE = 1;

    private Paint mBackgroundPaint;
    private Paint mDividerPaint;
    private Paint mStarPaint;
    private TextPaint mSenderAddressTextPaint;
    private TextPaint mSubjectTextPaint;
    private TextPaint mBodyTextPaint;

    private Bitmap mImportantStar;
    private Bitmap mUnimportantStar;

    private Email mEmail;


    float mScreenDensity;
    float mPaddingSize;
    float mStarPixelSize;
    float mDividerSize;
    float mLargeTextSize;
    float mSmallTextSize;
    float mStarLeft;
    float mStarTop;

    @Inject
    DataManager mDataManager;

    public EmailListItemView(Context context) {
        this(context, null);
    }

    public EmailListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);

        NerdMailApplication.component(context).inject(this);

        mScreenDensity = context.getResources().getDisplayMetrics().density;

        mStarPixelSize = Math.round(STAR_SIZE * mScreenDensity);
        mDividerSize = Math.round(DIVIDER_SIZE * mScreenDensity);
        mPaddingSize = Math.round(PADDING_SIZE * mScreenDensity);

        //Scale text size based on screen density and accessibility settings
        Configuration configuration = context.getResources().getConfiguration();
        float textScale = configuration.fontScale * mScreenDensity;


        mLargeTextSize = Math.round(LARGE_TEXT_SIZE * textScale);
        mSmallTextSize = Math.round(SMALL_TEXT_SIZE * textScale);

        setupPaints();
        setupBitmaps();

    }

    public void setEmail(Email email){
        mEmail = email;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
            width = widthSize;
        }else{
            width = calculateWidth();
        }

        if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
            height = heightSize;
        }else{
            height = calculateHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        // Draw paint to clear canvas
        canvas.drawPaint(mBackgroundPaint);

        // Draw the divider across the bottom of the canvas
        float dividerY = height - (mDividerSize / 2);
        canvas.drawLine(0, dividerY, width, dividerY, mDividerPaint);

        // Draw the sender address
        Paint.FontMetrics fm  = mSenderAddressTextPaint.getFontMetrics();
        float senderX = mPaddingSize;
        float senderTop = (float) Math.ceil(Math.abs(fm.top));
        float senderBottom = (float) Math.ceil(Math.abs(fm.bottom));
        float senderBaseLine = mPaddingSize = senderTop;
        float senderY = senderBaseLine + senderBottom;

        canvas.drawText(mEmail.getSenderAddress(), senderX, senderBaseLine, mSenderAddressTextPaint);

        // Draw the subject
        Paint.FontMetrics subjectFm = mSubjectTextPaint.getFontMetrics();
        float subjectX = PADDING_SIZE * mScreenDensity;
        float subjectTop = (float) Math.ceil(Math.abs(subjectFm.top));
        float subjectBottom = (float) Math.ceil(Math.abs(subjectFm.bottom));
        float subjectBaseline = senderY + subjectTop;
        float subjectY = subjectBaseline + subjectBottom;
        canvas.drawText(mEmail.getSubject(), subjectX, subjectBaseline, mSubjectTextPaint);

        // Draw the body
        Paint.FontMetrics bodyFm = mBodyTextPaint.getFontMetrics();
        float bodyX = mPaddingSize;
        float bodyTop = (float) Math.ceil(Math.abs(bodyFm.top));
        float bodyBottom = (float) Math.ceil(Math.abs(bodyFm.bottom));
        // First line of the body text
        float bodyFirstBaseline = subjectY + bodyTop;
        float bodyFirstY = bodyFirstBaseline + bodyBottom;
        // Second line of the body text
        float extraBodySpacing = 4 * mScreenDensity;
        float bodySecondBaseline = bodyFirstY + bodyTop + extraBodySpacing;

        float bodyWidth = getWidth() - (2 * mPaddingSize) - mStarPixelSize - mPaddingSize;
        String[] bodyLines = new String[2];
        if(mBodyTextPaint.measureText(mEmail.getBody()) < bodyWidth){
            bodyLines[0] = mEmail.getBody();
        }else{
            int currentLine = 0;
            String[] bodyWords = mEmail.getBody().split(" ");
            int numberOfWords = bodyWords.length;
            int currentWord = 0;
            String bodyLine = "";
            String checkingLine = bodyWords[currentWord];

            while ((currentWord < numberOfWords - 1) && (currentLine < 2)) {
                if (mBodyTextPaint.measureText(checkingLine) < bodyWidth) {
                    bodyLine = checkingLine;
                    currentWord++;
                    checkingLine = bodyLine + " " + bodyWords[currentWord];
                } else {
                    bodyLines[currentLine] = bodyLine;
                    currentLine++;
                    bodyLine = "";
                    checkingLine = bodyWords[currentWord];
                }
            }
            if(currentLine < 2){
                bodyLines[currentLine] = bodyLine;
            }

            if(currentWord < numberOfWords - 1){
                //ellipsize second sentence
                String secondSentence = bodyLines[1];
                secondSentence = secondSentence.substring(0, secondSentence.length() - 4);
                secondSentence += "...";
                bodyLines[1] = secondSentence;
            }

            if(bodyLines[0] != null){
                canvas.drawText(bodyLines[0], bodyX, bodyFirstBaseline, mBodyTextPaint);
            }

            if(bodyLines[1] != null){
                canvas.drawText(bodyLines[1], bodyX, bodySecondBaseline, mBodyTextPaint);
            }

            // Draw the star
            mStarLeft = getWidth() - mPaddingSize - mStarPixelSize;
            float starHeight = getHeight() - senderY - mDividerSize;
            mStarTop = (starHeight / 2) + senderY - (mStarPixelSize / 2);
            if (mEmail.isImportant()) {
                canvas.drawBitmap(mImportantStar, mStarLeft, mStarTop, mStarPaint);
            } else {
                canvas.drawBitmap(mUnimportantStar, mStarLeft, mStarTop, mStarPaint);
            }
        }
    }

    private int calculateHeight() {
        int layoutPadding = getPaddingTop() + getPaddingBottom();
        Paint.FontMetrics senderFm = mSenderAddressTextPaint.getFontMetrics();
        float senderHeight = getFontHeight(senderFm);
        Paint.FontMetrics subjectFm = mSubjectTextPaint.getFontMetrics();
        float subjectHeight = getFontHeight(subjectFm);
        Paint.FontMetrics bodyFm = mBodyTextPaint.getFontMetrics();
        float bodyHeight = getFontHeight(bodyFm);
        float bodyPadding = BODY_PADDING_SIZE * mScreenDensity;
        float totalHeight = layoutPadding + mPaddingSize + senderHeight
                + subjectHeight + (bodyHeight * 2) + bodyPadding
                + mPaddingSize + mDividerSize;
        return (int) totalHeight;
    }

    private float getFontHeight(Paint.FontMetrics metrics) {
        return (float) (Math.ceil(Math.abs(metrics.top)) +
                Math.ceil(Math.abs(metrics.bottom)));
    }

    private int calculateWidth() {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(getContext().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        return size.x;
    }

    private void setupBitmaps() {
        int bitMapSize = (int) (STAR_SIZE * mScreenDensity);

        Bitmap importantBitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_important);
        mImportantStar = Bitmap
                .createScaledBitmap(importantBitmap, bitMapSize, bitMapSize, false);

        Bitmap unimportantBitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_unimportant);
        mUnimportantStar = Bitmap
                .createScaledBitmap(unimportantBitmap, bitMapSize, bitMapSize, false);

    }

    private void setupPaints() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getResources().getColor(R.color.white));

        mDividerPaint = new Paint();
        mDividerPaint.setColor(getResources().getColor(R.color.divider_color));

        mStarPaint = new Paint();
        int starTint = getResources().getColor(R.color.star_tint);
        ColorFilter colorFilter = new LightingColorFilter(starTint, 1);
        mStarPaint.setColorFilter(colorFilter);

        mSenderAddressTextPaint = new TextPaint();
        mSenderAddressTextPaint.setTextSize(mLargeTextSize);
        mSenderAddressTextPaint.setTextAlign(Paint.Align.LEFT);
        mSenderAddressTextPaint.setColor(getResources().getColor(R.color.black));
        mSenderAddressTextPaint.setAntiAlias(true);

        mSubjectTextPaint = new TextPaint();
        mSubjectTextPaint.setTextSize(mSmallTextSize);
        mSubjectTextPaint.setTextAlign(Paint.Align.LEFT);
        mSubjectTextPaint.setColor(getResources().getColor(R.color.black));
        mSubjectTextPaint.setAntiAlias(true);

        mBodyTextPaint = new TextPaint();
        mBodyTextPaint.setTextSize(mSmallTextSize);
        mBodyTextPaint.setTextAlign(Paint.Align.LEFT);
        mBodyTextPaint.setColor(getResources().getColor(R.color.body_color));
        mBodyTextPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isStarClick(motionEvent)){
                    mEmail.setImportant(!mEmail.isImportant());
                    mDataManager.updateEmail(mEmail);
                    invalidate();
                    return true;
                }
                return true;
        }
        return false;
    }

    private boolean isStarClick(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float starRight = mStarLeft + mStarPixelSize;
        float starBottom = mStarTop + mStarPixelSize;
        boolean isXInStarRange = (x >= mStarLeft) && (x <= starRight);
        boolean isYInStarRange = (y >= mStarTop) && (y <= starBottom);
        return isXInStarRange && isYInStarRange;
    }
}
