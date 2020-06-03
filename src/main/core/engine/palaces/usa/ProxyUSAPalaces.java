package engine.palaces.usa;


import ambm.captcha.CaptchaCache;
import ambm.home.TableMessQueue;
import exception.BotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import tool.RegexParse;

import java.net.URISyntaxException;

public class ProxyUSAPalaces extends PalacesUSA {

    private Logger logger = LoggerFactory.getLogger(ProxyUSAPalaces.class);

    @Override
    public void excute(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws Exception {
       try{
           addCart(goods,palacesPersion,web,httpUntil);
           //Thread.sleep(web.getTryTimes()+10+(int)(Math.random()*1000));
           cartCheckOut(goods,palacesPersion,web,httpUntil);
          // Thread.sleep(web.getTryTimes()+10+(int)(Math.random()*1000));
           shopingMethod(goods,palacesPersion,web,httpUntil);
           //Thread.sleep(web.getTryTimes()+500+(int)(Math.random()*1000));
           paymentMethod(goods,palacesPersion,web,httpUntil);
           //Thread.sleep(web.getTryTimes()+10+(int)(Math.random()*1000));
           pay(goods,palacesPersion,web,httpUntil);
       }catch (Exception e){
           logger.error("抢购失败",e);
           throw  e;
       }
    }


    @Override
    public void addCart(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.addCart(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"添加购物车完成");
    }

    @Override
    public void cartCheckOut(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.cartCheckOut(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"提交购物车完成");
        if(web.isStartCaptcha())//验证码放到提交表单时候在打马
            CaptchaCache.GetInstance().addCaptchaDemand(web.getId()+"",web.getCaptchaKey(),web.getHost(),web.getWebsite(),web.getCapthchaHost());
    }

    @Override
    public void shopingMethod(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.shopingMethod(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"填写购物人信息完成");
    }


    @Override
    public void paymentMethod(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.paymentMethod(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"提交邮费完成");
    }

    @Override
    public void pay(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws Exception {
        super.pay(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"购买完成");
        if(RegexParse.ismatching(httpUntil.getResult(),"Your payment details couldn’t be verified."))
            TableMessQueue.getInstance().addMess(web.getIndex(),"购买失败，付款信息错误");
    }


}
