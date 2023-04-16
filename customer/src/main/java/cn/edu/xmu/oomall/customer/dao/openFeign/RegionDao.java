package cn.edu.xmu.oomall.customer.dao.openFeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.dao.bo.Region;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface RegionDao {

    //用于查询region_name
    @GetMapping("/region/{id}")
    InternalReturnObject<Region> getRegionById(@PathVariable Long id);

}
