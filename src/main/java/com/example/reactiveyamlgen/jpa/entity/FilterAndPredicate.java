package com.example.reactiveyamlgen.jpa.entity;

import com.example.reactiveyamlgen.dto.FilterAndPredicateDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Table("filterandrpredicate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilterAndPredicate{
    @Id
    @Column("id")
    private Long id;
    @Column("route_id")
    private String routeId;
    @Column("name")
    private String name;
    @Column("is_filter")
    private Boolean isFilter;
    @Transient
    private List<Args> args;

    public FilterAndPredicate(FilterAndPredicateDto filterAndPredicateDto) {
        this.routeId = filterAndPredicateDto.getRouteId();
        this.name = filterAndPredicateDto.getName();
        this.isFilter = filterAndPredicateDto.getIsFilter();
        this.args = filterAndPredicateDto.getArgs().stream().map(Args::new).collect(Collectors.toList());
    }
    public FilterAndPredicateDto toDto() {
        FilterAndPredicateDto dto = new FilterAndPredicateDto();
        dto.setId(this.id);
        dto.setRouteId(this.getRouteId());
        dto.setName(this.getName());
        dto.setIsFilter(this.getIsFilter());
        return dto;
    }
}
