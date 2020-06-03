package ambm.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CaptchaCache {
    private Logger logger = LoggerFactory.getLogger(CaptchaCache.class);
    private CaptchaCache() { }
    private volatile static CaptchaCache captchaCache=null;
    public static synchronized CaptchaCache GetInstance(){
            if(captchaCache==null)
                captchaCache=new CaptchaCache();
            return captchaCache;
    }

    private volatile  List<CaptchaItem> captchaItems=new ArrayList<>();
    private volatile List<CaptchaItem> bill=new ArrayList<>();//重要，每次页面要验证码的时候，必须等上一个验证码归还了才能给，不然他会覆盖掉，而不是一个一个的打码，而是后面的页面加载会覆盖掉前面的；
    private volatile HashMap<String,String> results=new HashMap<String,String>();
    public static class CaptchaItem{

        public String taskId;
        public String captchaKey;
        public String captchaResult;
        public  String host;
        public String website;
        public String capthchaHost;

        private CaptchaItem(String taskId, String captchaKey, String captchaResult, String host, String website,String capthchaHost) {
            this.taskId = taskId;
            this.captchaKey = captchaKey;
            this.captchaResult = captchaResult;
            this.host = host;
            this.website = website;
            this.capthchaHost=capthchaHost;
        }
    }

    /****
     * 启动时候添加需求
     */
     public synchronized void   addCaptchaDemand(String taskId, String captchaKey, String host, String website,String capthchaHost){
         captchaItems.add(new CaptchaItem( taskId,  captchaKey,  null,  host,  website,capthchaHost));
     }

    /****
     * 识别时候需要要需求
     * @return
     */
    public synchronized CaptchaItem getCaptchaDemand(){
        if(captchaItems.size()>0&&bill.size()==0){
            CaptchaItem captchaItem=captchaItems.remove(0);
            bill.add(captchaItem);
            return  captchaItem;
        }
        return null;
    }

    /****
     * 识别完毕归还
     * @return
     */
    public synchronized void backToCaptchaQueue(String taskId,String captchaKey,String captchaResult){
        results.put(taskId,captchaResult);
    }


    /****
     * 执行器要结果
     * @param taskId
     * @return
     */
    public synchronized String getCaptchaResult(String taskId){
        String result=results.get(taskId);
        //logger.info("bot询问是否打码完毕>taskId:"+taskId+" result: "+result);
        if(result!=null){
            bill.clear();
            return results.remove(taskId);
        }
        else
        {
            return null;
        }
    }

}

