package cbedoy.cblibrary.widgets;

import android.text.Editable;
import android.text.TextWatcher;
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
public class TextWatcherAdapter implements TextWatcher
{

    public interface ITextWatcherListener
    {
        void onTextChanged(EditText view, String text);
    }

    private final EditText view;
    private final ITextWatcherListener listener;

    public TextWatcherAdapter(EditText editText, ITextWatcherListener listener)
    {
        this.view = editText;
        this.listener = listener;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        listener.onTextChanged(view, s.toString().toLowerCase());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        listener.onTextChanged(view, s.toString().toLowerCase());
    }
}