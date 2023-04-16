package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.oomall.service.mapper.po.ServicePo;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

public class ServiceFactory {


    public static Service getServiceByPo(ServicePo po) {
        //根据不同的服务类型，返回不同的服务单对象
        switch (po.getType()) {
            case 0: {
                return cloneObj(po, OndoorService.class);
            }
            case 1: {
                return cloneObj(po, PostService.class);
            }
            default: {
                return cloneObj(po, OfflineService.class);
            }
        }
    }

}
