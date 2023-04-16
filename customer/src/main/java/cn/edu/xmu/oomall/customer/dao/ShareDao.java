package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import cn.edu.xmu.oomall.customer.dao.bo.Share;
import cn.edu.xmu.oomall.customer.mapper.SharePoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.SharePo;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import cn.edu.xmu.oomall.customer.service.dto.AddressPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class ShareDao {

    private static final Logger logger = LoggerFactory.getLogger(ShareDao.class);

    public static final String KEY = "SH%d";

//    @Value("${oomall.share.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private SharePoMapper sharePoMapper;
    private CustomerDao customerDao;


    private OnsaleDao onsaleDao;

    @Autowired
    public ShareDao(RedisUtil redisUtil, SharePoMapper sharePoMapper, CustomerDao customerDao, OnsaleDao onsaleDao) {
        this.redisUtil = redisUtil;
        this.sharePoMapper = sharePoMapper;
        this.customerDao = customerDao;
        this.onsaleDao = onsaleDao;
    }

    private Share getBo(SharePo po, String redisKey) {
        Share ret = cloneObj(po, Share.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Share bo) {
        bo.setOnsaleDao(this.onsaleDao);
        bo.setCustomerDao(this.customerDao);
    }


    /**
     * 根据id获得对象
     *
     * @date: 2022/12/21 20:42
     */
    public Share findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            Share bo = (Share) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<SharePo> retObj = this.sharePoMapper.findById(id);
        if (retObj.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        } else {
            SharePo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    public Share saveById(Share bo, UserDto userDto) {
        String key = String.format(KEY, bo.getId());
        SharePo po = cloneObj(bo, SharePo.class);
        putUserFields(po, "modifier", userDto);
        putGmtFields(po, "Modified");
        this.sharePoMapper.save(po);
        redisUtil.del(key);
        return bo;
    }


    /**
     * 顾客查询自己的分享记录
     * @date: 2022/12/21 22:31
     */
    public PageDto<Share> retrieveSharesByCustomerId(Long productId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, UserDto userDto) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<SharePo> pos = null;
        if (productId != null) {
            InternalReturnObject<Onsale> returnObject = onsaleDao.retrieveOnsales(productId,null);
            Onsale onsale = returnObject.getData();
            if (onsale == null) {
                return new PageDto<>(new ArrayList<>(), page, pageSize);
            }
            if (beginTime == null && endTime == null) {
                pos = sharePoMapper.findByOnsaleIdAndCustomerId(onsale.getId(), userDto.getId(), pageable);
            } else {
                pos = sharePoMapper.findByOnsaleIdAndCustomerIdAndGmtCreateBetween(
                        onsale.getId(),userDto.getId(),beginTime,endTime, pageable);
            }

        } else {
            if (beginTime != null && endTime != null) {
                pos = sharePoMapper.findByCustomerIdAndGmtCreateBetween(
                        userDto.getId(), beginTime, endTime, pageable);
            } else {
                pos = sharePoMapper.findByCustomerId(userDto.getId(), pageable);
            }
        }
        List<Share> ret = pos.stream().map(po->getBo(po,String.format(KEY,po.getId()))).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }



    public PageDto<Share> retrieveSharesByShopId(Long shopId, Long productId, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        InternalReturnObject<Onsale> returnObject = onsaleDao.retrieveOnsales(productId, shopId);
        if(returnObject.getData()==null){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }
        Page<SharePo> pos = sharePoMapper.findByOnsaleId(returnObject.getData().getId(), pageable);
        List<Share> ret = pos.stream().map(po->cloneObj(po,Share.class)).collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }


    public List<Share> retrieveByOnsaleId(Long onsaleId, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<SharePo> pos = sharePoMapper.findByOnsaleId(onsaleId, pageable);
        if(pos==null){
            return new ArrayList<>();
        }
        return pos.stream().map(po->cloneObj(po,Share.class)).collect(Collectors.toList());
    }

}
