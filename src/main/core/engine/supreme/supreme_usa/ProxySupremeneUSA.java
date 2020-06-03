package engine.supreme.supreme_usa;

import ambm.captcha.CaptchaCache;
import ambm.home.TableMessQueue;
import exception.BotException;
import tool.HttpUntil;
import tool.RegexParse;

import java.net.URISyntaxException;

public class ProxySupremeneUSA extends SupremeneUSA {

    @Override
    public void excute(SupremeneUSAGoods goods, SupremeneUSAPersion palacesPersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws Exception {
        Thread.sleep(web.getTryTimes() + 500 + (int) (Math.random() * 1500));
        addCart(goods, palacesPersion, web, httpUntil);
        Thread.sleep(web.getTryTimes() + 1000 + (int) (Math.random() * 2000));
        cartCheckOut(goods, palacesPersion, web, httpUntil);
    }


    @Override
    public void addCart(SupremeneUSAGoods goods, SupremeneUSAPersion palacesPersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.addCart(goods, palacesPersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(), "添加购物车完成");
        if (web.isStartCaptcha())//验证码放到提交表单时候在打马
            CaptchaCache.GetInstance().addCaptchaDemand(web.getId() + "", web.getCaptchaKey(), web.getHost(), web.getWebsite(), web.getCapthchaHost());
    }


    @Override
    public void cartCheckOut(SupremeneUSAGoods goods, SupremeneUSAPersion supremenePersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        super.cartCheckOut(goods, supremenePersion, web, httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(), "提交购物车完成");
        if(RegexParse.ismatching(httpUntil.getResult(),"paid"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"购买成功了..");
        else if(RegexParse.ismatching(httpUntil.getResult(),"failed"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"购买失败,原因未知");
        else if(RegexParse.ismatching(httpUntil.getResult(),"error"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"信息填写有误.导致失败");
        else if(RegexParse.ismatching(httpUntil.getResult(),"Failure Reason\":\"Sold Out"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"失败,商品已经售罄...");
        else if(RegexParse.ismatching(httpUntil.getResult(),"number\":\\[\"is required"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"失败，原因:是否填写了对的信用卡");
        else if(RegexParse.ismatching(httpUntil.getResult(),"\"status\":\"blacklisted\""))
            TableMessQueue.getInstance().addMess(web.getIndex(),"您被拉入supreme黑名单");
        else if(RegexParse.ismatching(httpUntil.getResult(),"\"status\":\"dup\""))
            TableMessQueue.getInstance().addMess(web.getIndex(),"失败，原因重复购买,这种操作容易被拉黑");
    }


}
