package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.oomall.aftersale.service.dto.SimpleAdminUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    private Long id;

    private Byte type;

    private String consignee;

    private Region region;

    private String address;

    private String mobile;

    private Byte status;

    private String maintainerName;

    private String maintainerMobile;

    private String description;

    private String result;

    private Shop maintainer;

    private Long creatorId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleAdminUserDto creator;

    private SimpleAdminUserDto modifier;

}
