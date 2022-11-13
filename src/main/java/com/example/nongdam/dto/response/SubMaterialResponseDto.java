package com.example.nongdam.dto.response;

import com.example.nongdam.entity.SubMaterial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubMaterialResponseDto {
    private long id;

    private int type;

    private String product;

    private String use;

    public SubMaterialResponseDto(SubMaterial material){
        this.id = material.getId();
        this.type = material.getType();
        this.product = material.getProduct();
        this.use = material.getUse();
    }
}
