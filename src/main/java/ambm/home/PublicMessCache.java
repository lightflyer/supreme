package ambm.home;

import java.util.LinkedList;
import java.util.List;

public class PublicMessCache {

    private PublicMessCache() { }

    volatile  static  PublicMessCache publicMessCache;

    private List<String> messqueue=new LinkedList<>();
    public synchronized static PublicMessCache getInstance() {
        if(publicMessCache==null)
            publicMessCache=new PublicMessCache();
        return publicMessCache;
    }

    public synchronized void addMess(String mess){
        messqueue.add(mess);
    }

    public synchronized String flushMess(){
        if(messqueue.isEmpty())
            return null;
        return messqueue.remove(0);
    }


}
