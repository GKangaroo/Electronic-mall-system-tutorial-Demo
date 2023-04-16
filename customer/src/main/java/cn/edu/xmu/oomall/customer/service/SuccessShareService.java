package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.SuccessShareDao;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import cn.edu.xmu.oomall.customer.dao.bo.Share;
import cn.edu.xmu.oomall.customer.dao.bo.SuccessShare;
import cn.edu.xmu.oomall.customer.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class SuccessShareService {

    private static final Logger logger = LoggerFactory.getLogger(SuccessShareService.class);

    private SuccessShareDao successShareDao;

    @Autowired
    public SuccessShareService(SuccessShareDao successShareDao) {
        this.successShareDao = successShareDao;
    }

    /**
     * 顾客查询分享成功记录
     */
    public PageDto<SuccessShareDto> retrieveSuccessShares(Long id, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, UserDto userDto){
        PageDto<SuccessShare> bos = successShareDao.retrieveByShareId(id, beginTime, endTime, page, pageSize);
        if(bos.getList().size()==0){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }
        List<SuccessShareDto> ret = bos.getList().stream().map(bo -> {
            SuccessShareDto dto = cloneObj(bo, SuccessShareDto.class);
            dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName()));
            dto.setCustomer(cloneObj(bo.getCustomer(), SimpleCustomerDto.class));
            Share share = bo.getShare();
            SimpleShareDto simpleShareDto = cloneObj(bo.getShare(),SimpleShareDto.class);
            dto.setShare(simpleShareDto);
            dto.setOnsale(share.getOnsale()==null?null:cloneObj(bo.getShare().getOnsale(), SimpleOnsaleDto.class));
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 管理员查询所有分享成功记录
     */
    public PageDto<SuccessShareDto> AdminRetrieveSuccessShares(Long did, Long id, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, UserDto userDto){
        PageDto<SuccessShare> bos = successShareDao.retrieveByShopId(did, id, beginTime, endTime, page, pageSize);
        if(bos.getList().size()==0){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }
        List<SuccessShareDto> ret = bos.getList().stream().map(bo -> {
            SuccessShareDto dto = cloneObj(bo, SuccessShareDto.class);
            dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName()));
            dto.setCustomer(cloneObj(bo.getCustomer(), SimpleCustomerDto.class));
            SimpleShareDto simpleShareDto = cloneObj(bo.getShare(),SimpleShareDto.class);
            dto.setShare(simpleShareDto);
            dto.setOnsale(cloneObj(bo.getShare().getOnsale(), SimpleOnsaleDto.class));
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }


}
