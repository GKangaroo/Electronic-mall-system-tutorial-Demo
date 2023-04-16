package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.AddressPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class AddressDao {

    private static final Logger logger = LoggerFactory.getLogger(AddressDao.class);

    public static final String KEY = "AD%d";
//    @Value("${oomall.address.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private AddressPoMapper addressPoMapper;

    @Autowired
    public AddressDao(RedisUtil redisUtil, AddressPoMapper addressPoMapper) {
        this.redisUtil = redisUtil;
        this.addressPoMapper = addressPoMapper;
    }

    private Address getBo(AddressPo po, String redisKey){
        Address ret = cloneObj(po,Address.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Address bo){

    }


    /**
     * 根据id获取对象
     * @date: 2022/12/21 18:26
     */
    public Address findById(Long id) throws RuntimeException{
        logger.debug("findById: id ={}",id);
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)){
            Address bo = (Address) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<AddressPo> retObj = this.addressPoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        }else{
            AddressPo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    /**
     * 新增地址
     * @date: 2022/12/21 18:31
     */
    public Address save(Address bo, UserDto userDto) throws BusinessException{
        List<AddressPo> pos = addressPoMapper.findByCustomerId(userDto.getId());
        if(pos.size()>=20){
            throw new BusinessException(ReturnNo.ADDRESS_OUTLIMIT,ReturnNo.ADDRESS_OUTLIMIT.getMessage());
        }
        AddressPo po = cloneObj(bo,AddressPo.class);
        putUserFields(po, "creator",userDto);
        putGmtFields(po, "create");
        po = addressPoMapper.save(po);
        return cloneObj(po,Address.class);
    }


    /**
     * 更新地址
     * @author: 兰文强
     * @date: 2022/12/21 19:15
     */
    public Address saveById(Address bo,UserDto userDto){
        AddressPo po = cloneObj(bo, AddressPo.class);
        if(userDto!=null) {
            putUserFields(po, "modifier", userDto);
            putGmtFields(po, "Modified");
        }
        logger.debug("saveById: po = {}", po);
        addressPoMapper.save(po);
        String key = String.format(KEY, bo.getId());
        redisUtil.del(key);
        return cloneObj(po,Address.class);
    }



    /**
     * 买家查询所有已有的地址信息
     * @date: 2022/12/21 18:34
     */
    public List<Address> retrieveByCustomerId(UserDto userDto){
        List<AddressPo> pos = addressPoMapper.findByCustomerId(userDto.getId());
        return pos.stream().map(po->cloneObj(po,Address.class)).collect(Collectors.toList());

    }


    /**
     * 买家删除地址
     * @date: 2022/12/21 18:37
     */
    public ReturnObject deleteById(Long id, UserDto userDto){
        Address bo = this.findById(id);
        if(!userDto.getId().equals(bo.getCustomerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"地址",id));
        }
        addressPoMapper.deleteById(id);
        return new ReturnObject();
    }

    /**
     * 设置默认地址
     * @author: 兰文强
     * @date: 2022/12/21 19:13
     */
    public ReturnObject setDefault(Long id, UserDto userDto){
        Address bo = this.findById(id);
        Optional<AddressPo> defaultBo = addressPoMapper.findByBeDefault(1);
        //将原默认地址设置为非默认地址
        if(defaultBo.isPresent()){
            AddressPo po = defaultBo.get();
            po.setBeDefault(0);
            this.save(cloneObj(po,Address.class),userDto);
        }
        bo.setBeDefault(1);
        this.saveById(bo,userDto);
        return new ReturnObject();
    }


}
