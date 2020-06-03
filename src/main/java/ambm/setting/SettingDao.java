package ambm.setting;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "setting", url = "${feign.client.url}")
public interface SettingDao {

    @RequestMapping(value ="/proxyByUsernameAndPassword")
    SettingTable proxyByUsernameAndPassword(@RequestParam(value="username") String username,@RequestParam(value="password") String password);

    @RequestMapping(value ="/proxySaveOrUpdate" ,method = RequestMethod.POST)
    void proxySaveOrUpdate(
            @RequestParam(value="ip") String ip,@RequestParam(value="port") String port
            ,@RequestParam(value="proxyUsername") String proxyUsername,@RequestParam(value="proxyPassword") String proxyPassword
            ,@RequestParam(value="username") String username,@RequestParam(value="password") String password,@RequestParam(value="enabled") Boolean enabled
    );

}
