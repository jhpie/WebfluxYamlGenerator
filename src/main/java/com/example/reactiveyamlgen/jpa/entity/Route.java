package com.example.reactiveyamlgen.jpa.entity;


import com.example.reactiveyamlgen.dto.RouteDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("route")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Route {
    @Id
    @Column("id")
    private Long id;
    @Column("uri")
    private String uri;
    @Column("domain_id")
    private Long domainId;
    @Column("route_id")
    private String routeId;
    @Column("metadata")
    private String metadata;

    @Transient
    private List<FilterAndPredicate> predicates;

    @Transient
    private List<FilterAndPredicate> filters;

    public Route(RouteDto routeDto) {
        this.uri = routeDto.getUri();
        this.routeId = routeDto.getRouteId();
        this.domainId = routeDto.getDomainId();
        this.routeId = routeDto.getRouteId();
        this.metadata = routeDto.getMetadata();
        this.predicates = routeDto.getPredicates().stream().map(FilterAndPredicate::new).toList();
        this.filters = routeDto.getFilters().stream().map(FilterAndPredicate::new).toList();
    }

    public RouteDto toDto() {
        RouteDto dto = new RouteDto();
        dto.setId(this.id);
        dto.setUri(this.uri);
        dto.setDomainId(this.domainId);
        dto.setRouteId(this.routeId);
        dto.setMetadata(this.metadata);
        return dto;
    }
}