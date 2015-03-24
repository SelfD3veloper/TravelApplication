package cbedoy.cblibrary.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Carlos Bedoy on 28/12/2014.
 *
 * Mobile App Developer
 * CBLibrary
 *
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class EditTextWithClose extends EditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcherAdapter.ITextWatcherListener {

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable drawable;
    private Listener listener;

    public EditTextWithClose(Context context) {
        super(context);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener touchListener) {
        this.onTouchListener = touchListener;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener focusChangeListener) {
        this.onFocusChangeListener = focusChangeListener;
    }

    private OnTouchListener onTouchListener;
    private OnFocusChangeListener onFocusChangeListener;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - drawable.getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(isNotEmpty(text));
        }
    }


    private boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    private boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private void init() {
        drawable = getCompoundDrawables()[2];
        if (drawable == null) {
            drawable = getResources()
                    .getDrawable(android.R.drawable.presence_offline);
        }
        int h = (int) (drawable.getIntrinsicHeight() * 1);
        int w = (int) (drawable.getIntrinsicHeight() * 1);
        drawable.setBounds(0, 0, h, w);
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? drawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }


}