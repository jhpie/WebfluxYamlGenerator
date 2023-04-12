package com.example.reactiveyamlgen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    private String routeId;
    private String name;
    private Boolean isName;
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
