package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.Region;
import cn.edu.xmu.oomall.customer.dao.openFeign.RegionDao;
import cn.edu.xmu.oomall.customer.service.dto.AddressDto;
import cn.edu.xmu.oomall.customer.service.dto.AddressPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private AddressDao addressDao;

    private RegionDao regionDao;

    @Autowired
    public AddressService(AddressDao addressDao, RegionDao regionDao) {
        this.addressDao = addressDao;
        this.regionDao = regionDao;
    }

    /**
     * 买家新增地址
     */
    public AddressDto addAddresses(Long regionId, String address, String consignee, String mobile, UserDto userDto){
        Region region = regionDao.getRegionById(regionId).getData();
        //地区不存在
        if(null==region){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"地区",regionId));
        }
        Address bo = Address.builder().region(region).address(address).
                consignee(consignee).mobile(mobile).build();
        bo = addressDao.save(bo,userDto);
        return cloneObj(bo,AddressDto.class);
    }

    /**
     * 买家查询所有已有的地址信息
     */
    public AddressPageDto<AddressDto> retrieveAddresses(Integer page, Integer pageSize, UserDto userDto){
        List<Address> bos = addressDao.retrieveByCustomerId(userDto);
        List<AddressDto> ret = bos.stream().map(bo->cloneObj(bo,AddressDto.class))
                .skip((long) (page - 1) * pageSize).limit(pageSize)
                .collect(Collectors.toList());
        return new AddressPageDto<>(ret, page, pageSize, bos.size(), (int) Math.floor(bos.size() / (float) pageSize));
    }

    /**
     * 买家设置默认地址
     */
    public ReturnObject setDefaultAddresses(Long id, UserDto userDto){
        return addressDao.setDefault(id,userDto);
    }

    /**
     * 买家修改自己的地址信息
     */
    public ReturnObject updateAddresses(Long id, Long regionId, String address, String consignee, String mobile, UserDto userDto){
        Address bo = addressDao.findById(id);
        bo.setRegionId(regionId);
        //地区不存在
        if(null==bo.getRegion()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"地区",regionId));
        }
        bo.setAddress(address);
        bo.setConsignee(consignee);
        bo.setMobile(mobile);
        addressDao.saveById(bo,userDto);
        return new ReturnObject();
    }

    /**
     * 买家删除地址
     */
    public ReturnObject deleteAddresses(Long id, UserDto userDto){
        return addressDao.deleteById(id,userDto);
    }



}
