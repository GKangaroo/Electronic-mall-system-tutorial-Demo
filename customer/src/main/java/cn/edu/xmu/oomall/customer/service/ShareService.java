package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.ShareDao;
import cn.edu.xmu.oomall.customer.dao.SuccessShareDao;
import cn.edu.xmu.oomall.customer.dao.bo.Share;
import cn.edu.xmu.oomall.customer.dao.bo.SuccessShare;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
import cn.edu.xmu.oomall.customer.service.dto.ShareDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleAdminUserDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleCustomerDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleOnsaleDto;
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
public class ShareService {

    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    private ShareDao shareDao;
    private SuccessShareDao successShareDao;
    private ProductDao productDao;


    @Autowired
    public ShareService(ShareDao shareDao, SuccessShareDao successShareDao, ProductDao productDao) {
        this.shareDao = shareDao;
        this.successShareDao = successShareDao;
        this.productDao = productDao;
    }


    /**
     * 顾客查询所有分享记录
     */
    public PageDto<ShareDto> retrieveShares(Long productId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, UserDto userDto){
        PageDto<Share> bos = shareDao.retrieveSharesByCustomerId(productId, beginTime, endTime, page, pageSize, userDto);
        if(bos.getList()==null||bos.getList().size()==0){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }
        List<ShareDto> ret = bos.getList().stream().map(bo->{
            ShareDto dto = cloneObj(bo, ShareDto.class);
            dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(),bo.getCreatorName()));
            dto.setCustomer(cloneObj(bo.getCustomer(), SimpleCustomerDto.class));
            dto.setOnsale(bo.getOnsale()==null?null:cloneObj(bo.getOnsale(), SimpleOnsaleDto.class));
            return dto;}).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 顾客查看商品的详细信息（需登录，从分享模式查看商品）
     */
    public ReturnObject retrieveProducts(Long sid, Long id, UserDto userDto){
        SuccessShare bo = SuccessShare.builder().shareId(sid).customerId(userDto.getId()).rebate(0L).build();
        // TODO: 2022/12/21 orderItem是什么值？如何记录？
        //记录分享成功记录
        successShareDao.save(bo,userDto);
        InternalReturnObject<Object> obj = productDao.getProductById(id);
        return new ReturnObject(obj.getData());
    }

    /**
     * 管理员查询商品分享记录
     */
    public PageDto<ShareDto> AdminRetrieveShares(Long did, Long id, Integer page, Integer pageSize, UserDto userDto){
        List<Share> bos = shareDao.retrieveSharesByShopId(did,id,page,pageSize).getList();
        if(bos==null){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }

        List<ShareDto> ret = bos.stream().map(bo->{
            ShareDto dto = cloneObj(bo, ShareDto.class);
            dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(),bo.getCreatorName()));
            dto.setCustomer(cloneObj(bo.getCustomer(), SimpleCustomerDto.class));
            dto.setOnsale(cloneObj(bo.getOnsale(), SimpleOnsaleDto.class));
            return dto;}).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);

    }

}
