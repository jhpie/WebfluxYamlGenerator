package com.example.reactiveyamlgen.dto;

import com.example.reactiveyamlgen.jpa.entity.FilterAndPredicate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterAndPredicateDto{
    private Long id;
    @NotBlank(message="routeId 는 필수값 입니다.")
    private String routeId;
    @NotBlank(message="name 는 필수값 입니다.")
    private String name;
    @NotNull(message="isName 는 필수값 입니다.")
    private Boolean isName;
    @NotNull(message="isFilter 는 필수값 입니다.")
    private Boolean isFilter;

    @Valid
    private List<ArgsDto> args;

    public FilterAndPredicateDto(FilterAndPredicate t2) {
        this.id = t2.getId();
        this.routeId = t2.getRouteId();
        this.name = t2.getName();
        this.isName = t2.getIsName();
        this.isFilter = t2.getIsFilter();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FilterAndPredicateDto)) return false;
        FilterAndPredicateDto other = (FilterAndPredicateDto) obj;
        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
