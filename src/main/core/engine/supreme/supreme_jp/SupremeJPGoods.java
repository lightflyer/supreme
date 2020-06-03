package engine.supreme.supreme_jp;


import base.BaseGoods;
import lombok.Getter;
import lombok.Setter;

public class SupremeJPGoods extends BaseGoods {

    @Getter @Setter public String productId;
    @Getter @Setter public String size;
    @Getter @Setter public String style;
    @Getter @Setter public String qty;//也是抓包拿到的，一直都是1；
    @Getter @Setter public String cookieSub;//这个是上下文传输的时候获取到的，实际上是size 的url编码后的；
}
