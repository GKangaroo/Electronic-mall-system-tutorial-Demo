package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
import cn.edu.xmu.oomall.customer.service.dto.CartDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleActivityDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

/**
 * @author: 兰文强
 * @date: 2022/12/18 19:28
 */
@Service
public class CartService {

    private static  final Logger logger = LoggerFactory.getLogger(CartService.class);

    private CartDao cartDao;

    private ProductDao productDao;

    private CouponActivityDao couponActivityDao;

    @Autowired
    public CartService(CartDao cartDao, ProductDao productDao, CouponActivityDao couponActivityDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.couponActivityDao = couponActivityDao;
    }

    /**
     * 买家获得购物车列表
     * @author: 兰文强
     * @date: 2022/12/18 22:11
     */
    public PageDto<CartDto> retrieveCarts(Integer page, Integer pageSize, UserDto userDto){
        List<Cart> bos = cartDao.retrieveCarts(page, pageSize, userDto).getList();
        //购物车为空
        if(bos==null||bos.size()==0) {
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }
        List<CartDto> ret = new ArrayList<>();
        bos.forEach(bo->{
            CartDto dto = cloneObj(bo,CartDto.class);
            dto.setProduct(cloneObj(bo.getProduct(), SimpleProductDto.class));
            ret.add(dto);
        });
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 买家将商品加入购物车
     * @author: 兰文强
     * @date: 2022/12/18 22:11
     */
    public CartDto addCart(Long productId, Integer quantity, UserDto userDto){
        Product product = productDao.getSimpleProductById(productId).getData();
        //团购预售商品不能加入购物车
        if (product.getActList()!=null){
            if(product.getActList().stream().anyMatch(act-> act.getType().equals((byte)1)||act.getType().equals((byte)2)))
                throw new BusinessException(ReturnNo.CUSTOMER_MOBILEDIFF);
        }

        Cart bo = Cart.builder()
                .productId(productId)
                .quantity(quantity)
                .price(product.getPrice())
                .customerId(userDto.getId())
                .couponActivityList(couponActivityDao.getCouponActivityByProductId(productId).getData())
                .product(product).build();
        bo = cartDao.save(bo, userDto);
        CartDto dto = cloneObj(bo, CartDto.class);
        dto.setProduct(cloneObj(bo.getProduct(), SimpleProductDto.class));
        dto.setActivities(bo.getCouponActivityList()==null?new ArrayList<>():bo.getCouponActivityList().stream().map(act->cloneObj(act, SimpleActivityDto.class)).collect(Collectors.toList()));
        return dto;
    }

    /**
     * 买家清空购物车
     * @author: 兰文强
     * @date: 2022/12/18 22:24
     */
    public ReturnObject deleteCarts(UserDto userDto){
        return cartDao.deleteCarts(userDto);
    }

    /**
     * 买家修改购物车单个商品的数量或规格
     * @author: 兰文强
     * @date: 2022/12/18 22:24
     */
    public ReturnObject updateCart(Long id, Long productId, Integer quantity, UserDto userDto){
        Cart bo = cartDao.findById(id);
        bo.setProductId(productId);
        bo.setQuantity(quantity);
        bo.setPrice(bo.getProduct().getPrice());
        return cartDao.saveById(bo,userDto);
    }

    /**
     * 买家删除购物车中商品
     * @author: 兰文强 
     * @date: 2022/12/18 22:25
     */
    public ReturnObject deleteCart(Long id, UserDto userDto){
        return cartDao.deleteById(id,userDto);
    }



}
