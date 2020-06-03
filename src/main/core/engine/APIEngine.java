package engine;


import base.BaseGoods;
import base.BasePerson;
import base.BaseWEB;
import exception.BotException;

import java.net.URISyntaxException;

public interface APIEngine<Goods extends BaseGoods,Persion extends BasePerson,Web extends BaseWEB,HttpUntil> {


    public void addCart(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws URISyntaxException, BotException;

    public void cartCheckOut(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws URISyntaxException, BotException;

    public void shopingMethod(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws URISyntaxException, BotException ;

    public void paymentMethod(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws URISyntaxException, BotException ;

    public void excute(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws Exception, BotException ;

    public void pay(Goods goods, Persion persion, Web web, HttpUntil httpUntil) throws Exception, BotException ;






}
