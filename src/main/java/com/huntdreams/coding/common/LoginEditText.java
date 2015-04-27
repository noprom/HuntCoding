package com.huntdreams.coding.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.huntdreams.coding.R;
import com.huntdreams.coding.common.enter.SimpleTextWatcher;

/**
 * 自定义用户输入框
 *
 * @author noprom (https://github.com/noprom)
 * @version 1.0
 * Created by noprom on 2015/4/26.
 */
public class LoginEditText extends EditText{

    Drawable mDrawable;

    public LoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = getResources().getDrawable(R.drawable.delete_edit_login);
        mDrawable.setBounds(0,0,mDrawable.getIntrinsicWidth(),mDrawable.getIntrinsicHeight());

        addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                displayDelete(s.length() > 0);
            }
        });
    }

    private void displayDelete(boolean show){
        if (show) {
            setDrawableRight(mDrawable);
        } else {
            setDrawableRight(null);
        }
    }

    private void setDrawableRight(Drawable drawable){
        setCompoundDrawables(null,null,drawable,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(getCompoundDrawables()[2] != null){
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }
}
