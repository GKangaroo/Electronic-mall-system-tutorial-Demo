package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.openFeign.RegionDao;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(AddressPo.class)
@Builder
public class Address extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Address.class);


    @Setter
    @Getter
    private Long customerId;

    @Setter
    @Getter
    private Long regionId;

    @JsonIgnore
    private Region region;

    @Setter
    @JsonIgnore
    private RegionDao regionDao;

    public Region getRegion(){
        if (null == this.region && null != this.regionDao){
            InternalReturnObject<Region> ret = this.regionDao.getRegionById(this.regionId);
            logger.debug("getRegion: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.region = ret.getData();
            }
        }
        return this.region;
    }

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    private String consignee;

    @Setter
    @Getter
    private String mobile;

    @Setter
    @Getter
    private Integer beDefault;



}
