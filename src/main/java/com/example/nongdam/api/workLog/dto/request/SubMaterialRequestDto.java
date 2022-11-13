package com.example.nongdam.api.workLog.dto.request;

import com.example.nongdam.entity.subMaterial.entity.SubMaterial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class SubMaterialRequestDto {
    private int type;
    private String product;
    private String use;

    public SubMaterial build() {
        return SubMaterial.builder()
                .type(this.type)
                .product(this.product)
                .use(this.use)
                .build();
    }
}