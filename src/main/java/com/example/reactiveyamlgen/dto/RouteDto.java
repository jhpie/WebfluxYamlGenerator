package com.example.reactiveyamlgen.dto;

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
public class RouteDto {
    private Long id;
    @NotBlank(message="uri 는 필수값 입니다.")
    private String uri;
    @NotNull(message="domainId 는 필수값 입니다.")
    private Long domainId;
    @NotBlank(message="routeId 는 필수값 입니다.")
    private String routeId;
    private String metadata;

    @Valid
    private List<FilterAndPredicateDto> predicates;

    @Valid
    private List<FilterAndPredicateDto> filters;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof RouteDto)) return false;
        RouteDto other = (RouteDto) obj;
        return Objects.equals(routeId, other.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }
}
