package cn.edu.xmu.oomall.aftersale.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.aftersale.dao.bo.Arbitration;
import cn.edu.xmu.oomall.aftersale.mapper.ArbitrationPoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.po.ArbitrationPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
@RefreshScope
public class ArbitrationDao extends OOMallObject implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(ArbitrationDao.class);

    private final static String KEY = "Ar%d";

    @Value("3600")

    private int timeout;
    private ArbitrationPoMapper arbitrationPoMapper;
    private RedisUtil redisUtil;

    @Autowired
    ArbitrationDao(ArbitrationPoMapper arbitrationPoMapper,
                   RedisUtil redisUtil) {
        this.arbitrationPoMapper = arbitrationPoMapper;
        this.redisUtil = redisUtil;
    }

    private Arbitration getBo(ArbitrationPo po, String redisKey) {
        Arbitration ret = cloneObj(po, Arbitration.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        return ret;
    }

    public Arbitration findById(Long id) {
        logger.debug("findArbitrationsById: id = {}", id);
        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            return (Arbitration) redisUtil.get(key);
        } else {
            ArbitrationPo po = arbitrationPoMapper.findByIdEquals(id);
            if (null == po) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仲裁单", id));
            } else {
                return this.getBo(po, key);
            }
        }
    }


    public Arbitration insert(Arbitration bo, UserDto userDto) {
        ArbitrationPo po = cloneObj(bo, ArbitrationPo.class);
        putUserFields(po, "creator", userDto);
        putGmtFields(po, "Create");
        po = arbitrationPoMapper.save(po);
        return cloneObj(po, Arbitration.class);
    }

    public List<Arbitration> findUnacceptedArbitrations(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return arbitrationPoMapper.findByStatusEquals(Arbitration.APPLYING, pageable)
                .stream().map(po -> cloneObj(po, Arbitration.class))
                .collect(Collectors.toList());
    }

    public List<Arbitration> findByArbitratorId(Long arbitratorId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return arbitrationPoMapper.findByArbitratorIdEquals(arbitratorId, pageable)
                .stream().map(po -> cloneObj(po, Arbitration.class))
                .collect(Collectors.toList());
    }

    public ReturnObject deleteById(Long id, Long userId) {
        ArbitrationPo po = arbitrationPoMapper.findByIdEquals(id);
        if (null == po) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
        } else {
            if (Objects.equals(po.getCreatorId(), userId)) {
                po.setStatus(Arbitration.CANCEL);
                arbitrationPoMapper.save(po);
                return new ReturnObject(ReturnNo.OK);
            } else {
                throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, ReturnNo.AUTH_NO_RIGHT.getMessage());
            }
        }
    }

    public ReturnObject acceptById(Long arbitrationId,
                                   Long arbitratiorId,
                                   String arbitratiorName) {
        ArbitrationPo po = arbitrationPoMapper.findByIdEquals(arbitrationId);
        if (null == po) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仲裁单", arbitrationId));
        } else if (!Objects.equals(po.getStatus(), Arbitration.APPLYING)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "仲裁单", arbitrationId, Arbitration.STATUSNAMES.get(po.getStatus())));
        } else {
            po.setStatus(Arbitration.ACCEPT);
            po.setArbitratorId(arbitratiorId);
            po.setArbitratorName(arbitratiorName);
            arbitrationPoMapper.save(po);
            return new ReturnObject(ReturnNo.OK);
        }
    }

    public ReturnObject closeById(Long arbitrationId,
                                  Long creatorId,
                                  String result) {
        ArbitrationPo po = arbitrationPoMapper.findByIdEquals(arbitrationId);
        if (null == po) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仲裁单", arbitrationId));
        } else if (!Objects.equals(po.getCreatorId(), creatorId)) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT, ReturnNo.AUTH_NO_RIGHT.getMessage());
        } else {
            po.setStatus(Arbitration.CLOSED);
            po.setResult(result);
            arbitrationPoMapper.save(po);
            return new ReturnObject(ReturnNo.OK);
        }
    }

}
