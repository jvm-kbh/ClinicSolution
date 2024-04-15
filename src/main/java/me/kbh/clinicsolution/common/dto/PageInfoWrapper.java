package me.kbh.clinicsolution.common.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PageInfoWrapper<T> {
    List<T> pageDataList;
    Pageable pageable;
    long totalCount;
    int totalPage;

    public PageInfoWrapper(List<T> pageDataList, Page pageInfo) {
        this.pageDataList = pageDataList;
        this.pageable = pageInfo.getPageable();
        this.totalCount = pageInfo.getTotalElements();
        this.totalPage = pageInfo.getTotalPages();
    }
}