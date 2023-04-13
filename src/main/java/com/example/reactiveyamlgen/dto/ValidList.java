package com.example.reactiveyamlgen.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidList<E> implements List<E> {
    @Valid
    @NotEmpty(message = "RouteList가 Null이 되어선 안됩니다")
    @Delegate
    private List<E> list;

    public ValidList() {
        this.list = new ArrayList<>();
    }

    public ValidList(List<E> list) {
        this.list = list;
    }
}