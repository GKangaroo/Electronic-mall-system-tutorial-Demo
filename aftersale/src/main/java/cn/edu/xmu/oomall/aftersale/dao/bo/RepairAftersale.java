package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RepairAftersale extends Aftersale{


    @Override
    public void confirmAftersale(boolean confirm, UserDto userDto) {
        super.setStatus(Aftersale.DISPATCHING);
    }

    @Override
    public void query(Byte status){
        switch (status){
            case (byte)1: { super.setStatus(Aftersale.PROCESS);}
            case (byte)2: { super.setStatus(Aftersale.END);}
            case (byte)3: { super.setStatus(Aftersale.CANCEL);}
        }
    }

    //维修售后不可确认收货
    @Override
    public void confirmReceive(boolean confirm) throws BusinessException {
        throw new BusinessException(ReturnNo.AFTERSALE_NOT_RETURNCHANGE,
                String.format(ReturnNo.AFTERSALE_NOT_RETURNCHANGE.getMessage(),super.id));
    }


}
