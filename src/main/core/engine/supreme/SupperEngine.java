package engine.supreme;

import ambm.captcha.CaptchaCache;
import ambm.home.TableMessQueue;
import base.BaseWEB;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SupperEngine {

    private Logger logger = LoggerFactory.getLogger(SupperEngine.class);
    /*****
     * 获取验证码流程，如果需要取验证码，就跟缓存去要验证码，如果要不到就一直等待；如果要得到就返回；
     * @param requestBuilder
     * @param isCaptcha
     * @param web
     * @return
     */
    public RequestBuilder getCaptcha(RequestBuilder requestBuilder, boolean isCaptcha, BaseWEB web){
        if(isCaptcha){
            String result=null;
            while ((result= CaptchaCache.GetInstance().getCaptchaResult(web.getId()+""))==null){
                try {
                    Thread.sleep(600);
                    logger.info("excuter等待验证码");
                    TableMessQueue.getInstance().addMess(web.getIndex(),"等待验证码...");
                }catch (Exception e){
                    logger.error("excuter获取验证码失败",e);
                }
            }
            logger.info("excuter拿到验证码"+result);
            TableMessQueue.getInstance().addMess(web.getIndex(),"拿到验证码...");
            requestBuilder.addParameter("g-recaptcha-response",result);
        }
        return requestBuilder;
    }


}
