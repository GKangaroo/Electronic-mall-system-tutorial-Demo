package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.controller.vo.NewCustomerVo;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.service.dto.CustomerDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleAdminUserDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleCustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;


/**
 * @author: 兰文强 
 * @date: 2022/12/16 22:42
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 注册用户
     * @author: 兰文强
     * @date: 2022/12/18 0:26
     */
    public CustomerDto createCustomer(NewCustomerVo customer){
        Customer bo = cloneObj(customer, Customer.class);
        bo = customerDao.insert(bo);
        return CustomerDto.builder().id(bo.getId()).point(bo.getPoint()).invalid(bo.getBeDeleted())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified()).userName(bo.getUserName())
                .name(bo.getName()).creator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName())).build();
    }

    /**
     * 买家查询自己信息
     * @author: 兰文强
     * @date: 2022/12/18 0:27
     */
    public CustomerDto findCustomerSelf(UserDto userDto){
        Customer bo = customerDao.findById(userDto.getId());
        return CustomerDto.builder().id(bo.getId()).point(bo.getPoint()).invalid(bo.getBeDeleted())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified()).userName(bo.getUserName())
                .name(bo.getName()).creator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName())).build();
    }

    /**
     * 买家修改自己信息
     * @author: 兰文强
     * @date: 2022/12/18 0:27
     */
    public ReturnObject updateCustomer(String name, String mobile, UserDto userDto){
        Customer bo = customerDao.findById(userDto.getId());
        bo.setName(name);
        bo.setMobile(mobile);
        customerDao.updateCustomer(bo,userDto);
        return new ReturnObject();
    }

    /**
     * 买家修改密码
     * @author: 兰文强
     * @date: 2022/12/18 0:28
     */
    public ReturnObject updatePassword(String captcha, String newPassword, UserDto userDto){
        customerDao.updatePassword(captcha,newPassword,userDto);
        return null;
    }

    /**
     * 发送验证码
     * @author: 兰文强
     * @date: 2022/12/18 0:32
     */
    public ReturnObject resetPassword(String userName, String mobile, UserDto userDto){
        Customer bo = customerDao.findById(userDto.getId());
        //电话号码与预留不一致
        if(!bo.getMobile().equals(mobile)){
            throw new BusinessException(ReturnNo.CUSTOMER_MOBILEDIFF,ReturnNo.CUSTOMER_MOBILEDIFF.getMessage());
        }
        String captcha = customerDao.resetPassword(mobile);
        return new ReturnObject();
    }


    /**
     * 平台管理员获取所有用户列表
     * @author: 兰文强 
     * @date: 2022/12/18 0:33
     */
    public PageDto<SimpleCustomerDto> retrieveCustomers(Long shopId, String userName, String name, String mobile,
                                                        Integer page, Integer pageSize, UserDto userDto){
        List<Customer> bos = customerDao.retrieveCustomers(userName, name, mobile, page, pageSize).getList();
        List<SimpleCustomerDto> ret = bos.stream().map(bo->cloneObj(bo,SimpleCustomerDto.class)).
                collect(Collectors.toList());
        return new PageDto<>(ret,page,pageSize);
    }


    /**
     * 用户名密码登录
     * @author: 兰文强 
     * @date: 2022/12/18 0:33
     */
    public String customerLogin(String userName, String password){
        Customer bo = customerDao.findByUserName(userName);
        //密码错误
        if(!password.equals(bo.getPassword())){
            throw new BusinessException(ReturnNo.CUSTOMER_INVALID_ACCOUNT,ReturnNo.CUSTOMER_INVALID_ACCOUNT.getMessage());
        }
        //用户被静止登陆
        if(bo.getStatus()!=0||bo.getBeDeleted()!=0){
            throw new BusinessException(ReturnNo.CUSTOMER_FORBIDDEN,ReturnNo.CUSTOMER_FORBIDDEN.getMessage());
        }
        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(bo.getId(), userName,2L,2,3600);
    }


    /**
     * 用户登出
     * @author: 兰文强
     * @date: 2022/12/18 0:33
     */
    public ReturnObject customerLogout(UserDto userDto){
        customerDao.Logout(userDto);
        return new ReturnObject();
    }



    /**
     * 管理员查看任意买家信息 ?
     * @author: 兰文强
     * @date: 2022/12/18 0:49
     */
    public CustomerDto findCustomerById(Long id){
        Customer bo = customerDao.findById(id);
        return CustomerDto.builder().id(bo.getId()).point(bo.getPoint()).invalid(bo.getBeDeleted())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified()).userName(bo.getUserName())
                .name(bo.getName()).creator(new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName())).build();
    }


    /**
     * 管理员逻辑删除顾客
     * @author: 兰文强
     * @date: 2022/12/18 0:50
     */
    public ReturnObject deleteCustomer(Long shopId, Long customerId, UserDto userDto){
        return customerDao.deleteCustomer(customerId,userDto);
    }

    /**
     * 平台管理员封禁买家
     * @author: 兰文强
     * @date: 2022/12/18 0:52
     */
    public ReturnObject banCustomer(Long shopId, Long customerId, UserDto userDto){
        return customerDao.banCustomer(customerId,userDto);
    }

    /**
     * 平台管理员解禁买家
     * @author: 兰文强
     * @date: 2022/12/18 0:52
     */
    public ReturnObject releaseCustomer(Long shopId, Long customerId, UserDto userDto){
        return  customerDao.releaseCustomer(customerId,userDto);
    }





}
