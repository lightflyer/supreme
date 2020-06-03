package ambm.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TController {

    private Logger logger = LoggerFactory.getLogger(TController.class);

    /****
     * 这个地方我需要接受两个参数一个是taskId,一个是captchaKey;
     * @return
     */
    @RequestMapping("/show")
   public String show(ModelMap map, @RequestParam("taskId")String taskId, @RequestParam("captchaKey")String captchaKey){
        logger.info("TController>>已经收到http请求;即将转到验证码页面;taskId: "+taskId+"captchaKey"+captchaKey);
        map.put("taskId",taskId);
        map.put("captchaKey",captchaKey);
       return "index";
    }

    @RequestMapping("/recaptcha")
    @ResponseBody
    public String recaptcha(@RequestParam("response")String response,@RequestParam("taskId")String taskId,@RequestParam("captchaKey")String captchaKey){
        logger.info("TController>>页面已经返回验证码 taskId: "+taskId+" captchaKey: "+captchaKey+"response: "+response);
        CaptchaCache.GetInstance().backToCaptchaQueue(taskId,captchaKey,response);
        return  "ok";
    }

}
