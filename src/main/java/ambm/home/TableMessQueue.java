package ambm.home;

import java.util.LinkedList;
import java.util.List;

public class TableMessQueue {

    private TableMessQueue() { }

    volatile  static  TableMessQueue tableMessQueue;

    private List<Mess> messqueue=new LinkedList<>();
    public synchronized static TableMessQueue getInstance() {
        if(tableMessQueue==null)
            tableMessQueue=new TableMessQueue();
        return tableMessQueue;
    }

    public synchronized void addMess(int rowIdOfUI,String state){
        messqueue.add(new Mess(rowIdOfUI,state));
    }

    public synchronized Mess flushMess(){
        if(messqueue.isEmpty())
            return null;
        return messqueue.remove(0);
    }

    public static class Mess{
       public int rowIdOfUI;//页面id
       public String state;

        private Mess(int rowIdOfUI, String state) {
            this.rowIdOfUI = rowIdOfUI;
            this.state = state;
        }
    }
}
