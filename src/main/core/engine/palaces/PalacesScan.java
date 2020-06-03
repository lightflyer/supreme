package engine.palaces;

import ambm.home.TableMessQueue;
import ambm.task.TaskTable;
import base.BaseWEB;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import engine.Scan;
import engine.palaces.global.PalacesGlobalGoods;
import engine.palaces.global.PalacesGlobalPersion;
import engine.palaces.global.PalacesGlobalWEB;
import engine.palaces.global.ProxyGlobalPalaces;
import engine.palaces.usa.PalacesUSAGoods;
import engine.palaces.usa.PalacesUSAPersion;
import engine.palaces.usa.PalacesUSAWEB;
import engine.palaces.usa.ProxyUSAPalaces;
import exception.BotException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import tool.RegexParse;
import tool.Similarity;
import java.io.StringReader;
import java.util.*;

public class PalacesScan extends Scan {

    private Logger logger = LoggerFactory.getLogger(PalacesScan.class);

    public Map<String, String> scanItem(TaskTable taskTable, BaseWEB baseWEB, String url, String keywords, String size, String backSize, HttpUntil httpUntil) {
        while (true) {
            try {
                Map map = getMaxSimilarityId(taskTable, baseWEB, url, keywords, size, backSize, httpUntil);
                if (map != null && map.containsKey("SoldOut"))
                    return null;
                if (map != null)
                    return map;
            } catch (Throwable e) {
                logger.info("扫描已发售货品出错....", e);
            }
        }
    }

    private Map<String, String> getMaxSimilarityId(TaskTable taskTable, BaseWEB baseWEB, String url, String keywords, String size, String backSize, HttpUntil httpUntil) throws BotException {
        Map<String, String> map = new HashMap<>();
        Map<String, String> backMap = new HashMap<>();
        httpUntil = httpUntil.get(url);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new StringReader(httpUntil.getResult().replace("image:", "")));
            Iterator book = document.getRootElement().elementIterator("url");
            HashMap<String, String> keywordsHerf = new HashMap<>();
            while (book.hasNext()) {
                try {
                    Element bookEle = (Element) book.next();
                    keywordsHerf.put(bookEle.element("image").elementTextTrim("title"), bookEle.elementTextTrim("loc"));
                } catch (Exception e) {
                    // logger.error("",e);
                }
            }
            ArrayList keys = new ArrayList<>();
            keys.addAll(keywordsHerf.keySet());
            //logger.info(">>>---------所有匹配到的词...");
            // keys.forEach(e-> System.out.println(e.toString()));
            keywords = Similarity.getMaxSimilarity(keys, keywords);
            httpUntil.get(keywordsHerf.get(keywords));
            if (RegexParse.ismatching(httpUntil.getResult(), "Sold Out")) {
                logger.info("此商品已经售罄.... " + keywords);
                TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "此商品已经售罄..." + keywords);
                //-------------------卖没了
                map.put("SoldOut", "1");
                return null;
            }
            System.out.println(RegexParse.baseParse(httpUntil.getResult(), "var\\s*?meta =\\s*?(\\{[\\s\\S]*?\\});", 1));
            List<String> all = RegexParse.fatherAndSon(httpUntil.getResult(), "<select\\s*?id=\"product-select\"[\\s\\S]*?</select>", 0, 1, "<option value=\"([\\S]*?)\">", 1, -1);
            JSONObject jsonObject = JSONObject.parseObject(RegexParse.baseParse(httpUntil.getResult(), "var\\s*?meta =\\s*?(\\{[\\s\\S]*?\\});", 1));
            JSONArray jsonArray = jsonObject.getJSONObject("product").getJSONArray("variants");
            for (int j = 0; j < jsonArray.size(); j++) {
                String name = jsonArray.getJSONObject(j).getString("name");
                String public_title = jsonArray.getJSONObject(j).getString("public_title");
                if (RegexParse.ismatching(name, keywords)) {
                    if (!all.contains(jsonArray.getJSONObject(j).getString("id")))
                        continue;
                    if (size.equals("random")) {
                        map.put("id", jsonArray.getJSONObject(j).getString("id") + "");
                        break;
                    }
                    if (RegexParse.ismatching(public_title, size)) {
                        map.put("id", jsonArray.getJSONObject(j).getString("id") + "");
                        break;
                    }
                    if (backSize.equals("random")) {
                        backMap.put("id", jsonArray.getJSONObject(j).getString("id") + "");
                    }
                    if (RegexParse.ismatching(public_title, backSize)) {
                        backMap.put("id", jsonArray.getJSONObject(j).getString("id") + "");
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        map.put("keywords", keywords == null ? "" : keywords);
        if (map.size() == 0) {
            if (backMap.size() > 0)
                return backMap;
            return null;
        } else {
            return map;
        }
    }

    @Override
    public void scan(TaskTable taskTable, BaseWEB baseWEB) {
        TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "开始扫描货品");
        long sum = 0;
        HttpUntil httpUntil = new HttpUntil();
        while (true) {
            try {
                String result = httpUntil.get(baseWEB.getBaseURL() + "/password").getResult();
                httpUntil.printResult();
                if (RegexParse.ismatching(result, "Saveth\\s*?pea\\s*?blad")
                        || RegexParse.ismatching(result, "Saveth\\s*?pea\\s*?blad")
                        || RegexParse.ismatching(result, "貯金\\s*?blad")
                ) {
                    try {
                        logger.info("官网关闭状态-稍后...." + (sum));
                        TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "官网关闭状态-稍后..." + (++sum));
                        int x = 1000 + (int) (Math.random() * 5000);//这里如果官网关闭，那么休眠一段时间；
                        Thread.sleep(x);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    httpUntil.printResult();
                    break;
                }
            } catch (Throwable e) {
                httpUntil = null;
                httpUntil = new HttpUntil();
                logger.info("扫描货品出错....");
            }
        }
        logger.info("官网开启...开始扫货");
        TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "官网开启...开始扫货");
        String url = null;
        while (true) {
            try {
                httpUntil = new HttpUntil();
                httpUntil.get(baseWEB.getScanURL());//这个地方需要修改，目前是写死的；还要考虑不同的域名USA-SHOP这个域名；
                url = RegexParse.baseParse(httpUntil.getResult(), "<loc>\\s*?(https://\\S*?sitemap_products\\S*?)\\s*?</loc>", 1);
                if (url != null && !"".equals(url.trim()))
                    break;
            } catch (Throwable e) {
                logger.info("扫描新发售货品出错....", e);
            }
        }
        Map<String, String> map = scanItem(taskTable, baseWEB, url, taskTable.getKeywords(), taskTable.getSize(), taskTable.getBackup_size(), new HttpUntil());
        if (map != null)
            setParms(taskTable, map, baseWEB);
    }

    public void setParms(TaskTable taskTable, Map<String, String> map, BaseWEB baseWEB) {
        logger.info("扫描到货品" + map.get("keywords"));
        TableMessQueue.getInstance().addMess(baseWEB.getIndex(), "扫描到货品" + map.get("keywords"));
        if (taskTable.getWebsite().equals("palaces-global"))
            setGlobleParms(taskTable, map, baseWEB);
        else if (taskTable.getWebsite().equals("palaces-usa"))
            setUSAParms(taskTable, map, baseWEB);
    }

    void setGlobleParms(TaskTable taskTable, Map<String, String> map, BaseWEB baseWEB) {
        PalacesGlobalPersion palacesPersion = new PalacesGlobalPersion();
        palacesPersion.setPhone(taskTable.getPhone());
        palacesPersion.setEmail(taskTable.getEmail());
        palacesPersion.setFirst_name(taskTable.getFirst_name());
        palacesPersion.setLast_name(taskTable.getLash_name());
        palacesPersion.setZip(taskTable.getPostcode());
        palacesPersion.setCountry(taskTable.getCountry());
        palacesPersion.setCity(taskTable.getCity());
        palacesPersion.setAddress1(taskTable.getAddress());
        palacesPersion.setAddress2(taskTable.getApartment());
        palacesPersion.setProvince(taskTable.getCounty_state());
        //String  number,String  name,String  month,String  year,String  verification_value
        palacesPersion.setCard(taskTable.getCard_number(), taskTable.getCard_holder_name(), taskTable.getMmyy().split("/")[0], taskTable.getMmyy().split("/")[1], taskTable.getCvv());


        PalacesGlobalWEB web = new PalacesGlobalWEB();
        web.setId(taskTable.getId());
        web.setIndex(baseWEB.getIndex());
        web.setBaseURL(baseWEB.getBaseURL());
        web.setAddCartURL(baseWEB.getAddCartURL());
        web.setCartURL(baseWEB.getCartURL());
        web.setStartCaptcha(taskTable.isStart_captcha());
        web.setWebsite(taskTable.getWebsite());
        web.setCaptchaKey(baseWEB.getCaptchaKey());
        web.setHost(baseWEB.getHost());
        web.setWebsite(baseWEB.getWebsite());
        web.setCapthchaHost(baseWEB.getCapthchaHost());


        PalacesGlobalGoods palacesGoods = new PalacesGlobalGoods();
        palacesGoods.setId(map.get("id"));
        palacesGoods.setQuantity("1");
        palacesGoods.setNote(map.get("note"));

        for (int p = 0; p < 2; p++) {
            try {
                web.setTryTimes(p);
                new ProxyGlobalPalaces().excute(palacesGoods, palacesPersion, web, new HttpUntil());
                return;
            } catch (Throwable e) {
                TableMessQueue.getInstance().addMess(web.getIndex(), "抢购失败...重试第" + p + "次");
                logger.info("抢购失败...重试第" + p + "次");
            }
        }
    }

    void setUSAParms(TaskTable taskTable, Map<String, String> map, BaseWEB baseWEB) {
        PalacesUSAPersion palacesPersion = new PalacesUSAPersion();
        palacesPersion.setPhone(taskTable.getPhone());
        palacesPersion.setEmail(taskTable.getEmail());
        palacesPersion.setFirst_name(taskTable.getFirst_name());
        palacesPersion.setLast_name(taskTable.getLash_name());
        palacesPersion.setZip(taskTable.getPostcode());
        palacesPersion.setCountry(taskTable.getCountry());
        palacesPersion.setCity(taskTable.getCity());
        palacesPersion.setAddress1(taskTable.getAddress());
        palacesPersion.setAddress2(taskTable.getApartment());
        palacesPersion.setProvince(taskTable.getProvince());
        //String  number,String  name,String  month,String  year,String  verification_value
        palacesPersion.setCard(taskTable.getCard_number(), taskTable.getCard_holder_name(), taskTable.getMmyy().split("/")[0], taskTable.getMmyy().split("/")[1], taskTable.getCvv());

        PalacesUSAWEB web = new PalacesUSAWEB();
        web.setId(taskTable.getId());
        web.setIndex(baseWEB.getIndex());
        web.setBaseURL(baseWEB.getBaseURL());
        web.setAddCartURL(baseWEB.getAddCartURL());
        web.setCartURL(baseWEB.getCartURL());
        web.setStartCaptcha(taskTable.isStart_captcha());
        web.setWebsite(taskTable.getWebsite());
        web.setCaptchaKey(baseWEB.getCaptchaKey());
        web.setHost(baseWEB.getHost());
        web.setWebsite(baseWEB.getWebsite());
        web.setCapthchaHost(baseWEB.getCapthchaHost());


        PalacesUSAGoods palacesGoods = new PalacesUSAGoods();
        palacesGoods.setId(map.get("id"));
        palacesGoods.setQuantity("1");
        palacesGoods.setNote(map.get("note"));

        for (int p = 0; p < 2; p++) {
            try {
                web.setTryTimes(p);
                new ProxyUSAPalaces().excute(palacesGoods, palacesPersion, web, new HttpUntil());
                return;
            } catch (Throwable e) {
                TableMessQueue.getInstance().addMess(web.getIndex(), "抢购失败...重试第" + p + "次");
                logger.info("抢购失败...重试第" + p + "次");
            }
        }

    }
}
