package tool;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

/*****
 * @author jhy
 */

public class HttpUntil {


    private Logger logger = LoggerFactory.getLogger(HttpUntil.class);

    private HttpClientContext context = HttpClientContext.create();

    private Registry<CookieSpecProvider> cookieSpecProviderRegistry = RegistryBuilder.<CookieSpecProvider>create().register("myCookieSpec", context -> new MyCookieSpec()).build();//注册自定义CookieSpec

    private CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("myCookieSpec").build()).build();

    private CloseableHttpResponse response;

    private String result;

    private String url;

    private StatusLine statusLine;

    private Header[] headers;

    private List<Cookie> cookies;

    public LinkedHashMap<String, String> getTemp() {
        logger.info(">>>>>>>>>>>>>>目前临时保存的变量>>start>>>>>>>>>>>>>>");
        temp.forEach((k,v)->logger.info(k+" > "+v));
        logger.info("<<<<<<<<<<<<<<<目前临时保存的变量>>end<<<<<<<<<<<<<<<");
        return temp;
    }

    public void setTemp(LinkedHashMap<String, String> temp) {
        this.temp = temp;
    }

    private LinkedHashMap<String,String> temp=new LinkedHashMap<>();

    public static final String USERAGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";

    /*****
     * 初始化cookie
     */
    public HttpUntil() {
        context.setCookieSpecRegistry(cookieSpecProviderRegistry);
    }


//    public HttpUntil() {
//        httpclient = HttpClients.custom().setProxy(new HttpHost("127.0.0.1",8888)).setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("myCookieSpec").build()).build();
//        context.setCookieSpecRegistry(cookieSpecProviderRegistry);
//    }

    /*****
     * 初始化cookie
     */
//    public HttpUntil(String proxyHost,int proxyPort) {
//        httpclient = HttpClients.custom().setProxy(new HttpHost(proxyHost,proxyPort)).setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("myCookieSpec").build()).build();
//        context.setCookieSpecRegistry(cookieSpecProviderRegistry);
//    }

    public CloseableHttpResponse getResponse() {
        return response;
    }

    public String getResult() {
        return result;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    /*****
     * 主要是sp这个网站，貌似不符合cookie某规范，时间总出问题；
     * 我暂时先把他干掉，干掉的时间的意思应该是告诉客户端session的过期时间；
     */
    class MyCookieSpec extends DefaultCookieSpec {
        @Override
        public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
            String value = header.getValue();
            String prefix = "expires";
            if (RegexParse.ismatching(value, prefix)) {
                String expires = RegexParse.baseParse(value, "(expires=[\\S\\s]*?)(;|$)", 1) + RegexParse.baseParse(value, "expires=([\\S\\s]*?)(;|$)", 2);
                value = value.replace(expires, "");
            }
            header = new BasicHeader(header.getName(), value);
            return super.parse(header, cookieOrigin);
        }
    }

   public  void setTimeOut(int time){
       context.setRequestConfig(RequestConfig.custom()
               .setConnectTimeout(time).setConnectionRequestTimeout(time)
               .setSocketTimeout(time).build());
   }

    /****
     * get method
     * @param url
     * @return
     * @throws Exception
     */
    public HttpUntil get(String url){
        try {
            this.url=url;
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("User-Agent", USERAGENT);
            this.response = httpclient.execute(httpget, context);
            setParams();
        } catch (Exception e) {
            logger.error("get请求失败"+url, e);
        } finally {
            try {
                if(response!=null)
                response.close();
            } catch (Exception e) {
                logger.error("get请求失败", e);
            }
        }
        return this;
    }



    /****
     * post method
     * @param httpUriRequest
     * @return
     */
    public HttpUntil post(HttpUriRequest httpUriRequest) {
        try {
            this.url=httpUriRequest.getURI().toString();
            httpUriRequest.setHeader("User-Agent", USERAGENT);
            this.response = httpclient.execute(httpUriRequest, context);
            setParams();
        } catch (Exception e) {
            logger.error("post请求失败"+this.url, e);
        } finally {
            try {
                if(response!=null)
                response.close();
            } catch (IOException e) {
                logger.error("post请求失败"+this.url, e);
            }
        }
        return this;
    }


    public void  post(HttpUntil httpUntil, RequestBuilder builder)  {
        print(builder);
        httpUntil = httpUntil.post(builder.build());
        httpUntil.printResult();
    }

    public void print(RequestBuilder builder){
        logger.info("-----------发送的参数-------post-parameters<<<start------------------");
        logger.info("URL:"+builder.getUri());
        logger.info("Method:"+builder.getMethod());
        logger.info("Entity:"+builder.getEntity());
        logger.info("Parameters:"+builder.getParameters());
        logger.info("-----------发送的参数-------post-parameters>>>>-end------------------");
    }
    private void setParams() {
        try {
            HttpEntity entity = response.getEntity();
            this.headers = response.getAllHeaders();
            this.statusLine = response.getStatusLine();
            this.result = EntityUtils.toString(entity);
            this.cookies = context.getCookieStore().getCookies();
        } catch (Exception e) {
            logger.error("设置参数失败", e);
        }
    }

    public void printResult(){
        printResultBody(true);
    }
    public void printResultBody(boolean prientBody){
        long start=System.currentTimeMillis();
        logger.info("==================http-result-start=========================\r\n");
        System.out.println("statusLine:    "+this.statusLine);
        System.out.println("url:    "+this.url);
        logger.info("------------------http-headers------------------------------\r\n");
        for(Header header:this.headers){
            System.out.println("name:    "+header.getName()+"    "+"value:    "+header.getValue());
        }
        logger.info("------------------http-cookies------------------------------\r\n");
        for (Cookie cookie:this.cookies){
            System.out.println("name:    "+cookie.getName()+"    "+"value:    "+cookie.getValue());
        }
        logger.info("------------------http-result-------------------------------\r\n");
        if(prientBody)
        logger.info(this.result);
        logger.info("==================http-result-end===========================\r\n");
        logger.info("请求耗时:"+(System.currentTimeMillis()-start));
    }

    public  String urlResolve(String baseUrl, String fragmentUrl) {
        String url = null;
        try {
            if (fragmentUrl.contains("http://"))
                return fragmentUrl;
            if (!fragmentUrl.contains("javascript"))
                url = new URI(baseUrl).resolve(new URI(fragmentUrl)).toString();
            url = url.replaceFirst("#[\\S]*", "");
        } catch (Throwable e) {
            logger.error("url拼接失败",e);
            url = null;
        }
        return url;
    }

    public static void main(String[] args) {

        new HttpUntil().get("https://mp.weixin.qq.com/s?src=11&timestamp=1561514401&ver=1691&signature=zdPd15iS5cpUNTW9mPocTESd5895MlxnPiXwRMxvPaQtmZD26-H0FCT7x47qLngMlFj4UtjsyyB-OXLsRHK9TlzmMpq-1h9i5kocdrrafIX27gvjEcHUwX76dHnFECEb&new=1").printResult();
    }

}


