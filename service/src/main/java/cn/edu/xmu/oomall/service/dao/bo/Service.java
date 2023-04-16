package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.ServiceDao;
import cn.edu.xmu.oomall.service.dao.ServiceLogisticsDao;
import cn.edu.xmu.oomall.service.dao.openfeign.ExpressDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author：guiqingxin
 * @date：2022/12/20 0:08
 */
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class Service extends OOMallObject implements Serializable {
    /**
     * 新建
     */
    public static final Byte NEW = 0;
    /**
     * 已分派
     */
    public static final Byte ASSIGNED = 1;
    /**
     * 已完成
     */
    public static final Byte FINISHED = 2;
    /**
     * 取消
     */
    public static final Byte CANCEL = 3;
    /**
     * 待收件
     */
    public static final Byte WAITRECEIVE = 4;
    /**
     * 维修
     */
    public static final Byte REPAIR = 5;


    public static final Map<Byte,String> STATUSNAMES = new HashMap(){
        {
            put(NEW,"新建");
            put(ASSIGNED,"已分派");
            put(FINISHED,"已完成");
            put(CANCEL,"取消");
            put(WAITRECEIVE,"待收件");
            put(REPAIR,"维修");
        }
    };
    /**
     * 服务商允许的状态迁移
     */
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(NEW, new HashSet<>(){
                {//有两个大括号的愿意是:这个大括号是一个匿名内部类的实例化,外面的大括号是一个静态代码块
                    add(ASSIGNED);
                    add(CANCEL);
                    add(WAITRECEIVE);
                }
            });

            put(ASSIGNED, new HashSet<>(){
                {
                    add(FINISHED);
                    add(CANCEL);
                }
            });

            put(WAITRECEIVE, new HashSet<>(){
                {
                    add(REPAIR);
                    add(CANCEL);
                }
            });

            put(REPAIR, new HashSet<>(){
                {
                    add(FINISHED);
                    add(CANCEL);
                }
            });
        }
    };

    public boolean allowStatus(Byte status){//status是要迁移到的状态
        boolean ret = false;

        if (null != status && null != this.status){
            Set<Byte> allowStatusSet = toStatus.get(this.status);//Map.get()方法返回的是一个Set集合
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    @JsonIgnore
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }
    @Builder
    public Service(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Byte type, String consignee, Long serviceregionId, String serviceAddress, String consigneeMobile, Byte status, String maintainerName, String maintainerMobile,
                   String description, String result, Long maintainerId , Long shopId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.type = type;
        this.consignee = consignee;
        this.serviceregionId = serviceregionId;
        this.serviceAddress = serviceAddress;
        this.consigneeMobile = consigneeMobile;
        this.status = status;
        this.maintainerName = maintainerName;
        this.maintainerMobile = maintainerMobile;
        this.description = description;
        this.result = result;
        this.maintainerId = maintainerId;
        this.shopId = shopId;
    }
    @Setter
    @Getter
    private Byte type;
    /**
     * 联系人
     */
    @Setter
    @Getter
    private String consignee;
    /**
     * 服务地址
     */
    @Setter
    @Getter
    private Long serviceregionId;
    /**
     * 详细地址
     */
    @Setter
    @Getter
    private String serviceAddress;
    /**
     * 联系人电话
     */
    private String consigneeMobile;
    @Setter
    @Getter
    private Byte status;
    /**
     * 服务人员姓名
     */
    @Setter
    @Getter
    private String maintainerName;
    /**
     * 服务人员电话
     */
    @Setter
    @Getter
    private String maintainerMobile;
    /**
     * 描述
     */
    @Setter
    @Getter
    private String description;
    /**
     * 服务结果
     */
    @Setter
    @Getter
    private String result;
    /**
     * 服务商id
     */
    @Setter
    @Getter
    private Long maintainerId;

    @Setter
    @Getter
    private Long shopId;

    ServiceDao serviceDao;
    ServiceLogisticsDao serviceLogisticsDao;
    ExpressDao expressDao;

    //服务商确认接受服务单
    public abstract void accept(String maintainerName, String maintainerMobile, UserDto userDto);


    //服务商确认服务单结束
    public abstract void finish(String result, UserDto userDto);

}
