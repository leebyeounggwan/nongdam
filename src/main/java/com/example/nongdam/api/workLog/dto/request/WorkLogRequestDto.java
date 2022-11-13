package com.example.nongdam.api.workLog.dto.request;

import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.entity.workLog.entity.WorkLog;
import com.example.nongdam.global.FinalValue;
import com.example.nongdam.entity.crop.repository.CropRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WorkLogRequestDto {
    private String title;
    private String date;
    private int workTime;
    private int crop;
    private String memo;
    private Long harvest;
    private List<SubMaterialRequestDto> subMaterial;

    private List<String> images;

    public WorkLog build(Member member, CropRepository repository) {
        WorkLog workLog = WorkLog.builder()
                .title(this.title)
                .date(LocalDate.parse(this.date, FinalValue.DAY_FORMATTER))
                .workTime(this.workTime)
                .memo(this.memo)
                .crop(repository.findById(crop).orElseThrow(() -> new IllegalArgumentException("작물 정보를 찾을 수 없습니다.")))
                .harvest(this.harvest)
                .member(member)
                .build();
        workLog.setQuarter();
        subMaterial.forEach(e -> workLog.addSubMaterial(e.build()));
        return workLog;
    }
}