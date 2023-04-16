package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.oomall.aftersale.mapper.po.AftersalePo;

import javax.validation.constraints.NotNull;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

public class AftersaleFactory {

    //根据不同的售后类型，返回不同的Aftersale bo
    public static Aftersale createAftersale(AftersalePo po){
        if(po.getStatus().equals(Aftersale.REPLACE)){
            return cloneObj(po,ReplaceAftersale.class);
        }
        else if(po.getStatus().equals(Aftersale.RETURN)){
            return cloneObj(po,ReturnAftersale.class);
        }
        else {
            return cloneObj(po,RepairAftersale.class);
        }
    }



}
