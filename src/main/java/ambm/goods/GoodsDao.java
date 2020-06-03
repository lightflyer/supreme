package ambm.goods;

import ambm.basic.SizeTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "goods", url = "${feign.client.url}")
public interface GoodsDao {
    @RequestMapping(value ="/sizesByWebsite" ,method = RequestMethod.POST)
    List<SizeTable> sizesByWebsite(@RequestParam(value="website") String website);
}
