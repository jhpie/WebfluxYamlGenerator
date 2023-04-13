package com.example.reactiveyamlgen.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

//@Getter
//@NoArgsConstructor
//public class RouteListDto {
//    List<@Valid RouteDto> routeDtos;
//}
@Getter
public class ValidList<E> implements List<E> {
    @Valid
    @Delegate
    private List<E> list;

    public ValidList() {
        this.list = new ArrayList<>();
    }

    public ValidList(List<E> list) {
        this.list = list;
    }
}