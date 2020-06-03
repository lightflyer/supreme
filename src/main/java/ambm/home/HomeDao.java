package ambm.home;

import ambm.task.BaseWebTable;
import ambm.task.TaskTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "home", url = "${feign.client.url}")
public interface HomeDao {

    @RequestMapping(value ="/tasksByUsernameAndPassword" ,method = RequestMethod.POST)
    List<TaskTable> tasksByUsernameAndPassword(@RequestParam(value="username") String username, @RequestParam(value="password") String password);

    @RequestMapping(value ="/deleteTaskByUsernameAndPasswordAndId" ,method = RequestMethod.POST)
    void deleteTaskByUsernameAndPasswordAndId(@RequestParam(value="username") String username, @RequestParam(value="password") String password,@RequestParam Integer id);


    @RequestMapping(value ="/taskById" ,method = RequestMethod.POST)
    TaskTable taskById(@RequestParam(value="username") String username, @RequestParam(value="password") String password,@RequestParam Integer id);

    @RequestMapping(value ="/baseWebByWebsite" ,method = RequestMethod.POST)
    BaseWebTable baseWebByWebsite(@RequestParam(value="website") String website);



}
