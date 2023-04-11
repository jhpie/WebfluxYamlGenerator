package com.example.reactiveyamlgen.jpa.entity;

import com.example.reactiveyamlgen.dto.ArgsDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("args")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Args{
    @Id
    @Column("id")
    private Long id;
    @Column("route_id")
    private String routeId;
    @Column("parent_name")
    private String parentName;
    @Column("hash_key")
    private String hashKey;
    @Column("hash_value")
    private String hashValue;

    public Args(ArgsDto argsDto) {
        this.routeId = argsDto.getRouteId();
        this.parentName = argsDto.getParentName();
        this.hashKey = argsDto.getHashKey();
        this.hashValue = argsDto.getHashValue();
    }

    public ArgsDto toDto() {
        ArgsDto dto = new ArgsDto();
        dto.setId(this.id);
        dto.setRouteId(this.routeId);
        dto.setParentName(this.getParentName());
        dto.setHashKey(this.getHashKey());
        dto.setHashValue(this.getHashValue());
        return dto;
    }
}
