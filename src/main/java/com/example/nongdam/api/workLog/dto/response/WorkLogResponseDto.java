package com.example.nongdam.api.workLog.dto.response;

import com.example.nongdam.api.CropDto;
import com.example.nongdam.entity.subMaterial.entity.SubMaterial;
import com.example.nongdam.entity.workLog.entity.WorkLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkLogResponseDto {
    private Long id;
    private String title;
    private String date;
    private int workTime;
    private String memo;
    private Long harvest;
    private CropDto crop;
    private List<SubMaterialResponseDto> subMaterial = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    private SubWorkLogResponseDto pre;

    private SubWorkLogResponseDto next;

    public WorkLogResponseDto(WorkLog workLog, CropDto cropDto) {
        this.id = workLog.getId();
        this.title = workLog.getTitle();
        this.date = workLog.getDate().toString();
        this.workTime = workLog.getWorkTime();
        this.memo = workLog.getMemo();
        this.harvest = workLog.getHarvest();
        this.crop = cropDto;
        for(SubMaterial material : workLog.getSubMaterials())
            this.subMaterial.add(new SubMaterialResponseDto(material));
        this.images.addAll(workLog.getImages());
    }
    public void setPreviousWorkLogInfo(SubWorkLogResponseDto pre){
        this.pre = pre;
    }
    public void setNextWorkLogInfo(SubWorkLogResponseDto next){
        this.next = next;
    }
}