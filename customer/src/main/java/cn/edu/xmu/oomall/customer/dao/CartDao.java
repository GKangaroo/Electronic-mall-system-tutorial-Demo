package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.CartPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class CartDao {

    private static final Logger logger = LoggerFactory.getLogger(CartDao.class);
    public static final String KEY = "CA%d";

//    @Value("${oomall.cart.timeout}")
    private int timeout;

    private RedisUtil redisUtil;
    private ProductDao productDao;
    private CouponActivityDao couponActivityDao;
    private CartPoMapper cartPoMapper;

    @Autowired
    public CartDao(RedisUtil redisUtil, ProductDao productDao, CouponActivityDao couponActivityDao, CartPoMapper cartPoMapper) {
        this.redisUtil = redisUtil;
        this.productDao = productDao;
        this.couponActivityDao = couponActivityDao;
        this.cartPoMapper = cartPoMapper;
    }

    private Cart getBo(CartPo po, String redisKey){
        Cart ret = cloneObj(po,Cart.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Cart bo){
        bo.setProductDao(this.productDao);
        bo.setCouponActivityDao(this.couponActivityDao);
    }

    /**
     * 根据id获得对象
     * @author: 兰文强
     * @date: 2022/12/18 22:38
     */
    public Cart findById(Long id) throws RuntimeException{
        logger.debug("findById: id ={}",id);
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)){
            Cart bo = (Cart) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<CartPo> retObj = this.cartPoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        }else{
            CartPo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    /**
     * 新增购物车
     * @author: 兰文强
     * @date: 2022/12/18 23:00
     */
    public Cart save(Cart bo, UserDto userDto){
        CartPo po = cloneObj(bo, CartPo.class);
        putUserFields(po, "creator", userDto);
        putGmtFields(po, "create");
        logger.debug("save: po = {}", po);
        po = this.cartPoMapper.save(po);
        return getBo(po,String.format(KEY,bo.getId()));
    }


    /**
     * 根据用户信息查询购物车
     * @author: 兰文强
     * @date: 2022/12/18 22:44
     */
    public PageDto<Cart> retrieveCarts(Integer page, Integer pageSize, UserDto userDto){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        List<Cart> ret = cartPoMapper.findByCustomerId(userDto.getId(), pageable).stream()
                .map(po->getBo(po,String.format(KEY,po.getId())))
                .collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 根据用户信息删除购物车
     * @author: 兰文强
     * @date: 2022/12/18 23:08
     */
    public ReturnObject deleteCarts(UserDto userDto){
        cartPoMapper.deleteByCustomerId(userDto.getId());
        return new ReturnObject();
    }

    /**
     * 根据id修改购物车(需要删除redis)
     * @author: 兰文强
     * @date: 2022/12/18 23:09
     */
    public ReturnObject saveById(Cart bo, UserDto userDto){
        String key = String.format(KEY, bo.getId());
        CartPo po = cloneObj(bo, CartPo.class);
        if (null != userDto) {
            putUserFields(po, "modifier", userDto);
            putGmtFields(po, "Modified");
        }
        logger.debug("saveById: po = {}", po);
        this.cartPoMapper.save(po);
        redisUtil.del(key);
        return new ReturnObject();
    }

    /**
     * 根据id删除购物车
     * @author: 兰文强
     * @date: 2022/12/18 23:14
     */
    public ReturnObject deleteById(Long id, UserDto userDto){
        Cart bo = this.findById(id);
        if(!bo.getCustomerId().equals(userDto.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"购物车",id));
        }
        cartPoMapper.deleteById(id);
        return new ReturnObject();
    }


}
