package ambm.task.palaces_usa;

import ambm.basic.CountryTable;
import ambm.task.TaskTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "palacesusa", url = "${feign.client.url}")
public interface PalacesUSADao {
    @RequestMapping(value ="/countrysByWebsite" ,method = RequestMethod.POST)
    List<String> countryByWebsite(@RequestParam(value="website") String website);

    @RequestMapping(value ="/ProvincesByWebsiteAndCountry" ,method = RequestMethod.POST)
    List<String> provincesByWebsiteAndCountry(@RequestParam(name = "website")String website,@RequestParam(name = "countryName")String countryName);

    @RequestMapping(value ="/countryByWebsiteAndProvince" ,method = RequestMethod.POST)
    CountryTable countryByWebsiteAndProvince(@RequestParam(name = "website")String website, @RequestParam(name = "provincesName")String provincesName);

}