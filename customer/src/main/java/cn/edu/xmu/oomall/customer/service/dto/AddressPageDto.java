package cn.edu.xmu.oomall.customer.service.dto;

import cn.edu.xmu.javaee.core.model.dto.PageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AddressPageDto<T> extends PageDto<T> {

    //总数
    private int total;

    //总页数
    private int pages;

    public AddressPageDto(int total, int pages) {
        super(null,1,10);
        this.total = total;
        this.pages = pages;
    }

    public AddressPageDto(List<T> list, int page, int pageSize, int total, int pages) {
        super(list, page, pageSize);
        this.total = total;
        this.pages = pages;
    }

    public AddressPageDto(List<T> list, int page, int pageSize) {
        super(list, page, pageSize);
    }

    public AddressPageDto() {
        super(null,1,10);
    }
}
