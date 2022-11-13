package com.example.nongdam.entity.workLog.entity;

import com.example.nongdam.entity.crop.entity.Crop;
import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.entity.subMaterial.entity.SubMaterial;
import com.example.nongdam.global.FinalValue;
import com.example.nongdam.api.workLog.dto.request.WorkLogRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WorkLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank(message = "제목")
    private String title;

    @Column
    private LocalDate date;

    @Column
    private int workTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @NotNull(message = "작물")
    private Crop crop;

    @Column(length = 500)
    private String memo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<SubMaterial> subMaterials = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "worklog_pictures", joinColumns = {@JoinColumn(name = "work_log_id", referencedColumnName = "id")})
    @Column
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column
    @Builder.Default
    @PositiveOrZero(message = "수확량")
    private long harvest = 0L;

    @Column
    private int quarter;

    public void updateWorkLog(WorkLogRequestDto requestDto, Crop crop,
                              List<SubMaterial> SubMaterialList, List<MultipartFile> files) {
        this.title = requestDto.getTitle();
        this.date = LocalDate.parse(requestDto.getDate(), FinalValue.DAY_FORMATTER);
        this.workTime = requestDto.getWorkTime();
        this.crop = crop;
        this.memo = requestDto.getMemo();
        this.subMaterials.clear();
        this.subMaterials.addAll(SubMaterialList);
        if (files != null) this.images.clear();
        this.harvest = requestDto.getHarvest();
    }

    public void setQuarter() {
        if (date != null) {
            int month = date.getMonthValue();
            if (month >= 1 && month <= 3)
                this.quarter = 1;
            else if (month >= 4 && month <= 6)
                this.quarter = 2;
            else if (month >= 7 && month <= 9)
                this.quarter = 3;
            else
                this.quarter = 4;
        }
    }

    public void addSubMaterial(SubMaterial material) {
        this.subMaterials.add(material);
    }

    public void addPicture(String url) {
        this.images.add(url);
    }
}
