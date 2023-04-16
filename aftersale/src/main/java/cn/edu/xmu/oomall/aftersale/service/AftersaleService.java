package cn.edu.xmu.oomall.aftersale.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.vo.ConsigneeVo;
import cn.edu.xmu.oomall.aftersale.dao.AftersaleDao;
import cn.edu.xmu.oomall.aftersale.dao.HistoryDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.Aftersale;
import cn.edu.xmu.oomall.aftersale.dao.bo.History;
import cn.edu.xmu.oomall.aftersale.dao.bo.OrderItem;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.OrderItemDao;
import cn.edu.xmu.oomall.aftersale.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class AftersaleService {
    private final Logger logger = LoggerFactory.getLogger(AftersaleService.class);

    private AftersaleDao aftersaleDao;

    private HistoryDao historyDao;

    private OrderItemDao orderItemDao;

    @Autowired
    public AftersaleService(AftersaleDao aftersaleDao, HistoryDao historyDao, OrderItemDao orderItemDao) {
        this.aftersaleDao = aftersaleDao;
        this.historyDao = historyDao;
        this.orderItemDao = orderItemDao;
    }

    /**
     * 顾客查询自己的所有售后单
     *
     * @date: 2022/12/22 16:22
     */
    public PageDto<SimpleAftersaleDto> retrieveAftersales(Byte status, Integer page, Integer pageSize, UserDto userDto) {
        List<Aftersale> bos = aftersaleDao.retrieveByCustomerId(status, page, pageSize, userDto);
        List<SimpleAftersaleDto> ret = bos.stream()
                .map(bo -> cloneObj(bo, SimpleAftersaleDto.class))
                .collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }


    /**
     * 顾客根据id查询自己的售后单
     *
     * @date: 2022/12/22 16:22
     */
    public AftersaleDto findAftersaleById(Long id, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //不是顾客的售后单
        if (!userDto.getId().equals(bo.getCustomerId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        AftersaleDto dto = cloneObj(bo, AftersaleDto.class);
        dto.setCustomer(cloneObj(bo.getCustomer(), SimpleAdminUserDto.class));
        dto.setConsignee(new ConsigneeDto(bo.getName(),bo.getMobile(),bo.getRegionId(),bo.getAddress()));
        dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName()));
        dto.setModifier(new SimpleAdminUserDto(bo.getModifierId(), bo.getModifierName()));
        dto.setProduct(cloneObj(bo.getProduct(), ProductNameDto.class));
        dto.setOrderItem(cloneObj(bo.getOrderItem(), SimpleOrderItemDto.class));
        dto.setShop(cloneObj(bo.getShop(), SimpleShopDto.class));
        return dto;
    }


    /**
     * 顾客修改售后单信息
     *
     * @date: 2022/12/22 16:22
     */
    public ReturnObject updataAftersale(Long id, Integer quantity, String reason, ConsigneeVo consignee, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //不是顾客的售后单
        if (!userDto.getId().equals(bo.getCustomerId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        bo.setQuantity(quantity);
        bo.setReason(reason);
        bo.setName(consignee.getName());
        bo.setMobile(consignee.getMobile());
        bo.setRegionId(consignee.getRegionId());
        bo.setAddress(consignee.getAddress());
        bo.update(userDto);
        return new ReturnObject();
    }


    public AftersaleDto createAftersale(Long orderItemId, Byte type, Integer quantity, String reason, ConsigneeDto consignee, UserDto userDto) {
        Aftersale ret = aftersaleDao.createAftersale(type,quantity,reason,consignee.getName(),
                consignee.getMobile(), orderItemId, userDto.getId(), userDto.getName());
        return cloneObj(ret, AftersaleDto.class);
    }


    /**
     * 买家取消售后单
     *
     * @date: 2022/12/22 16:30
     */
    public ReturnObject deleteAftersale(Long id, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //售后单不属于买家
        if(!bo.getCustomerId().equals(userDto.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        //不处于申请态和处理态
        if(!bo.getStatus().equals(Aftersale.NEW)&&!bo.getStatus().equals(Aftersale.PROCESS)){
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage(),"售后单",id,bo.getStatusName()));
        }
        bo.cancel(userDto);
        return new ReturnObject();
    }


    /**
     * 管理员查看所有售后单
     *
     * @date: 2022/12/22 16:30
     */
    public PageDto<SimpleAftersaleDto> retrieveAllAftersales(Long shopId, LocalDateTime beginTime, LocalDateTime endTime,
                                                             Integer page, Integer pageSize, Byte type, Byte state, UserDto userDto) {
        List<Aftersale> bos = aftersaleDao.retrieveByShopId(shopId, beginTime, endTime, page, pageSize, type, state, userDto);
        List<SimpleAftersaleDto> ret = bos.stream().map(bo -> cloneObj(bo, SimpleAftersaleDto.class)).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }


    /**
     * 管理员根据售后单id查询售后单信息
     *
     * @date: 2022/12/22 16:33
     */
    public AftersaleDto findAftersaleById(Long shopId, Long id, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //不为该商铺的售后单
        if (shopId != 0 && !userDto.getId().equals(bo.getShopId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        AftersaleDto dto = cloneObj(bo, AftersaleDto.class);
        dto.setCustomer(cloneObj(bo.getCustomer(), SimpleAdminUserDto.class));
        dto.setCreator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName()));
        dto.setConsignee(new ConsigneeDto(bo.getName(),bo.getMobile(),bo.getRegionId(),bo.getAddress()));
        dto.setModifier(new SimpleAdminUserDto(bo.getModifierId(), bo.getModifierName()));
        dto.setProduct(cloneObj(bo.getProduct(), ProductNameDto.class));
        dto.setOrderItem(cloneObj(bo.getOrderItem(), SimpleOrderItemDto.class));
        dto.setShop(cloneObj(bo.getShop(), SimpleShopDto.class));
        return dto;
    }


    /**
     * 管理员审核同意/不同意（退款，换货，维修）
     *
     * @date: 2022/12/22 16:34
     */
    public ReturnObject confirmAftersales(Long shopId, Long id, boolean confirm, String conclusion,
                                          Byte type, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //售后单不属于该商铺
        if(!shopId.equals(bo.getShopId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        bo.setType(type);
        bo.setConclusion(conclusion);
        aftersaleDao.saveById(bo, userDto);
        bo = aftersaleDao.findById(id);
        bo.confirmAftersale(confirm, userDto);
        aftersaleDao.saveById(bo,userDto);
        return new ReturnObject();
    }


    /**
     * 店家验收买家的退（换）货 维修不可调用此API
     *
     * @date: 2022/12/22 16:35
     */
    public ReturnObject shopConfirmReceive(Long shopId, Long id, boolean confirm, String conclusion,
                                           String serialNo, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //不是该商铺的售后单
        if(!shopId.equals(bo.getShopId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        bo.setConclusion(conclusion);
        bo.setSerialNo(serialNo);
        bo.confirmReceive(confirm);
        aftersaleDao.saveById(bo,userDto);
        return new ReturnObject();
    }


    /**
     * 过售后单id查询售后轨迹
     *
     * @date: 2022/12/22 16:40
     */
    public PageDto<HistoryDto> retrieveHistory(Long id, Integer page, Integer pageSize, UserDto userDto) {
        Aftersale aftersale = aftersaleDao.findById(id);
        //售后单id不属于该用户
        if (!userDto.getId().equals(aftersale.getCustomerId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        List<History> bos = historyDao.retrieveByAftersaleId(id, page, pageSize);
        List<HistoryDto> ret = bos.stream().map(bo -> cloneObj(bo, HistoryDto.class)).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }


    /**
     * 店铺查询售后的服务单信息
     *
     * @date: 2022/12/22 16:41
     */
    public ServiceDto retrieveService(Long shopId, Long id, UserDto userDto) {
        Aftersale bo = aftersaleDao.findById(id);
        //不为该商铺的售后单
        if (shopId != 0 && !userDto.getDepartId().equals(bo.getShopId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        }
        cn.edu.xmu.oomall.aftersale.dao.bo.Service service = bo.getService();
        if (service == null) {
            return null;
        }
        return cloneObj(service, ServiceDto.class);
    }


    public PageDto<SimpleOrderItemDto> retriveOrderItemsByCustomerId(Integer page, Integer pageSize, UserDto userDto) {
        if (null == userDto || null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN, ReturnNo.AUTH_NEED_LOGIN.getMessage());
        } else {
            List<OrderItem> orderItems = orderItemDao.getAftersalesOrderitemsByCustomerId(userDto.getId(), page, pageSize).getData();
            List<SimpleOrderItemDto> ret = orderItems.stream().map(bo -> cloneObj(bo, SimpleOrderItemDto.class)).collect(Collectors.toList());
            return new PageDto<>(ret, page, pageSize);
        }
    }


    /**
     * [发出换货订单后查询订单状态:已完成]
     * @author: 兰文强
     * @date: 2023/1/4 19:33
     */
    public void orderQuery(Long orderId, Byte status){
        aftersaleDao.updateByOrder(orderId, status);
    }

    /**
     * 分派后查询服务:已分派
     * @author: 兰文强
     * @date: 2023/1/4 20:05
     */
    public void serviceQuery(Long serviceId, Byte status){
        aftersaleDao.updateByService(serviceId, status);
    }


}
