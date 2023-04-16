package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ReplaceAftersale extends Aftersale{


    @Override
    public void confirmAftersale(boolean confirm, UserDto userDto) {
        //商铺同意
        if(confirm){
            //虚拟商品无需退货
            if(super.getProduct().getType().equals(313)){
                super.setStatus(Aftersale.REPLACING);
            }
            else {
                super.setStatus(Aftersale.PROCESS);
            }
        }
        //商铺拒绝
        else {
            super.setStatus(Aftersale.NEW);
        }
    }

    @Override
    public void query(Byte status){
        super.setStatus(Aftersale.END);
    }

    //维修售后不可确认收货
    @Override
    public void confirmReceive(boolean confirm) throws BusinessException {
        //售后单状态不为处理中
        if(!super.getStatus().equals(Aftersale.PROCESS)){
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage(),"售后单",super.id,super.getStatusName()));
        }
        //验收不合格，售后取消
        if(!confirm){
            super.setStatus(Aftersale.CANCEL);
        }
        //验收合格，发出换货订单
        else {
            super.setStatus(Aftersale.REPLACING);
        }
    }



}
