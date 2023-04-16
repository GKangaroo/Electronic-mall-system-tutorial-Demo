package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.openfeign.Bo.Express;

public class PostService extends Service{
    @Override
    public void accept(String maintainerName, String maintainerMobile, UserDto userDto) {
        if (this.allowStatus(Service.WAITRECEIVE)) {
            this.setStatus(Service.WAITRECEIVE);
            this.setMaintainerName(maintainerName);
            this.setMaintainerMobile(maintainerMobile);
            serviceDao.save(this, userDto);
            Express express = expressDao.createExpress();
            ServiceLogistics serviceLogistics = ServiceLogistics.builder()
                    .serviceId(this.getId()).type(this.getType()).packageId(express.getId()).build();
            this.serviceLogisticsDao.insert(serviceLogistics, userDto);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, this.getStatusName()));
        }
    }

    @Override
    public void finish(String result, UserDto userDto) {
        if (this.allowStatus(Service.FINISHED)) {
            this.setStatus(Service.FINISHED);
            this.setResult(result);
            serviceDao.save(this, userDto);
            if(this.getType() == 1){  //寄件的情况
                Express express = expressDao.createExpress();
                ServiceLogistics serviceLogistics = ServiceLogistics.builder()
                        .serviceId(this.getId()).type(this.getType()).packageId(express.getId()).build();
                serviceLogisticsDao.insert(serviceLogistics, userDto);
            }
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, this.getStatusName()));
        }
    }

}
