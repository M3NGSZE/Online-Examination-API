package com.m3ngsze.sentry.onlineexaminationapi.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListResponse <T> {

    List<T> data;

    PaginationResponse pagination;

}
