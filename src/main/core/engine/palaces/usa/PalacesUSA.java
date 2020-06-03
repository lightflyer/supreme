package engine.palaces.usa;

import ambm.captcha.CaptchaCache;
import ambm.home.TableMessQueue;
import engine.APIEngine;
import engine.palaces.CheckOutId;
import exception.BotException;
import org.apache.http.Header;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import tool.RegexParse;
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
public class PalacesUSA extends CheckOutId implements APIEngine<PalacesUSAGoods, PalacesUSAPersion, PalacesUSAWEB, HttpUntil> {
    private Logger logger = LoggerFactory.getLogger(PalacesUSA.class);


    private void getNote(PalacesUSAWEB web, HttpUntil httpUntil) throws BotException {
        httpUntil = httpUntil.get(web.getCartURL());
        httpUntil.getTemp().put("note", RegexParse.baseParse(httpUntil.getResult(), "id=\"note\"\\s*?value=\"(\\S*?)\"", 1));
        logger.info("解析到note:" + httpUntil.getTemp().get("note"));
    }

    private void contactInformation(HttpUntil httpUntil, PalacesUSAWEB web) throws BotException {
        if(httpUntil.getTemp().get("checkOutURL")!=null&&RegexParse.ismatching(httpUntil.getTemp().get("checkOutURL"),"stock_problems"))
            TableMessQueue.getInstance().addMess(web.getIndex(),"已经售罄...");
        httpUntil = httpUntil.get(httpUntil.getTemp().get("checkOutURL"));
        httpUntil.getTemp().put("authenticity_token", RegexParse.baseParse(httpUntil.getResult(), "authenticity_token\"\\s*?value=\"([\\S]*?)\"", 1));

        httpUntil.getTemp().put("checkout[shipping_rate][id]", RegexParse.baseParse(httpUntil.getResult(), "value=\"(shopify[\\S]*?)\"\\s*?name=\"checkout\\[shipping_rate\\]\\[id\\]\"", 1));

        httpUntil.getTemp().put("price", RegexParse.baseParse(httpUntil.getResult(), "data-checkout-total-price-cents=\"([\\S]*?)\"", 1));
        logger.info("价格：" + httpUntil.getTemp().get("price") + "邮费:" + httpUntil.getTemp().get("checkout[shipping_rate][id]"));
    }

    private void paymentMethodGet(HttpUntil httpUntil) throws BotException {
        httpUntil.get(httpUntil.getTemp().get("payment_methodURL"));
        httpUntil.getTemp().put("authenticity_token", RegexParse.baseParse(httpUntil.getResult(), "authenticity_token\"\\s*?value=\"([\\S]*?)\"", 1));
        httpUntil.getTemp().put("payment_gateway", RegexParse.baseParse(httpUntil.getResult(), "checkout_payment_gateway_(\\S*?)\"", 1));
    }

    /**将美国的电话处理到和官网一致*/
    private  String palacesUsaPhone(String phone){
            if(phone!=null){
                String temp= phone.replaceAll("[^\\d]","").trim();
                if(temp.length()!=10)
                    return  phone;
                phone=temp;
                int i=-1,j=0;
                StringBuilder stringBuilder=new StringBuilder();
                for(char c:phone.trim().toCharArray()){
                    if((i=i+1)==3)
                    {if(j<2){stringBuilder.append("-");}i=0;j++;}
                    stringBuilder.append(c);
                }
                phone=stringBuilder.toString();
                phone=phone.replaceAll("^[\\d]{3}-","("+RegexParse.baseParse(phone,"(^[\\d]{3})",1)+") ");
            }
            return  phone;
    }


    /*****
     * 获取验证码流程，如果需要取验证码，就跟缓存去要验证码，如果要不到就一直等待；如果要得到就返回；
     * @param requestBuilder
     * @param isCaptcha
     * @param web
     * @return
     */
    private RequestBuilder getCaptcha(RequestBuilder requestBuilder, boolean isCaptcha, PalacesUSAWEB web) {
        if (isCaptcha) {
            String result = null;
            while ((result = CaptchaCache.GetInstance().getCaptchaResult(web.getId() + "")) == null) {
                try {
                    Thread.sleep(600);
                    logger.info("excuter等待验证码");
                    TableMessQueue.getInstance().addMess(web.getIndex(), "等待验证码...");
                } catch (Exception e) {
                    logger.error("excuter获取验证码失败", e);
                }
            }
            logger.info("excuter拿到验证码" + result);
            TableMessQueue.getInstance().addMess(web.getIndex(), "拿到验证码...");
            requestBuilder.addParameter("g-recaptcha-response", result);
        }
        return requestBuilder;
    }



    @Override
    public void addCart(PalacesUSAGoods goods, PalacesUSAPersion palacesUSAPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder = RequestBuilder.post()
                .setUri(new URI(web.getAddCartURL()))
                .addParameter("id", goods.getId())
                .addParameter("quantity", goods.getQuantity())
                .setHeader("User-Agent", httpUntil.USERAGENT);
        httpUntil.post(httpUntil, builder);
        getNote(web, httpUntil);
    }

    @Override
    public void cartCheckOut(PalacesUSAGoods goods, PalacesUSAPersion palacesUSAPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder = RequestBuilder.post()
                .setUri(new URI(web.getCartURL()))
                .addParameter("updates[" + goods.getId() + "]", "1")
                .addParameter("checkout", "Checkout")
                .addParameter("note", httpUntil.getTemp().get("note"))
                .setHeader("User-Agent", httpUntil.USERAGENT);
        httpUntil.post(httpUntil, builder);
        super.getCheckOutUrlAndAuth(httpUntil, web.getBaseURL());
    }

    @Override
    public void shopingMethod(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        RequestBuilder builder = RequestBuilder.post()
                .setUri(new URI(super.checkOutURL))
                .addParameter("utf8", "✓")
                .addParameter("_method", "patch")
                .addParameter("authenticity_token", super.authenticity_token)
                .addParameter("previous_step", "contact_information")
                .addParameter("step", "shipping_method")
                .addParameter("checkout[email]", palacesPersion.getEmail())
                .addParameter("checkout[buyer_accepts_marketing]", "0")
                .addParameter("checkout[shipping_address][first_name]", "")
                .addParameter("checkout[shipping_address][last_name]", "")
                .addParameter("checkout[shipping_address][address1]", "")
                .addParameter("checkout[shipping_address][address2]", "")
                .addParameter("checkout[shipping_address][city]", "")
                .addParameter("checkout[shipping_address][country]", "")
                .addParameter("checkout[shipping_address][province]", "")
                .addParameter("checkout[shipping_address][zip]", "")
                .addParameter("checkout[shipping_address][phone]", "")
                .addParameter("checkout[shipping_address][first_name]", palacesPersion.getFirst_name())
                .addParameter("checkout[shipping_address][last_name]", palacesPersion.getLast_name())
                .addParameter("checkout[shipping_address][address1]", palacesPersion.getAddress1())
                .addParameter("checkout[shipping_address][address2]", palacesPersion.getAddress2())
                .addParameter("checkout[shipping_address][city]", palacesPersion.getCity())
                .addParameter("checkout[shipping_address][country]", palacesPersion.getCountry())
                .addParameter("checkout[shipping_address][province]", palacesPersion.getProvince())//这个地方我没看见；
                .addParameter("checkout[shipping_address][zip]", palacesPersion.getZip())
                .addParameter("checkout[shipping_address][phone]", palacesUsaPhone(palacesPersion.getPhone()))
                .addParameter("checkout[remember_me]", "")
                .addParameter("checkout[remember_me]", "0");
        builder = getCaptcha(builder, web.isStartCaptcha(), web);
        builder.addParameter("button", "")
                .addParameter("checkout[client_details][browser_width]", "414")
                .addParameter("checkout[client_details][browser_height]", "736")
                .addParameter("checkout[client_details][javascript_enabled]", "1")
                .setHeader("User-Agent", httpUntil.USERAGENT)
                .setHeader("Referer", httpUntil.getTemp().get("checkOutURL"))
        ;
        httpUntil.post(httpUntil, builder);
        String url = "";
        for (Header header : httpUntil.getHeaders()) {
            if ("Location".equals(header.getName())) {
                url = header.getValue();
                httpUntil.getTemp().put("checkOutURL", url);//
                break;
            }
        }
        contactInformation(httpUntil, web);
    }

    @Override
    public void paymentMethod(PalacesUSAGoods goods, PalacesUSAPersion palacesUSAPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws URISyntaxException, BotException {
        logger.info("邮费==============================:" + httpUntil.getTemp().get("checkout[shipping_rate][id]"));
        RequestBuilder builder = RequestBuilder.post()
                .setUri(new URI(super.checkOutURL))
                .addParameter("utf8", "✓")
                .addParameter("_method", "patch")
                .addParameter("authenticity_token", super.authenticity_token)
                .addParameter("previous_step", "shipping_method")
                .addParameter("step", "payment_method")
                //.addParameter("checkout[shipping_rate][id]",httpUntil.getTemp().get("checkout[shipping_rate][id]"))
                .addParameter("checkout[shipping_rate][id]", httpUntil.getTemp().get("checkout[shipping_rate][id]"))
                .addParameter("button", "")
                .addParameter("checkout[client_details][browser_width]", "414")
                .addParameter("checkout[client_details][browser_height]", "736")
                .addParameter("checkout[client_details][javascript_enabled]", "1")
                .setHeader("User-Agent", httpUntil.USERAGENT);
        httpUntil.post(httpUntil, builder);
        String url = "";
        for (Header header : httpUntil.getHeaders()) {
            if ("Location".equals(header.getName())) {
                url = header.getValue();
                httpUntil.getTemp().put("payment_methodURL", url);//
                break;
            }
        }
        paymentMethodGet(httpUntil);
    }

    @Override
    public void excute(PalacesUSAGoods goods, PalacesUSAPersion palacesUSAPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws Exception, BotException {

    }

    @Override
    public void pay(PalacesUSAGoods goods, PalacesUSAPersion palacesPersion, PalacesUSAWEB web, HttpUntil httpUntil) throws Exception, BotException {
        String cartJson="";
        if (web.getWebsite().equals("palaces-global"))
            cartJson=palacesPersion.getGlobaleCard();
        else if (web.getWebsite().equals("palaces-usa"))
            cartJson=palacesPersion.getUSACard();
        RequestBuilder builder = RequestBuilder.post()
                .setUri(new URI(web.getPAYURL()))
                .setEntity(new StringEntity(cartJson, "UTF-8"))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setHeader("User-Agent", httpUntil.USERAGENT);
        logger.info(palacesPersion.getCard());
        httpUntil.print(builder);
        httpUntil = httpUntil.post(builder.build());


        logger.info("网关==============================:" + httpUntil.getTemp().get("payment_gateway"));
        RequestBuilder httpUriRequest = RequestBuilder.post()
                .setUri(new URI(super.checkOutURL))
                .addParameter("utf8", "✓")
                .addParameter("_method", "patch")
                .addParameter("authenticity_token", super.authenticity_token)
                .addParameter("previous_step", "payment_method")
                .addParameter("step", "")
                .addParameter("s", RegexParse.baseParse(httpUntil.getResult(), "id\":\"([\\S]*?)\"", 1))
                .addParameter("checkout[payment_gateway]", httpUntil.getTemp().get("payment_gateway"))
                .addParameter("checkout[credit_card][vault]", "false")
                .addParameter("checkout[different_billing_address]", "false")
                .addParameter("checkout[total_price]", httpUntil.getTemp().get("price"))
                .addParameter("complete", "1")
                .addParameter("checkout[client_details][browser_width]", "414")
                .addParameter("checkout[client_details][browser_height]", "736")
                .addParameter("checkout[client_details][javascript_enabled]", "1")
                .setHeader("User-Agent", httpUntil.USERAGENT);
        httpUntil.post(httpUntil, httpUriRequest);
    }
}
