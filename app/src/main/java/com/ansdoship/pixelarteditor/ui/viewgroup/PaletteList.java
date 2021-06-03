package com.ansdoship.pixelarteditor.ui.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ansdoship.pixelarteditor.R;
import com.ansdoship.pixelarteditor.editor.palette.Palette;
import com.ansdoship.pixelarteditor.ui.view.PaletteView;
import com.ansdoship.pixelarteditor.util.MathUtils;

public class PaletteList extends LinearLayout implements View.OnClickListener {

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        try {
            throw new IllegalAccessException("Cannot add child view");
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        try {
            throw new IllegalAccessException("Cannot add child view");
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int mIndex;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnDoubleTapListener mOnDoubleTapListener;
    private final Context mContext;
    private Palette mPalette;
    private final int mPaletteWidth;
    private final int mPaletteHeight;

    private int paletteBackgroundColor1;
    private int paletteBackgroundColor2;

    @Override
    public void onClick(View view) {
        checkIndex(this.indexOfChild(view));
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        mOnDoubleTapListener = onDoubleTapListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(PaletteList paletteList, int checkedIndex);
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(PaletteList paletteList, int checkedIndex);
    }

    private void setIndex(int index) {
        mIndex = MathUtils.clamp(index, 0, getSize());
        if (mPalette != null) {
            mPalette.setIndex(index);
        }
    }

    public void checkIndex(int index) {
        int preIndex = mIndex;
        if (preIndex != index) {
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, index);
            }
        }
        else {
            if (mOnDoubleTapListener != null) {
                mOnDoubleTapListener.onDoubleTap(this, index);
            }
        }
        setIndex(index);
        for (int i = 0; i < getChildCount(); i ++) {
            ((PaletteView) getChildAt(i)).setChecked(false);
        }
        if (index >= 0 && index < getChildCount()) {
            ((PaletteView) getChildAt(index)).setChecked(true);
        }
    }

    public int getCheckedIndex() {
        return mIndex;
    }

    public void setPaletteBackgroundColor1(int paletteBackgroundColor1) {
        this.paletteBackgroundColor1 = paletteBackgroundColor1;
        for (int i = 0; i < getChildCount(); i ++) {
            ((PaletteView) getChildAt(i)).setPaletteBackgroundColor1(paletteBackgroundColor1);
        }
    }

    public void setPaletteBackgroundColor2(int paletteBackgroundColor2) {
        this.paletteBackgroundColor2 = paletteBackgroundColor2;
        for (int i = 0; i < getChildCount(); i ++) {
            ((PaletteView) getChildAt(i)).setPaletteBackgroundColor1(paletteBackgroundColor2);
        }
    }

    public void setPaletteBackgroundColors(int paletteBackgroundColor1, int paletteBackgroundColor2) {
        this.paletteBackgroundColor1 = paletteBackgroundColor1;
        this.paletteBackgroundColor2 = paletteBackgroundColor2;
        for (int i = 0; i < getChildCount(); i ++) {
            ((PaletteView) getChildAt(i)).setPaletteBackgroundColors(paletteBackgroundColor1, paletteBackgroundColor2);
        }
    }

    public PaletteList(@NonNull Context context) {
        this(context, null);
    }

    public PaletteList(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaletteList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PaletteList, defStyleAttr, 0);
        mPaletteWidth = typedArray.getDimensionPixelSize(R.styleable.PaletteList_paletteWidth, 0);
        mPaletteHeight = typedArray.getDimensionPixelSize(R.styleable.PaletteList_paletteHeight, 0);
        int size = typedArray.getInt(R.styleable.PaletteList_size, 1);
        for (int i = 0; i < size; i ++) {
            PaletteView paletteView = new PaletteView(context);
            paletteView.setLayoutParams(new LinearLayout.LayoutParams(mPaletteWidth, mPaletteHeight));
            paletteView.setOnClickListener(this);
            paletteBackgroundColor1 = typedArray.getInt(R.styleable.PaletteList_paletteBackgroundColor1, Color.LTGRAY);
            paletteBackgroundColor2 = typedArray.getInt(R.styleable.PaletteList_paletteBackgroundColor2, Color.GRAY);
            paletteView.setPaletteBackgroundColors(paletteBackgroundColor1, paletteBackgroundColor2);
            paletteView.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            addView(paletteView);
        }
        checkIndex(typedArray.getInt(R.styleable.PaletteList_checkedIndex, 0));
        typedArray.recycle();
    }

    public void setSize (int size) {
        setSize(size, 0);
    }

    public void setSize (int size, int index) {
        if (size < 1) {
            return;
        }
        mPalette = null;
        removeAllViews();
        for (int i = 0; i < size; i ++) {
            PaletteView paletteView = new PaletteView(mContext);
            paletteView.setLayoutParams(new LinearLayout.LayoutParams(mPaletteWidth, mPaletteHeight));
            paletteView.setOnClickListener(this);
            paletteView.setPaletteBackgroundColors(paletteBackgroundColor1, paletteBackgroundColor2);
            paletteView.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            addView(paletteView);
        }
        checkIndex(index);
    }

    public int getSize () {
        return getChildCount();
    }

    public void setPalette (@NonNull Palette palette) {
        setPalette(palette, palette.getIndex());
    }

    public void setPalette (@NonNull Palette palette, int index) {
        mPalette = palette;
        removeAllViews();
        for (int i = 0; i < palette.size(); i++) {
            PaletteView paletteView = new PaletteView(mContext);
            paletteView.setLayoutParams(new LayoutParams(mPaletteWidth, mPaletteHeight));
            paletteView.setPaletteColor(palette.getColor(i));
            paletteView.setOnClickListener(this);
            paletteView.setPaletteBackgroundColors(paletteBackgroundColor1, paletteBackgroundColor2);
            paletteView.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            addView(paletteView);
        }
        setIndex(index);
        ((PaletteView) getChildAt(index)).setChecked(true);
    }

    public void setPaletteColor (int index, int color) {
        ((PaletteView) getChildAt(index)).setPaletteColor(color);
        if (mPalette != null) {
            mPalette.setColor(index, color);
        }
    }

    public int getPaletteColor (int index) {
        return ((PaletteView) getChildAt(index)).getPaletteColor();
    }

    public void setCheckedPaletteColor (int color) {
        setPaletteColor(getCheckedIndex(), color);
    }

    public int getCheckedPaletteColor () {
        return getPaletteColor(getCheckedIndex());
    }

}