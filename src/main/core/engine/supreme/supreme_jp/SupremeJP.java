package engine.supreme.supreme_jp;

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
import java.util.HashMap;

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
public class SupremeJP extends SupperEngine implements APIEngine<SupremeJPGoods, SupremeJPPersion, SupremeJPWEB, HttpUntil> {
    private Logger logger = LoggerFactory.getLogger(SupremeJP.class);

    public static void main(String[] args) throws Exception {
        SupremeJPGoods supremeneGoods=new SupremeJPGoods();
        supremeneGoods.setSize("62325");
        supremeneGoods.setStyle("21954");
        supremeneGoods.setQty("1");


        SupremeJPPersion supremenePersion=new SupremeJPPersion();
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

        SupremeJPWEB web=new SupremeJPWEB();
        web.setBaseURL("https://www.supremenewyork.com");
        web.setAddCartURL("https://www.supremenewyork.com/shop/5703/add.json");
        web.setCartURL("https://www.supremenewyork.com/checkout.json");

       SupremeJP supremene=new SupremeJP();
       supremene.excute(supremeneGoods,supremenePersion,web,new HttpUntil());
    }


    @Override
    public void addCart(SupremeJPGoods goods, SupremeJPPersion palacesPersion, SupremeJPWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder= RequestBuilder.post()
                .setUri(new URI(web.getAddCartURL()))
                .addParameter("size",goods.getSize())
                .addParameter("style",goods.getStyle())
                .addParameter("qty",goods.getQty())
                .setHeader("User-Agent",httpUntil.USERAGENT);
        httpUntil.post(httpUntil,builder);
    }


    @Override
    public void cartCheckOut(SupremeJPGoods goods, SupremeJPPersion supremenePersion, SupremeJPWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder= RequestBuilder.post()
                .setUri(new URI(web.getCartURL()))
                .addParameter("store_credit_id","")
                .addParameter("from_mobile","1")
                .addParameter("cookie-sub",goods.getCookieSub())//---------估计上下文获取的此处先不写
                .addParameter("same_as_billing_address","1")
                .addParameter("order[billing_name]",supremenePersion.getFirst_name())//----作为全名
                .addParameter("order[email]",supremenePersion.getEmail())
                .addParameter("order[tel]",supremenePersion.getPhone())
                .addParameter("order[billing_zip]",supremenePersion.getZip())
                .addParameter("order[billing_state]",supremenePersion.getCountyState().trim())//--------县
                .addParameter("order[billing_city]",supremenePersion.getCity())
                .addParameter("order[billing_address]",supremenePersion.getAddress1())
                .addParameter("store_address","1")
                .addParameter("credit_card[type]",getCartType(supremenePersion.cartType,supremenePersion))
                .addParameter("credit_card[cnb]",supremenePersion.getNumber())
                .addParameter("credit_card[month]",supremenePersion.month)
                .addParameter("credit_card[year]",supremenePersion.year)
                .addParameter("credit_card[vval]",supremenePersion.verification_value)
                .addParameter("order[terms]","0")
                .addParameter("order[terms]","1");
                 builder=getCaptcha(builder,web.isStartCaptcha(),web);//验证码
                 builder.setHeader("User-Agent",httpUntil.USERAGENT);
        httpUntil.post(httpUntil,builder);
        httpUntil.printResultBody(true);
    }

    private String getCartType(String cartType, SupremeJPPersion supremenePersion){
        HashMap<String,String> hashMap=new HashMap(10);
        hashMap.put("Visa","visa");
        hashMap.put("American Express","american_express");
        hashMap.put("Mastercard","master");
        hashMap.put("JCB","jcb");
        hashMap.put("代金引換","cod");
        if(cartType.equals("代金引換"))
        {
            supremenePersion.setNumber("");
            supremenePersion.month="01";
            supremenePersion.year="2019";
            supremenePersion.verification_value="";
        }
        if(hashMap.get(cartType)!=null)
        return  hashMap.get(cartType);
        logger.info("SupremeJP>>付款类型可能出现问题"+cartType);
        return  cartType;
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
    public void shopingMethod(SupremeJPGoods goods, SupremeJPPersion supremenePersion, SupremeJPWEB web, HttpUntil httpUntil) throws URISyntaxException,BotException {

    }

    @Override
    public void paymentMethod(SupremeJPGoods goods, SupremeJPPersion supremenePersion, SupremeJPWEB web, HttpUntil httpUntil) throws URISyntaxException,BotException {

    }


    @Override
    public void excute(SupremeJPGoods goods, SupremeJPPersion palacesPersion, SupremeJPWEB web, HttpUntil httpUntil) throws Exception {
        addCart(goods,palacesPersion,web,httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"添加购物车完成");
        cartCheckOut(goods,palacesPersion,web,httpUntil);
        TableMessQueue.getInstance().addMess(web.getIndex(),"提交购物车完成");
        TableMessQueue.getInstance().addMess(web.getIndex(),"购买完成");
    }

    @Override
    public void pay(SupremeJPGoods goods, SupremeJPPersion supremenePersion, SupremeJPWEB web, HttpUntil httpUntil) throws Exception {

    }


}
