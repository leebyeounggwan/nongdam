package com.example.nongdam.dto.response;


import com.example.nongdam.entity.Crop;
import com.example.nongdam.enums.CropCategoryCode;
import com.example.nongdam.enums.CropTypeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropDto {

    private int id;

    private String name;

    private String category;

    private String type;

    public CropDto(Crop crop) {
        this.id = crop.getId();
        this.name = crop.getName();
        this.category = CropCategoryCode.findByCode(crop.getCategory());
        this.type = CropTypeCode.findByCode(crop.getType()).name();
    }
}