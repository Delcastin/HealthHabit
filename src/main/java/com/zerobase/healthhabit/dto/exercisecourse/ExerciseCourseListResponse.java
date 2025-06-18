package com.zerobase.healthhabit.dto.exercisecourse;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExerciseCourseListResponse<T> { // 목록전체 + Paging 정보를 포함한 DTO

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
    private int currentPage;

    public ExerciseCourseListResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean isFirst, boolean isLast) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isFirst = isFirst;
        this.isLast = isLast;

    }
}
