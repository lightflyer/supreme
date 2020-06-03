package ambm.home;

import ambm.ambm.SpringContextUtil;
import ambm.ambm.TempManage;
import ambm.captcha.CaptchaView;
import ambm.setting.SettingView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@FXMLController
public class HomeController implements Initializable {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController() {
    }

    @FXML
    TableView taskTable;

    @Autowired
    HomeService homeService;

    @FXML
    TableColumn id;

    @FXML
    TableColumn size;

    @FXML
    TableColumn keywords;

    @FXML
    TableColumn website;

    @FXML
    TableColumn proxy;

    @FXML
    TableColumn startCaptcha;

    @FXML
    TableColumn firstName;

    @FXML
    TableColumn lashName;

    @FXML
    TableColumn state;

    @FXML
    TableColumn state1;

    @FXML
    Button addTask;

    @FXML
    Text pubmess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /***
         * 启动的时候，根据账号刷新列表；
         */
        fluchTable();
        homeService.flushTableState(taskTable);
        fushPubMess();
    }

    public void fluchTable() {
        homeService.flushTable(TempManage.temp.get("username"), TempManage.temp.get("password"), taskTable, id, firstName, size, keywords, website, state, state1);
    }

    /*****
     * 显示隐藏setting
     */
    private Stage stageSetting;

    public void setting() {
        if (stageSetting == null)
            initWindowSetting();
        stageSetting.show();
    }

    void initWindowSetting() {
        stageSetting = new Stage();
        Scene scene = new Scene(SpringContextUtil.getBean(SettingView.class).getView());
        stageSetting.setScene(scene);
        stageSetting.setOnCloseRequest(event -> {
            stageSetting.hide();
        });
    }

    /*****
     * 显示隐藏Captcha
     */
    private Stage stageCaptcha;

    public void captchaAction() {
        if (stageCaptcha == null)
            initWindowCaptcha();
        stageCaptcha.show();
    }

    void initWindowCaptcha() {
        stageCaptcha = new Stage();
        stageCaptcha.setAlwaysOnTop(true);
        Scene scene = new Scene(SpringContextUtil.getBean(CaptchaView.class).getView());
        stageCaptcha.setScene(scene);
        stageCaptcha.setOnCloseRequest(event -> {
            stageCaptcha.hide();
        });
    }

    /*****
     * 添加addTask
     */

    public void addTask() {
        logger.info("home ~ addTask...");
        homeService.addTask();
    }



    /****
     * 启动所有任务
     */
    public void startAll(){
        homeService.startAllTasks(taskTable);
    }

    /****
     * 停止所有任务
     */
    public void stopAll(){
        homeService.stopAllTask();
    }


    void fushPubMess(){
        ExecutorService executors= Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String mess=PublicMessCache.getInstance().flushMess();
                    if(mess!=null){
                        Platform.runLater(new Runnable() {//更新ui 要这么写；不能在ui线程之外更新ui
                            @Override
                            public void run() {
                                logger.info("****************************"+mess);
                                pubmess.setText(mess);
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

}
