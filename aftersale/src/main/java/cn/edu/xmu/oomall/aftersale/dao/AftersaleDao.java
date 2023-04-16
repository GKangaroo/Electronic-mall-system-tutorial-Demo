package cn.edu.xmu.oomall.aftersale.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.aftersale.dao.bo.AftersaleFactory;
import cn.edu.xmu.oomall.aftersale.dao.bo.OrderItem;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import cn.edu.xmu.oomall.aftersale.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.po.AftersalePo;
import cn.edu.xmu.oomall.aftersale.mapper.po.OrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.oomall.aftersale.dao.bo.Aftersale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class AftersaleDao {

    private static final Logger logger = LoggerFactory.getLogger(AftersaleDao.class);

    public static final String KEY = "AF%d";


    @Value("3600")
    private int timeout;

    private RedisUtil redisUtil;
    private AftersalePoMapper aftersalePoMapper;
    private OrderPoMapper orderPoMapper;
    private ProductDao productDao;
    private ShopDao shopDao;
    private OrderItemDao orderItemDao;
    private CustomerDao customerDao;
    private ServiceDao serviceDao;


    @Autowired
    public AftersaleDao(RedisUtil redisUtil, AftersalePoMapper aftersalePoMapper, OrderPoMapper orderPoMapper, ProductDao productDao, ShopDao shopDao, OrderItemDao orderItemDao, CustomerDao customerDao, ServiceDao serviceDao) {
        this.redisUtil = redisUtil;
        this.aftersalePoMapper = aftersalePoMapper;
        this.orderPoMapper = orderPoMapper;
        this.productDao = productDao;
        this.shopDao = shopDao;
        this.orderItemDao = orderItemDao;
        this.customerDao = customerDao;
        this.serviceDao = serviceDao;
    }

    //获取bo对象
    private Aftersale getBo(AftersalePo po, String redisKey) {
        Aftersale ret = AftersaleFactory.createAftersale(po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Aftersale bo) {
        bo.setOrderItemDao(this.orderItemDao);
        bo.setProductDao(this.productDao);
        bo.setShopDao(this.shopDao);
        bo.setCustomerDao(this.customerDao);
        bo.setServiceDao(this.serviceDao);
        bo.setAftersaleDao(this);
    }


    /**
     * 根据id获取对象
     *
     * @date: 2022/12/22 17:01
     */
    public Aftersale findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            Aftersale bo = (Aftersale) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<AftersalePo> retObj = this.aftersalePoMapper.findById(id);
        if (retObj.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        } else {
            AftersalePo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    /**
     * 修改售后单
     *
     * @date: 2022/12/22 17:42
     */
    public Aftersale saveById(Aftersale bo, UserDto userDto) {
        String key = String.format(KEY, bo.getId());
        AftersalePo po = cloneObj(bo, AftersalePo.class);
        putUserFields(po, "modifier", userDto);
        putGmtFields(po, "Modified");
        AftersalePo ret = this.aftersalePoMapper.save(po);
        redisUtil.del(key);
        return cloneObj(ret, Aftersale.class);
    }


    /**
     * 买家取消售后单
     *
     * @date: 2022/12/22 17:45
     */
    public void cancelById(Long id, UserDto userDto) {
        Aftersale bo = this.findById(id);
        if (!userDto.getId().equals(bo.getCustomerId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        if (bo.getStatus() != (byte) 0 && bo.getStatus() != (byte) 1) {
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage(), "售后单", id, bo.getStatus()));
        }
        bo.setStatus(Aftersale.CANCEL);
        this.saveById(bo, userDto);
    }


    /**
     * 根据用户id查询售后单
     *
     * @date: 2022/12/22 17:11
     */
    public List<Aftersale> retrieveByCustomerId(Byte status, Integer page, Integer pageSize, UserDto userDto) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<AftersalePo> pos = null;
        if (status != null)
            pos = aftersalePoMapper.findByCustomerIdAndStatus(userDto.getId(), status, pageable);
        else
            pos = aftersalePoMapper.findByCustomerId(userDto.getId(), pageable);
        if (pos == null)
            return new ArrayList<>();
        return pos.stream().map(po -> getBo(po, String.format(KEY,po.getId()))).collect(Collectors.toList());
    }

    /**
     * 商铺查询售后单
     *
     * @author: 兰文强
     * @date: 2022/12/22 20:32
     */
    public List<Aftersale> retrieveByShopId(Long shopId, LocalDateTime beginTime, LocalDateTime endTime,
                                            Integer page, Integer pageSize, Byte type, Byte state, UserDto userDto) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<AftersalePo> pos = null;
        //如果是平台管理员，则能查询所有售后单
        if(0==shopId)
            pos = aftersalePoMapper.findByGmtCreateBetweenAndTypeAndStatus(beginTime,endTime,type,state,pageable);
        //商铺管理员只能查询本商铺的售后单
        else
            pos = aftersalePoMapper.findByShopIdAndGmtCreateBetweenAndTypeAndStatus(shopId, beginTime, endTime, type, state, pageable);
        return pos.stream().map(po -> getBo(po, null)).collect(Collectors.toList());

    }

    public List<Aftersale> retrieveAftersaleByShopId(Long shopId, int page, int pageSize) {
        List<Aftersale> aftersaleList = null;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<AftersalePo> pageObj = aftersalePoMapper.findByShopIdEquals(shopId, pageable);
        if (!pageObj.isEmpty()) {
            aftersaleList = pageObj.stream()
                    .map(po -> cloneObj(po, Aftersale.class))
                    .collect(Collectors.toList());
        } else {
            aftersaleList = new ArrayList<>();
        }
        return aftersaleList;
    }


    /**
     * 买家创建售后单
     *
     * @author: 陈昊
     * @date: 2022/12/23 14:25
     */
    public Aftersale createAftersale(Byte type, Integer quantity, String reason, String name,
            String mobile, Long orderItemId, Long creatorId, String creatorName) {
        AftersalePo po = AftersalePo.builder().type(type).quantity(quantity).reason(reason).name(name).mobile(mobile)
                .build();
        po.setOrderItemId(orderItemId);
        po.setCreatorId(creatorId);
        po.setCreatorName(creatorName);
        AftersalePo ret = aftersalePoMapper.save(po);
        return cloneObj(ret, Aftersale.class);
    }

    /**
     * 根据查询售后订单的状态更新售后单的状态
     * @author: 兰文强
     * @date: 2023/1/4 19:37
     */
    public void updateByOrder(Long orderId, Byte status){
        Optional<OrderPo> optional = orderPoMapper.findByOrderId(orderId);
        if(optional.isPresent()){
            Long aftersaleId = optional.get().getAftersaleId();
            Aftersale bo = this.findById(aftersaleId);
            bo.query(status);
            this.saveById(bo, new UserDto());
        }
    }

    /**
     * 根据查询服务单的状态更新售后单的状态
     * @author: 兰文强 
     * @date: 2023/1/4 20:09
     */
    public void updateByService(Long serviceId, Byte status){
        AftersalePo po = aftersalePoMapper.findByServiceId(serviceId);
        //修改状态为处理中
        Aftersale bo = this.getBo(po, String.format(KEY, po.getId()));
        bo.query(status);
        this.saveById(bo,new UserDto());
    }

}
