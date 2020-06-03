package base;

import ambm.task.BaseWebTable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BaseWEB {

    public BaseWEB() {
    }

    @Getter @Setter public  Integer id;

    public BaseWEB setIndex(Integer index) {
        this.index = index;
        return this;
    }

    @Getter public  Integer index;
    @Getter @Setter public  String baseURL;
    @Getter @Setter public  String cartURL;
    @Getter @Setter public  String addCartURL;
    @Getter @Setter public  String website;
    @Getter @Setter public  String scanURL;
    @Getter @Setter public  String PAYURL="https://elb.deposit.shopifycs.com/sessions";
    @Getter @Setter public  String Host;
    @Getter @Setter public  String captchaKey;
    @Getter @Setter public  int tryTimes=0;
    @Getter @Setter public  boolean preStart=false;//预演
    @Getter @Setter public  String capthchaHost;
    @Getter @Setter public  boolean startCaptcha;
    @Getter @Setter public  boolean proxy;
    @Getter @Setter public  String ip;
    @Getter @Setter public  String port;
    @Getter @Setter public  String proxyUserName;
    @Getter @Setter public  String proxyPassword;

    public BaseWEB(BaseWebTable baseWebTable) {
        this.id = baseWebTable.getId();
        this.baseURL = baseWebTable.getBaseUrl();
        this.cartURL = baseWebTable.getCartUrl();
        this.addCartURL = baseWebTable.getAddCartUrl();
        this.website = baseWebTable.getWebsite();
        this.scanURL = baseWebTable.getScanUrl();
        this.PAYURL = baseWebTable.getPayUrl();
        this.captchaKey = baseWebTable.getCaptchaKey();
        this.capthchaHost = baseWebTable.getCapthchaHost();
    }
}
