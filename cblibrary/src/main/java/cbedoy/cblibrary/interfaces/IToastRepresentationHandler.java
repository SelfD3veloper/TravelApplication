package cbedoy.cblibrary.interfaces;

/**
 * Created by Carlos Bedoy on 1/5/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public interface IToastRepresentationHandler
{
    public void showToastWithMessage(String message);
    public void showToastWithMessageAndCallback(String message, IToastRepresentationCallback callback);

    public abstract interface IToastRepresentationCallback
    {
        public void run();
    }
}
