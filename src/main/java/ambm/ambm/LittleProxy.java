package ambm.ambm;

import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class LittleProxy {

    private static Logger logger = LoggerFactory.getLogger(LittleProxy.class);

    public static void initProxy() {
        String ip = "127.0.0.1";
        String port = "9988";
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", ip);
        System.setProperty("http.proxyPort", port);
//      System.setProperty("https.proxyHost", ip);
//      System.setProperty("https.proxyPort", port);
        DefaultHttpProxyServer.bootstrap()
                .withPort(9988)
                .withFiltersSource(getFiltersSource())
                .start();
    }

    /****
     * originalRequest.getUri().contains("palaceskateboards")
     * 无论包含哪个域名，都转到localhost
     * @return
     */
    private static HttpFiltersSource getFiltersSource() {
        return new HttpFiltersSourceAdapter() {
            @Override
            public HttpFilters filterRequest(HttpRequest originalRequest) {
                return new HttpFiltersAdapter(originalRequest) {
                    @Override
                    public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
                        logger.info("目前代理收到的url请求为:" + originalRequest.getUri());
                        if (originalRequest.getUri().contains("localhost")
                                || originalRequest.getUri().contains("palaceskateboards")
                                || originalRequest.getUri().contains("supremenewyork")
                        ) {
                            logger.info("目前代理收到的url请求为:" + originalRequest.getUri() + "满足条件转发");
                            return new InetSocketAddress("127.0.0.1", 8080);
                        }
                        return null;
                    }
                };
            }
        };
    }
}
