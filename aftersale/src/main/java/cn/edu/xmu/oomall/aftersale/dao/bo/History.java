package cn.edu.xmu.oomall.aftersale.dao.bo;


import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History  extends OOMallObject implements Serializable {

    Long aftersaleId;

    String content;
}
