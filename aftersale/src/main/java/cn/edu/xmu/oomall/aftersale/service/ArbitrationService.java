package cn.edu.xmu.oomall.aftersale.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.dao.AftersaleDao;
import cn.edu.xmu.oomall.aftersale.dao.ArbitrationDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.Arbitration;
import cn.edu.xmu.oomall.aftersale.service.dto.SimpleArbitrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.xmu.oomall.aftersale.service.dto.ArbitrationDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class ArbitrationService {

    private static final Logger logger = LoggerFactory.getLogger(ArbitrationService.class);

    private ArbitrationDao arbitrationDao;

    private AftersaleDao aftersaleDao;

    @Autowired
    public ArbitrationService(ArbitrationDao arbitrationDao, AftersaleDao aftersaleDao) {
        this.arbitrationDao = arbitrationDao;
        this.aftersaleDao = aftersaleDao;
    }

    @Transactional
    public ArbitrationDto findById(Long id, UserDto userDto) {
        if (null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN,
                    String.format(ReturnNo.AUTH_NEED_LOGIN.getMessage()));
        } else {
            Arbitration bo = arbitrationDao.findById(id);
            return cloneObj(bo, ArbitrationDto.class).getUser(bo);
        }
    }

    @Transactional
    public PageDto<SimpleArbitrationDto> retrieveUnacceptedArbitrations(Integer page, Integer pageSize, UserDto userDto) {
        if (null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN, ReturnNo.AUTH_NEED_LOGIN.getMessage());
        }
        List<SimpleArbitrationDto> ret = arbitrationDao.findUnacceptedArbitrations(page, pageSize)
                .stream().map(bo -> cloneObj(bo, SimpleArbitrationDto.class))
                .collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public ArbitrationDto createArbitration(Long id, Byte type, Integer quantity, String reason,
                                            Long regionId, String detail, String consignee, String mobile, UserDto userDto) {
        Arbitration bo = Arbitration.builder().status(Arbitration.APPLYING).aftersaleId(id).applicantType(type).reason(reason).build();
        Arbitration ret = arbitrationDao.insert(bo, userDto);
        return cloneObj(ret, ArbitrationDto.class);
    }

    public PageDto<ArbitrationDto> findArbitrationsByArbitratorId(Integer page, Integer pageSize, Long arbitratorId, UserDto userDto) {
        if (!Objects.equals(arbitratorId, userDto.getId())) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, ReturnNo.AUTH_NO_RIGHT.getMessage());
        }
        List<ArbitrationDto> ret = arbitrationDao.findByArbitratorId(arbitratorId, page, pageSize)
                .stream().map(bo -> cloneObj(bo, ArbitrationDto.class))
                .collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public ReturnObject deleteById(Long id, UserDto userDto) {
        if (null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN,
                    String.format(ReturnNo.AUTH_NEED_LOGIN.getMessage()));
        } else {
            return arbitrationDao.deleteById(id, userDto.getId());
        }
    }

    public ReturnObject acceptById(Long arbitrationId,
                                   Long arbitratiorId,
                                   String arbitratiorName,
                                   UserDto userDto) {
        if (null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN,
                    String.format(ReturnNo.AUTH_NEED_LOGIN.getMessage()));
        } else if (Objects.equals(userDto.getId(), arbitratiorId)) {
            return new ReturnObject(arbitrationDao.acceptById(arbitrationId, arbitratiorId, arbitratiorName));
        } else {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT,
                    String.format(ReturnNo.AUTH_NO_RIGHT.getMessage()));
        }

    }

    public ReturnObject closeById(Long arbitrationId,
                                  String result,
                                  UserDto userDto) {
        if (null == userDto.getId()) {
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN,
                    String.format(ReturnNo.AUTH_NEED_LOGIN.getMessage()));
        } else {
            return arbitrationDao.closeById(arbitrationId, userDto.getId(), result);
        }
    }
}
