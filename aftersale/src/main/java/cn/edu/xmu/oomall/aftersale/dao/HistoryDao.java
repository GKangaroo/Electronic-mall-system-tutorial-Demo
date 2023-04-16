package cn.edu.xmu.oomall.aftersale.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.aftersale.dao.bo.History;
import cn.edu.xmu.oomall.aftersale.mapper.HistoryPoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.po.AftersalePo;
import cn.edu.xmu.oomall.aftersale.mapper.po.HistoryPo;
import jdk.dynalink.linker.LinkerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
public class HistoryDao {

    private static final Logger logger = LoggerFactory.getLogger(HistoryDao.class);

    public static final String KEY = "HI%d";

    @Value("3600")
    private int timeout;

    private RedisUtil redisUtil;
    private HistoryPoMapper historyPoMapper;

    @Autowired
    public HistoryDao(RedisUtil redisUtil, HistoryPoMapper historyDao) {
        this.redisUtil = redisUtil;
        this.historyPoMapper = historyDao;
    }

    private History getBo(HistoryPo po, String redisKey) {
        History ret = cloneObj(po, History.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(History bo) {

    }


    /**
     * 根据id获取对象
     *
     * @date: 2022/12/22 17:01
     */
    public History findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            History bo = (History) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<HistoryPo> retObj = this.historyPoMapper.findById(id);
        if (retObj.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", id));
        } else {
            HistoryPo po = retObj.get();
            return this.getBo(po, key);
        }
    }

    /**
     * 根据aftersaleID获取history
     * @author: 兰文强
     * @date: 2022/12/22 20:50
     */
    public List<History> retrieveByAftersaleId(Long aftersaleId, Integer page, Integer pageSize){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        Page<HistoryPo> ret = historyPoMapper.findByAftersaleId(aftersaleId,pageable);
        if(ret==null)
            return new ArrayList<>();
        return ret.stream().map(po->getBo(po,null)).collect(Collectors.toList());
    }




}
