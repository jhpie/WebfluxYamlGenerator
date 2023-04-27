package com.example.reactiveyamlgen.dto;

import com.example.reactiveyamlgen.jpa.entity.Args;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArgsDto {
    private Long id;

    @NotBlank(message="routeId 는 필수값 입니다.")
    private String routeId;

    @NotBlank(message="parentName 는 필수값 입니다.")
    private String parentName;

    private String hashKey;

    @NotBlank(message="hashValue 는 필수값 입니다.")
    private String hashValue;

    public ArgsDto(Args args) {
        this.id = args.getId();
        this.routeId = args.getRouteId();
        this.parentName = args.getParentName();
        this.hashKey = args.getHashKey();
        this.hashValue = args.getHashValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ArgsDto)) return false;
        ArgsDto other = (ArgsDto) obj;
        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
