package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.dao.bo.Share;
import cn.edu.xmu.oomall.customer.dao.bo.SuccessShare;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OrderItemDao;
import cn.edu.xmu.oomall.customer.mapper.SuccessSharePoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import cn.edu.xmu.oomall.customer.mapper.po.SuccessSharePo;
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

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class SuccessShareDao {

    private static final Logger logger = LoggerFactory.getLogger(SuccessShareDao.class);

    public static final String KEY = "SU%d";
//    @Value("${oomall.successShare.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private SuccessSharePoMapper successSharePoMapper;

    private ShareDao shareDao;

    private OrderItemDao orderItemDao;

    private CustomerDao customerDao;

    private OnsaleDao onsaleDao;

    @Autowired
    public SuccessShareDao(RedisUtil redisUtil, SuccessSharePoMapper successSharePoMapper, ShareDao shareDao, OrderItemDao orderItemDao, CustomerDao customerDao, OnsaleDao onsaleDao) {
        this.redisUtil = redisUtil;
        this.successSharePoMapper = successSharePoMapper;
        this.shareDao = shareDao;
        this.orderItemDao = orderItemDao;
        this.customerDao = customerDao;
        this.onsaleDao = onsaleDao;
    }

    private SuccessShare getBo(SuccessSharePo po, String redisKey) {
        SuccessShare ret = cloneObj(po, SuccessShare.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(SuccessShare bo) {
        bo.setShareDao(this.shareDao);
        bo.setCustomerDao(this.customerDao);
        bo.setOrderItemDao(this.orderItemDao);
    }


    /**
     * 根据id获得对象
     *
     * @author: 兰文强
     * @date: 2022/12/21 23:01
     */
    public SuccessShare findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            SuccessShare bo = (SuccessShare) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<SuccessSharePo> retObj = this.successSharePoMapper.findById(id);
        if (retObj.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        } else {
            SuccessSharePo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    /**
     * 新增分享成功记录
     *
     * @date: 2022/12/21 23:04
     */
    public SuccessShare save(SuccessShare bo, UserDto userDto) {
        SuccessSharePo po = cloneObj(bo, SuccessSharePo.class);
        putUserFields(po, "creator", userDto);
        putGmtFields(po, "Create");
        po = successSharePoMapper.save(po);
        return cloneObj(po, SuccessShare.class);
    }


    public PageDto<SuccessShare> retrieveByShareId(Long shareId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<SuccessSharePo> pos = null;
        if (beginTime == null && endTime == null)
            pos = successSharePoMapper.findByShareId(shareId, pageable);
        else
            pos = successSharePoMapper.findByShareIdAndGmtCreateBetween(shareId, beginTime, endTime, pageable);
        if (pos == null) {
            return new PageDto<>(new ArrayList<>(), page, pageSize);
        }
        List<SuccessShare> bos = pos.stream().map(po -> getBo(po, String.format(KEY,po.getId()))).collect(Collectors.toList());
        return new PageDto<>(bos, page, pageSize);

    }


    public PageDto<SuccessShare> retrieveByShopId(Long shopId, Long onsaleId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<SuccessSharePo> pos = null;
        List<SuccessShare> ret = new ArrayList<>();
        List<Share> bos = shareDao.retrieveByOnsaleId(onsaleId, 1, MAX_RETURN);
        if (beginTime == null && endTime == null) {
            for (Share bo : bos) {
                pos = successSharePoMapper.findByShareIdAndGmtCreateBetween(bo.getId(), beginTime, endTime, pageable);
                if (pos != null)
                    ret.addAll(pos.stream().map(po -> cloneObj(po, SuccessShare.class)).collect(Collectors.toList()));
            }
        } else {
            for (Share bo : bos) {
                pos = successSharePoMapper.findByShareId(bo.getId(), pageable);
                if (pos != null)
                    ret.addAll(pos.stream().map(po -> getBo(po, String.format(KEY,po.getId()))).collect(Collectors.toList()));
            }
        }
        return new PageDto<>(ret, page, pageSize);

    }


}
