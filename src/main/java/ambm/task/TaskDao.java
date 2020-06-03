package ambm.task;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "task", url = "${feign.client.url}")
public interface TaskDao {

    //http://127.0.0.1:9999/
    @RequestMapping(value ="/allBaseWebs" ,method = RequestMethod.POST)
    List<BaseWebTable> allBaseWebs();

    @RequestMapping(value ="/taskSave" ,method = RequestMethod.POST)
    public String taskSave(@RequestBody TaskTable task);


}
