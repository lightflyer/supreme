package ambm.ambm;

import ambm.captcha.CaptchaCache;
import ambm.home.PublicMessCache;
import ambm.login.LoginDao;
import com.alibaba.fastjson.JSONObject;
import exception.BotException;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tool.HttpUntil;
import tool.RegexParse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class InitTest {
    private Logger logger = LoggerFactory.getLogger(InitTest.class);
    HttpUntil httpUntil=new HttpUntil();

    @Autowired
    private LoginDao loginDao;

    public InitTest() {
        httpUntil.setTimeOut(5000);
    }

    public  void test(){
        ExecutorService executors= Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            int sum=1;
            @Override
            public void run() {
                while(true){
                   try{
                       mess();
                       sleep(4*1000);
                       PublicMessCache.getInstance().addMess(testSupreme());
                       sleep(sum*2*1000);
                       PublicMessCache.getInstance().addMess(testGoogle());
                       sleep(sum*2*1000);
                       if(sum<600)
                       sum+=10;
                   }catch (Throwable e){
                        logger.info("这个地方错没什么大事儿");
                        sleep(1000*30);
                   }
                }
            }
        });
        executors.shutdown();
    }

    private  void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.error("sleep");
        }
    }

    private String testGoogle(){
        try {
            if(httpUntil.get("https://www.google.com").getStatusLine().equals("200"))
            {
                return "测试连接 google 成功";
            }
        } catch (Exception e) {
            logger.error("testGoogle");
        }
        return "测试连接 google 失败，这可能影响您打码;请检测代理设置是否正确...";
    }

    private String testSupreme(){
        try {
            httpUntil.get("https://www.supremenewyork.com/mobile");
            if(httpUntil.getStatusLine().equals("403"))
            {
                return "测试连接 supremenewyork 失败,您的ip可能已经被封锁;";
            }else if(RegexParse.ismatching(httpUntil.getResult(),"利用規約に同意致")){
                return "测试连接 supremenewyork 成功,您的ip连接的是 日本;";
            }
        } catch (Exception e) {
            logger.error("testSupreme");
        }
        return "测试连接 supremenewyork 成功";
    }


    private void mess(){
        if(TempManage.temp!=null&&TempManage.temp.get("username")!=null)
        {
            JSONObject jsonObject = JSONObject.parseObject(loginDao.login(TempManage.temp.get("username"),TempManage.temp.get("password")));
            if(jsonObject.containsKey("mess")&&jsonObject.getString("mess").equals("exit")){
                PublicMessCache.getInstance().addMess("您的账号可能已经过期，或者被停用，请联系果刀;公众号:果刀 ,程序将在10秒钟后自动销毁...");

                sleep(10*1000);
                System.exit(0);
            }
            if(jsonObject.containsKey("mess"))
                PublicMessCache.getInstance().addMess(jsonObject.getString("mess"));
        }
    }

}
