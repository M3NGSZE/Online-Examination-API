package com.m3ngsze.sentry.onlineexaminationapi.model.response;

import java.util.List;

public class ListResponse <T> {
    List<T> data;
    PaginationResponse pagination;
}
