package ambm;

import ambm.ambm.InitTest;
import ambm.ambm.License;
import ambm.ambm.LittleProxy;
import ambm.login.LoginView;
import ambm.style.CustomSplash;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Application extends AbstractJavaFxApplicationSupport {

    private static Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        try {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
            License.init();
            LittleProxy.initProxy();
            launchApp(Application.class, LoginView.class, new CustomSplash(), args);
            getStage().close();
        } catch (Throwable e) {
            logger.error("系统错误....", e);
            System.exit(0);
        }
    }


}
