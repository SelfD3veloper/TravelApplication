package cbedoy.cblibrary.services;

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
public class InjectionManager
{
    private int env = 0;
    private int rest_port;
    private String rest_url;
    private static InjectionManager _instance;

    public static InjectionManager getInstance() {
        if (_instance == null)
            _instance = new InjectionManager();

        return _instance;
    }

    private InjectionManager() {
        switch (this.env) {
            case 0: //dev
                this.rest_port = 80;
                this.rest_url = "http://$your_url";
                break;
            case 1: //pre
                this.rest_port = 80;
                this.rest_url = "http://$your_url";
                break;
            case 2: //pro
                this.rest_port = 80;
                this.rest_url = "http://$your_url";
                break;
            default:
                this.rest_port = 8080;
                this.rest_url = "http://$your_url";
                break;
        }
    }

    public static final String MEDIA_URL = "http://$your_url:8080/media/";

    public boolean isProduction() {
        return env == 2;
    }

    public boolean isPreProduction() {
        return env == 1;
    }


    public void initApp(){

    }

    public boolean enableFlurry() {
        return false;
    }
}
