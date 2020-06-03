package ambm.task.supreme_jp;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "SupremeJP", url = "${feign.client.url}")
public interface SupremeJPDao {
    @RequestMapping(value ="/countrysByWebsite" ,method = RequestMethod.POST)
    List<String> countryByWebsite(@RequestParam(value="website") String website);

    @RequestMapping(value ="/ProvincesByWebsiteAndCountry" ,method = RequestMethod.POST)
    List<String> provincesByWebsiteAndCountry(@RequestParam(name = "website")String website,@RequestParam(name = "countryName")String countryName);


}