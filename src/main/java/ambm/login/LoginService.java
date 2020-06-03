package ambm.login;

import ambm.Application;
import ambm.ambm.InitTest;
import ambm.ambm.SpringContextUtil;
import ambm.ambm.TempManage;
import ambm.home.HomeView;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginDao loginDao;

    @Autowired
    InitTest initTest;

    void login(Label mess, String username, String password) {
        TempManage.temp.put("username",username);
        TempManage.temp.put("password",password);
        JSONObject jsonObject = JSONObject.parseObject(loginDao.login(username, password));
        mess.setText(jsonObject.getString("result"));
        if(jsonObject.getString("state").equals("ok"))
        {
            initTest.test();
            HomeView homeView = SpringContextUtil.getBean(HomeView.class);
            Scene scene = new Scene(homeView.getView());

            Stage stage = new Stage();
            stage.setOnCloseRequest(event -> {
                logger.info("检测到主页面退出...直接关闭掉进程");
                System.exit(0);
            });
            stage.setScene(scene);
            stage.show();
            Application.getStage().close();
        }
    }
}
