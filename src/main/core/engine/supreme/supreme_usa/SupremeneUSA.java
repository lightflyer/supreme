package engine.supreme.supreme_usa;

import ambm.captcha.CaptchaCache;
import ambm.home.TableMessQueue;
import base.BaseWEB;
import engine.APIEngine;
import engine.supreme.SupperEngine;
import exception.BotException;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import java.net.URI;
import java.net.URISyntaxException;

/******
 * 猜测差不多；
 * 问题；
 * 1 验证码
 * 2 ip
 * 3 价格限制
 * 4 找到对应的产品；
 * 5 表单是否每个国家不一样
 * 6 要不要提前输入一下试试，免得低级错误，也叫表单校验；
 * 7 找货这一步根据关键方面要靠谱。
 */
public class SupremeneUSA extends SupperEngine implements APIEngine<SupremeneUSAGoods, SupremeneUSAPersion, SupremeneUSAWEB, HttpUntil> {
    private Logger logger = LoggerFactory.getLogger(SupremeneUSA.class);

    public static void main(String[] args) throws Exception {
        SupremeneUSAGoods supremeneGoods=new SupremeneUSAGoods();
        supremeneGoods.setSize("65035");
        supremeneGoods.setStyle("23057");
        supremeneGoods.setQty("1");


        SupremeneUSAPersion supremenePersion=new SupremeneUSAPersion();
        supremenePersion.setPhone("18410945297");
        supremenePersion.setEmail("jihongyu19870122@163.com");
        supremenePersion.setFirst_name("yu");
        supremenePersion.setLast_name("zhang");
        supremenePersion.setZip("M2 5BQ");
        supremenePersion.setCountry("United Kingdom");
        supremenePersion.setCity("beijing 3");
        supremenePersion.setAddress1("beijing 1");
        supremenePersion.setAddress2("beijing 2");
        supremenePersion.setCard("1","Bogus Gateway","8","2023","068");

        SupremeneUSAWEB web=new SupremeneUSAWEB();
        web.setBaseURL("https://www.supremenewyork.com");
        web.setAddCartURL("https://www.supremenewyork.com/shop/5703/add.json");
        web.setCartURL("https://www.supremenewyork.com/checkout.json");

       SupremeneUSA supremene=new SupremeneUSA();
       supremene.excute(supremeneGoods,supremenePersion,web,new HttpUntil());
    }


    @Override
    public void addCart(SupremeneUSAGoods goods, SupremeneUSAPersion palacesPersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder= RequestBuilder.post()
                .setUri(new URI(web.getAddCartURL()))
                .addParameter("s",goods.getSize())
                .addParameter("st",goods.getStyle())
                .addParameter("qty",goods.getQty())
                .setHeader("User-Agent",httpUntil.USERAGENT);
        httpUntil.post(httpUntil,builder);
    }


    @Override
    public void cartCheckOut(SupremeneUSAGoods goods, SupremeneUSAPersion supremenePersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder= RequestBuilder.post()
                .setUri(new URI(web.getCartURL()))
                .addParameter("store_credit_id","")
                .addParameter("from_mobile","1")
                .addParameter("cookie-sub",goods.getCookieSub())//---------估计上下文获取的此处先不写
                .addParameter("same_as_billing_address","1")
                .addParameter("order[billing_name]","")
                .addParameter("order[bn]",supremenePersion.getFirst_name())//----作为全名)
                .addParameter("order[email]",supremenePersion.getEmail())
                .addParameter("order[tel]",supremenePersion.getPhone())
                .addParameter("order[billing_address]",supremenePersion.getAddress1())
                .addParameter("order[billing_address_2]",supremenePersion.getAddress2())
                .addParameter("order[billing_zip]",supremenePersion.getZip())
                .addParameter("order[billing_city]",supremenePersion.getCity())
                .addParameter("order[billing_state]",supremenePersion.getCountyState().trim())//--------县
                .addParameter("order[billing_country]",supremenePersion.getCountry())
                .addParameter("store_address","1")
                .addParameter("credit_card[cnb]",supremenePersion.getNumber())
                .addParameter("credit_card[month]",supremenePersion.month)
                .addParameter("credit_card[year]",supremenePersion.year)
                .addParameter("credit_card[rsusr]",supremenePersion.verification_value)
                .addParameter("order[terms]","0")
                .addParameter("order[terms]","1");
                 builder=getCaptcha(builder,web.isStartCaptcha(),web);//验证码
                 builder.setHeader("User-Agent",httpUntil.USERAGENT);
       httpUntil.post(httpUntil,builder);
    }


    @Override
    public void shopingMethod(SupremeneUSAGoods goods, SupremeneUSAPersion supremenePersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException,BotException {

    }

    @Override
    public void paymentMethod(SupremeneUSAGoods goods, SupremeneUSAPersion supremenePersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws URISyntaxException,BotException {

    }


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
            requestBuilder.addParameter("g-recaptcha-response",""+result);
        }
        return requestBuilder;
    }


    @Override
    public void excute(SupremeneUSAGoods goods, SupremeneUSAPersion palacesPersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws Exception {
        Thread.sleep(web.getTryTimes()+500+(int)(Math.random()*2000));
        addCart(goods,palacesPersion,web,httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"添加购物车完成");
        Thread.sleep(web.getTryTimes()+1000+(int)(Math.random()*2000));
        if(web.isStartCaptcha())//验证码放到提交表单时候在打马
            CaptchaCache.GetInstance().addCaptchaDemand(web.getId()+"",web.getCaptchaKey(),web.getHost(),web.getWebsite(),web.getCapthchaHost());
        cartCheckOut(goods,palacesPersion,web,httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"提交购物车完成");
        TableMessQueue.getInstance().addMess(web.getIndex(),"购买完成");

    }

    @Override
    public void pay(SupremeneUSAGoods goods, SupremeneUSAPersion supremenePersion, SupremeneUSAWEB web, HttpUntil httpUntil) throws Exception {

    }


}
