package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;

public class OfflineService extends Service{

    @Override
    public void accept(String maintainerName, String maintainerMobile, UserDto userDto) {
        if (this.allowStatus(Service.ASSIGNED)) {
            this.setStatus(Service.ASSIGNED);
            this.setMaintainerName(maintainerName);
            this.setMaintainerMobile(maintainerMobile);
            this.serviceDao.save(this, userDto);
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
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, this.getStatusName()));
        }
    }


}
