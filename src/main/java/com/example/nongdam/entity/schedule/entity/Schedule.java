package com.example.nongdam.entity.schedule.entity;

import com.example.nongdam.entity.crop.entity.Crop;
import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.global.FinalValue;
import com.example.nongdam.api.scedule.dto.request.ScheduleRequestDto;
import com.example.nongdam.global.exception.WrongArgumentException;
import com.example.nongdam.entity.crop.repository.CropRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String toDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Crop crop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    public void update(ScheduleRequestDto dto, CropRepository repository) throws WrongArgumentException {
        this.startTime = LocalDateTime.parse(dto.getStartTime(), FinalValue.DAYTIME_FORMATTER);
        this.endTime = LocalDateTime.parse(dto.getEndTime(), FinalValue.DAYTIME_FORMATTER);
        if(this.startTime.isAfter(endTime))
            throw new WrongArgumentException("잘못된 입력입니다.","시작/끝 시간");
        this.crop = repository.findById(dto.getCropId()).orElseThrow(()->new IllegalArgumentException("작물 정보를 찾을 수 없습니다."));
        this.toDo = dto.getToDo();
    }
}
