package engine.supreme;

import ambm.home.TableMessQueue;
import ambm.task.TaskTable;
import base.BaseWEB;
import com.alibaba.fastjson.JSONObject;
import engine.Scan;
import engine.supreme.supreme_jp.ProxySupremeJP;
import engine.supreme.supreme_jp.SupremeJPGoods;
import engine.supreme.supreme_jp.SupremeJPPersion;
import engine.supreme.supreme_jp.SupremeJPWEB;
import engine.supreme.supreme_usa.*;
import exception.BotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import tool.RegexParse;
import tool.Similarity;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class SupremeneScan extends Scan {

    private Logger logger = LoggerFactory.getLogger(SupremeneScan.class);

    public Map<String, String> scanItem(TaskTable taskTable, BaseWEB baseWEB, HttpUntil httpUntil) {
        for (int i = 0; i < 3; i++) {
            try {
                logger.info("web.getId()>>>" + taskTable.getKeywords() + "第" + i + "次");
                TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "开始扫描>>>" + taskTable.getKeywords()+ "第" + i + "次");
                Map map = getMaxSimilarityId(taskTable, baseWEB, httpUntil);
                if (map != null)
                    return map;
            } catch (Throwable e) {
                logger.info("扫描已发售货品出错....", e);
            }
            try {
                Thread.sleep(3000);
                httpUntil = new HttpUntil();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

        private Map<String, String> getMaxSimilarityId (TaskTable taskTable, BaseWEB baseWEB, HttpUntil httpUntil) throws
        BotException {
            String productId = "";
            LinkedHashMap<String, String> result = new LinkedHashMap<>();
            LinkedHashMap<String, String> resultBackUp = new LinkedHashMap<>();
            HashMap hashMap = new HashMap();
            //此处是通过这个url获取全部的商品，是json格式的;
            List<String> keys = null;
            try {
                httpUntil.get(baseWEB.getScanURL());
                logger.info("scan ... 扫描首页的状态"+httpUntil.getStatusLine());
                if (taskTable.isNew_goods()) {
                    keys = RegexParse.baseParseList(RegexParse.baseParse(httpUntil.getResult(), "\"new\": \\[[\\s\\S]*?]\\]"), "\"name\":\\s*?\"([\\s\\S]*?)\",\\s*?\"id[\\s\\S]*?,", 0);
                } else {
                    keys = RegexParse.baseParseList(httpUntil.getResult(), "\"name\":\\s*?\"([\\s\\S]*?)\",\\s*?\"id[\\s\\S]*?,", 0);
                }
            } catch (Exception e) {
                logger.info("web.getId()", e);
            }
            if (keys == null || keys.size() == 0) {
                TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "扫描出错,检测vpn&浏览器是否正常能打开，或被封杀");
                logger.info("扫描出错，请检测是否能打开官网");
                return null;
            }
            keys.forEach(e -> {
                if (e != null)
                    hashMap.put(RegexParse.baseParse(e, "\"name\":\\s*?\"([\\s\\S]*?)\",\\s*?\"id[\\s\\S]*?,", 1), RegexParse.baseParse(e, "\"id\"\\s*?:([\\s\\S]*?),", 1));
            });
            httpUntil.printResult();
            //拿到最匹配的产品名称
            String keywords = Similarity.getMaxSimilarity(new ArrayList<String>(hashMap.keySet()), taskTable.getKeywords());
            //这个id，是就是购买的时候https://www.supremenewyork.com/mobile#products/5653  这个后面跟的这个数字，因为我发现都是这个规律，也就是拼接这个数字就可以到商品详情页
            productId = hashMap.get(keywords).toString();
            //下面这个方位是，获取具体的商品页面，也是https://www.supremenewyork.com/mobile#products/5653这个访问后抓包获取的，应该是这个商品的所有尺寸信息，风格信息，但是是多个，暂时我用的第一个
            JSONObject jsonObject = JSONObject.parseObject(httpUntil.get(baseWEB.getBaseURL() + "/shop/" + hashMap.get(keywords) + ".json").getResult());
            for (Object jsonObject1 : jsonObject.getJSONArray("styles")) {
                JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.toString());
                String styleId = jsonObject2.getString("id");//这个是"styles": [{"id": 21709, 实际是style 的id：
                for (Object jsonObject3 : jsonObject2.getJSONArray("sizes")) {//这个是循环size获取跟用户匹配的size；
                    JSONObject jsonObject4 = JSONObject.parseObject(jsonObject3.toString());
                    String Wsize = jsonObject4.getString("name");//尺码-人能看懂的;比如samll
                    String sizeId = jsonObject4.getString("id");//尺码对应的id，也就是查明对应的id
                    if (taskTable.getBackup_size().equals("random")) {
                        resultBackUp.put("style", styleId);//这个是style的id，后面抢购会用
                        resultBackUp.put("size", sizeId);//这个是size 的id后面抢购会用
                        resultBackUp.put("productId", productId);//这个是具体的商品的页面的那个id；
                        resultBackUp.put("keywords", keywords);
                    }
                    if (RegexParse.ismatching(Wsize, taskTable.getSize()))//这里需要尺码
                    {
                        result.put("style", styleId);
                        result.put("size", sizeId);
                        result.put("productId", productId);
                        result.put("keywords", keywords);
                        break;
                    }
                    if (RegexParse.ismatching(Wsize, taskTable.getBackup_size()))//这里需要尺码
                    {
                        resultBackUp.put("style", styleId);
                        resultBackUp.put("size", sizeId);
                        resultBackUp.put("productId", productId);
                        resultBackUp.put("keywords", keywords);
                        break;
                    }
                }
                break;
            }
            if (result != null && result.size() > 0)
                return result;
            if (resultBackUp != null && resultBackUp.size() > 0)
                return resultBackUp;
            return null;
        }

        @Override
        public void scan (TaskTable taskTable, BaseWEB baseWEB){
            TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "开始扫描货品");
            Map<String, String> map = scanItem(taskTable, baseWEB, new HttpUntil());
            if(map!=null)
                setParms(taskTable, map, baseWEB);
            else
                TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "扫描货品出错,请停止后在开启");
        }

        public void setParms (TaskTable taskTable, Map < String, String > map, BaseWEB baseWEB){
            if (baseWEB.getWebsite().equals("supreme-jp"))
                setParmsJP(taskTable, map, baseWEB);
            else if (baseWEB.getWebsite().equals("supreme-usa"))
                setParmsUSA(taskTable, map, baseWEB);
        }

        void setParmsUSA (TaskTable taskTable, Map < String, String > map, BaseWEB baseWEB){
            TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "扫描到货品" + map.get("keywords"));
            SupremeneUSAGoods supremeneGoods = new SupremeneUSAGoods();
            supremeneGoods.setSize(map.get("size"));
            supremeneGoods.setStyle(map.get("style"));
            supremeneGoods.setQty("1");

            try {
                //抓包获取的参数，我观察应该是size {"62302":1}
                supremeneGoods.setCookieSub(java.net.URLEncoder.encode("{\"" + map.get("size") + "\":1}", "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SupremeneUSAPersion supremenePersion = new SupremeneUSAPersion();
            supremenePersion.setFirst_name(taskTable.getFirst_name());//-----这个就是全name了
            supremenePersion.setEmail(taskTable.getEmail());
            supremenePersion.setPhone(taskTable.getPhone());
            supremenePersion.setAddress1(taskTable.getAddress());
            supremenePersion.setAddress2(taskTable.getApartment());
            supremenePersion.setZip(taskTable.getPostcode());
            supremenePersion.setCity(taskTable.getCity());
            supremenePersion.setCountyState(taskTable.getCounty_state());//-------这个是县；order_billing_state
            supremenePersion.setCountry(taskTable.getCountry());
            supremenePersion.setCard(taskTable.getCard_number(), taskTable.getCard_holder_name(), taskTable.getMmyy().split("/")[0], taskTable.getMmyy().split("/")[1], taskTable.getCvv());


            SupremeneUSAWEB web = new SupremeneUSAWEB();
            web.setId(taskTable.getId());
            web.setIndex(baseWEB.getIndex());
            web.setBaseURL(baseWEB.getBaseURL());
            web.setAddCartURL(baseWEB.getBaseURL() + "/shop/" + map.get("productId") + "/add.json");//"https://www.supremenewyork.com/shop/5703/add.json"这个是拼接的
            web.setCartURL(baseWEB.getCartURL());
            web.setStartCaptcha(taskTable.isStart_captcha());
            web.setWebsite(baseWEB.getWebsite());
            web.setCaptchaKey(baseWEB.getCaptchaKey());
            web.setHost(baseWEB.getHost());
            web.setCapthchaHost(baseWEB.getCapthchaHost());
            ProxySupremeneUSA supremene = new ProxySupremeneUSA();
            for (int p = 0; p < 2; p++) {
                try {
                    supremene.excute(supremeneGoods, supremenePersion, web, new HttpUntil());
                    break;
                } catch (Throwable e) {
                    TableMessQueue.getInstance().addMess(web.getIndex(), "抢购失败...重试第" + p + "次");
                    logger.info("抢购失败...重试第" + p + "次");
                }
            }
        }


        void setParmsJP (TaskTable taskTable, Map < String, String > map, BaseWEB baseWEB){
            TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "扫描到货品" + map.get("keywords"));
            SupremeJPGoods supremeneGoods = new SupremeJPGoods();
            supremeneGoods.setSize(map.get("size"));
            supremeneGoods.setStyle(map.get("style"));
            supremeneGoods.setQty("1");

            try {
                //抓包获取的参数，我观察应该是size {"62302":1}
                supremeneGoods.setCookieSub(java.net.URLEncoder.encode("{\"" + map.get("size") + "\":1}", "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SupremeJPPersion supremenePersion = new SupremeJPPersion();
            supremenePersion.setPhone(taskTable.getPhone());
            supremenePersion.setEmail(taskTable.getEmail());
            supremenePersion.setFirst_name(taskTable.getFirst_name());//-----这个就是全name了
            supremenePersion.setZip(taskTable.getPostcode());
            supremenePersion.setCountyState(taskTable.getCounty_state());//-------这个是县；order_billing_state
            supremenePersion.setCity(taskTable.getCity());
            supremenePersion.setAddress1(taskTable.getAddress());
            supremenePersion.setCartType(taskTable.getPay_method());
            supremenePersion.setCard(taskTable.getCard_number(), taskTable.getCard_holder_name(), taskTable.getMmyy().split("/")[0], taskTable.getMmyy().split("/")[1], taskTable.getCvv());

            SupremeJPWEB web = new SupremeJPWEB();
            web.setId(taskTable.getId());
            web.setIndex(baseWEB.getIndex());
            web.setBaseURL(baseWEB.getBaseURL());
            web.setAddCartURL(baseWEB.getBaseURL() + "/shop/" + map.get("productId") + "/add.json");//"https://www.supremenewyork.com/shop/5703/add.json"这个是拼接的
            web.setCartURL(baseWEB.getCartURL());
            web.setStartCaptcha(taskTable.isStart_captcha());
            web.setWebsite(baseWEB.getWebsite());
            web.setCaptchaKey(baseWEB.getCaptchaKey());
            web.setHost(baseWEB.getHost());
            web.setCapthchaHost(baseWEB.getCapthchaHost());
            ProxySupremeJP supremene = new ProxySupremeJP();
            for (int p = 0; p < 2; p++) {
                try {
                    supremene.excute(supremeneGoods, supremenePersion, web, new HttpUntil());
                    break;
                } catch (Throwable e) {
                    TableMessQueue.getInstance().addMess(web.getIndex(), "抢购失败...重试第" + p + "次");
                    logger.info("抢购失败...重试第" + p + "次");
                }
            }
        }


    }
