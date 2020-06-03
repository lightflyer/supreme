package ambm.captcha;

import ambm.ambm.TempManage;
import ambm.setting.SettingDao;
import ambm.setting.SettingTable;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@FXMLController
public class CaptchaController  implements Initializable  {
    /*******
     * 验证码 一定要用本国家的ip，就是vpn的ip要用对应国家的；
     */
    private Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    Browser browser=new Browser();

    @Autowired
    private SettingDao settingDao;

    @FXML
    BrowserView webView=new BrowserView(browser) ;

    @FXML
    private Button loadGoogle;


    public CaptchaController() {
        browser.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");

    }

    public void setProxy(String ip,String port,String proxyUsername,String proxyPassword){
        logger.info("set proxy:https="+ip+":"+port);
        BrowserContext browserContext = webView.getBrowser().getContext();
        ProxySerivce proxyService = browserContext.getProxyService();
        String proxyRules="";
        if(port!=null&&port.equals("0000"))
             proxyRules="http=127.0.0.1:9988;";//https=127.0.0.1:9988
        else
            proxyRules="http=127.0.0.1:9988;https="+ip+":"+port+";";//https=127.0.0.1:9988
        proxyService.setProxyConfig(new CustomProxyConfig(proxyRules));
    }

    public void loadProxy(){
        SettingTable settingTable= settingDao.proxyByUsernameAndPassword(TempManage.temp.get("username"),TempManage.temp.get("password"));
        if(settingTable!=null&&settingTable.isEnabled()){
            logger.info("设置全局代理为:"+settingTable.getIp()+":"+settingTable.getPort());
            BrowserContext browserContext = webView.getBrowser().getContext();
            ProxySerivce proxyService = browserContext.getProxyService();
            String proxyRules="";
            String port=settingTable.getPort();
             if(port!=null&&port.equals("0000"))
                proxyRules="http=127.0.0.1:9988;";//https=127.0.0.1:9988
            else
                proxyRules="http=127.0.0.1:9988;https="+settingTable.getIp()+":"+port+";";//https=127.0.0.1:9988
            proxyService.setProxyConfig(new CustomProxyConfig(proxyRules));
        }
        webView.getBrowser().loadHTML("<html><H5>请点击上方加载google视频按钮</H5></html>");
    }

    public void   init(){
        System.out.println(">>>>>>>>>>>>>>>homeController...");
//        if (Environment.isMac()) {
//            BrowserCore.initialize();
//        }
    }

    @FXML
    Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
       loadProxy();
       loadCaptcha();
    }

    private void  loadCaptcha(){
        ExecutorService executors= Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    CaptchaCache.CaptchaItem captchaItem=CaptchaCache.GetInstance().getCaptchaDemand();
                    if(captchaItem!=null){
                        Platform.runLater(new Runnable() {//更新ui 要这么写；不能在ui线程之外更新ui
                            @Override
                            public void run() {
                                logger.info("获取到了验证码需求"+captchaItem.taskId+"capthchaHost"+captchaItem.capthchaHost );
                                logger.info("加载的url为:http://"+ captchaItem.capthchaHost +"?taskId="+captchaItem.taskId+"&captchaKey="+captchaItem.captchaKey);
                                webView.getBrowser().loadURL(captchaItem.capthchaHost +"?taskId="+captchaItem.taskId+"&captchaKey="+captchaItem.captchaKey);
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("验证码窗口错误",e);
                    }
                }
            }
        });
        executors.shutdown();
    }

    public void googleAction(){
        logger.info("加载google视频");
        webView.getBrowser().loadURL("https://www.youtube.com");
        logger.info("打开youtube视频....");
    }


}
