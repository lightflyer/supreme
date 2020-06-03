package ambm.login;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "login", url = "${feign.client.url}")
public interface LoginDao {

    @RequestMapping(value ="/login" ,method = RequestMethod.POST)
    String login(@RequestParam(value="username") String username,@RequestParam(value="password") String password);
}

