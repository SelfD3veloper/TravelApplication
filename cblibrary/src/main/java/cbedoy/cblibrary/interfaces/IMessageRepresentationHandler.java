package cbedoy.cblibrary.interfaces;

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

public interface IMessageRepresentationHandler {

    public void showLoading();

    public void hideLoading();

    public void hideMessage();

    public void showCode(NOTIFICATION_CODE code);

    public void showCodeWithCallback(NOTIFICATION_CODE code, IMessageRepresentationCallback callback);

    public enum NOTIFICATION_CODE {
        B_ERROR,
        B_SUCCESS,
        B_NEWWORK
    }

    public interface IMessageRepresentationCallback {
        public void run();
    }
}
