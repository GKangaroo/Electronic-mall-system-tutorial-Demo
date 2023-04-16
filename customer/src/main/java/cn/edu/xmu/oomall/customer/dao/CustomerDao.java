package cn.edu.xmu.oomall.customer.dao;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


import static cn.edu.xmu.javaee.core.util.Common.*;


@Repository
public class CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDao.class);

    public static final String KEY = "CU%d";
    public static final String CAPTCHA_KEY = "CAP%s";

//    @Value("${oomall.customer.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private CustomerPoMapper customerPoMapper;

    @Autowired
    public CustomerDao(RedisUtil redisUtil, CustomerPoMapper customerPoMapper) {
        this.redisUtil = redisUtil;
        this.customerPoMapper = customerPoMapper;
    }

    private Customer getBo(CustomerPo po, String redisKey){
        Customer ret = cloneObj(po,Customer.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Customer bo){

    }


    public Customer findById(Long id) throws RuntimeException{
        logger.debug("findById: id ={}",id);
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)){
            Customer bo = (Customer) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<CustomerPo> retObj = this.customerPoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        }else{
            CustomerPo po = retObj.get();
            return this.getBo(po, key);
        }
    }

    /**
     * 注册用户
     * @author: 兰文强 
     * @date: 2022/12/18 0:08
     */
    //insert
    public Customer insert(Customer bo) throws RuntimeException{
        //用户名相同
        CustomerPo customerPo = customerPoMapper.findByUserName(bo.getUserName());
        if(customerPo!=null){
            throw new BusinessException(ReturnNo.CUSTOMER_NAMEEXIST, ReturnNo.CUSTOMER_NAMEEXIST.getMessage());
        }
        //电话相同
        customerPo = customerPoMapper.findByMobile(bo.getMobile());
        if(customerPo!=null){
            throw new BusinessException(ReturnNo.CUSTOMER_MOBILEEXIST, ReturnNo.CUSTOMER_MOBILEEXIST.getMessage());
        }
        CustomerPo po = cloneObj(bo,CustomerPo.class);
        po.setCreatorId(1L);
        po.setCreatorName("admin");
        putGmtFields(po, "create");
        po = customerPoMapper.save(po);
        logger.debug("save: po = {}", po);
        return cloneObj(po,Customer.class);
    }

    /**
     * 修改用户信息
     * @author: 兰文强
     * @date: 2022/12/18 20:31
     */
    //save
    public void save(Customer bo, UserDto userDto){
        String key = String.format(KEY, bo.getId());
        CustomerPo po = cloneObj(bo, CustomerPo.class);
        putUserFields(po, "modifier", userDto);
        putGmtFields(po, "Modified");
        this.customerPoMapper.save(po);
        redisUtil.del(key);
    }

    /**
     * 用户修改自己的信息
     * @author: 兰文强
     * @date: 2022/12/24 18:19
     */
    public void updateCustomer(Customer bo, UserDto userDto){
        CustomerPo customerPo = customerPoMapper.findByUserName(bo.getUserName());
        //用户名相同
        if(customerPo!=null&& !customerPo.getId().equals(userDto.getId())){
            throw new BusinessException(ReturnNo.CUSTOMER_NAMEEXIST, ReturnNo.CUSTOMER_NAMEEXIST.getMessage());
        }
        customerPo = customerPoMapper.findByMobile(bo.getMobile());
        //电话相同
        if(customerPo!=null&&!customerPo.getId().equals(userDto.getId())){
            throw new BusinessException(ReturnNo.CUSTOMER_MOBILEEXIST, ReturnNo.CUSTOMER_MOBILEEXIST.getMessage());
        }
        this.save(bo, userDto);
    }

    //生成6位验证码
    private String rand(){
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=1;i<6;i++) {
            builder.append(r.nextInt(10));
        }
        return builder.toString();
    }


    /**
     * 获取验证码
     * @author: 兰文强
     * @date: 2022/12/18 20:31
     */
    public String resetPassword(String mobile){
        String key = String.format(CAPTCHA_KEY, mobile);
        String captcha = rand();
        redisUtil.set(key, captcha,300);
        return captcha;
    }


    /**
     * 修改密码
     * @author: 兰文强
     * @date: 2022/12/18 20:31
     */
    public void updatePassword(String captcha, String newPassword, UserDto userDto) throws BusinessException{
        Customer bo = findById(userDto.getId());
        String key = String.format(CAPTCHA_KEY, bo.getMobile());
        Serializable serializable = redisUtil.get(key);
        //验证码错误，暂时用这个错误代替
//        if(serializable==null||!serializable.equals(captcha)){
//            throw new BusinessException(ReturnNo.CUSTOMER_PASSWORDSAME,ReturnNo.CUSTOMER_PASSWORDSAME.getMessage());
//        }
        //与旧密码相同
        if(newPassword.equals(bo.getPassword())){
            throw new BusinessException(ReturnNo.CUSTOMER_PASSWORDSAME,ReturnNo.CUSTOMER_PASSWORDSAME.getMessage());
        }
        bo.setPassword(newPassword);
        this.save(bo,userDto);
    }

    /**
     * 查询所有用户
     * @author: 兰文强
     * @date: 2022/12/18 20:42
     */
    public PageDto<Customer> retrieveCustomers(String userName, String name, String mobile,
                                               Integer page, Integer pageSize){
        Page<CustomerPo> ret = null;
        Pageable pageable = PageRequest.of(page - 1,pageSize);

        //这是一个十分繁琐的条件是否存在问题
        if(null!=userName&&null!=name&&null!=mobile)
            ret = customerPoMapper.findByUserNameAndNameAndMobile(userName,name,mobile,pageable);
        else if(null!=userName&&null!=name&&null==mobile)
            ret = customerPoMapper.findByUserNameAndName(userName,name,pageable);
        else if(null!=userName&&null==name&&null!=mobile)
            ret = customerPoMapper.findByNameAndMobile(name,mobile,pageable);
        else if(null==userName&&null!=name&&null!=mobile)
            ret = customerPoMapper.findByNameAndMobile(name,mobile,pageable);
        else if(null==userName&&null==name&&null!=mobile)
            ret = customerPoMapper.findByMobile(mobile,pageable);
        else if(null==userName&&null!=name&&null==mobile)
            ret = customerPoMapper.findByName(name,pageable);
        else if(null!=userName&&null==name&&null==mobile)
            ret = customerPoMapper.findByUserName(userName,pageable);
        else if(null==userName&&null==name&&null==mobile)
            ret = customerPoMapper.findAll(pageable);

        if(null == ret){
            return new PageDto<>(new ArrayList<>(),page,pageSize);
        }

        List<Customer> bos = ret.stream().map(po->cloneObj(po,Customer.class))
                .skip((long) (page - 1) * pageSize).limit(pageSize)
                .collect(Collectors.toList());
        return new PageDto<>(bos,page,pageSize);
    }


    /**
     * 用户登出
     * @author: 兰文强
     * @date: 2023/1/5 15:08
     */
    public void Logout(UserDto userDto){
        String key = String.format(KEY, userDto.getId());
        redisUtil.del(key);
    }
    
    /**
     * 根据用户名查找用户
     * @author: 兰文强
     * @date: 2022/12/18 21:35
     */
    public Customer findByUserName(String userName) throws BusinessException{
        CustomerPo po = customerPoMapper.findByUserName(userName);
        if(po==null){
            throw new BusinessException(ReturnNo.CUSTOMER_INVALID_ACCOUNT,ReturnNo.CUSTOMER_INVALID_ACCOUNT.getMessage());
        }

        return getBo(po,String.format(KEY,po.getId()));
    }


    /**
     * 逻辑删除买家
     * @author: 兰文强
     * @date: 2022/12/18 21:35
     */
    public ReturnObject deleteCustomer(Long id, UserDto userDto){
        Customer bo = this.findById(id);
        bo.setBeDeleted((byte) 1);
        this.save(bo,userDto);
        return new ReturnObject();
    }

    /**
     * 封禁买家
     * @author: 兰文强
     * @date: 2022/12/18 21:36
     */
    public ReturnObject banCustomer(Long id ,UserDto userDto){
        Customer bo = this.findById(id);
        bo.setStatus((byte) 1);
        this.save(bo,userDto);
        return new ReturnObject();
    }

    /**
     * 解禁买家
     * @author: 兰文强
     * @date: 2022/12/18 21:36
     */
    public ReturnObject releaseCustomer(Long id ,UserDto userDto){
        Customer bo = this.findById(id);
        bo.setStatus((byte) 0);
        this.save(bo,userDto);
        return new ReturnObject();
    }



}



