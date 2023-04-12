package com.example.reactiveyamlgen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

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
    @NotBlank(message="isName 는 필수값 입니다.")
    private Boolean isName;
    @NotBlank(message="isFilter 는 필수값 입니다.")
    private Boolean isFilter;
    private List<ArgsDto> args;

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
