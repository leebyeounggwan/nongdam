package com.example.nongdam.controller;

import com.example.nongdam.dto.request.WorkLogRequestDto;
import com.example.nongdam.dto.response.WorkLogResponseDto;
import com.example.nongdam.security.MemberDetail;
import com.example.nongdam.service.WorkLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkLogController {
    private final WorkLogService workLogService;

    @PostMapping(value = "/worklog", consumes = {"multipart/form-data"})
    //@Operation(summary = "작업일지 생성")
    public void createWorkLog(@AuthenticationPrincipal MemberDetail details,
                              @RequestPart String data,
                              @RequestPart(required = false) List<MultipartFile> images) {
        ObjectMapper mapper = new ObjectMapper();
        WorkLogRequestDto dto;
        try {
            dto = mapper.readValue(data, WorkLogRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        workLogService.createWorkLog(details.getMember(), dto, images);
    }

    // 작업일지 조회
    @GetMapping("/worklog/{worklogid}")
    public WorkLogResponseDto getWorkLog(@PathVariable Long worklogid,
                                         @AuthenticationPrincipal MemberDetail detail) {
        String userEmail = detail.getUsername();
        return workLogService.getWorkLogDetails(worklogid, detail.getMember().getId());
    }

    //@Operation(summary = "작업일지 전체조회")
    @GetMapping("/worklog")
    public List<WorkLogResponseDto> getWorkLogList(@AuthenticationPrincipal MemberDetail detail) throws JsonProcessingException {
        return workLogService.getWorkLogList(detail);
    }

    //작업일지 삭제
    @DeleteMapping("/worklog/{worklogid}")
    public void deleteWorkLog(@PathVariable Long worklogid, @AuthenticationPrincipal MemberDetail details) {
        workLogService.deleteWorkLog(worklogid, details);
    }

    //작업일지 수정
    @PutMapping(value = "/worklog/{worklogid}/update", consumes = {"multipart/form-data"})
    public void updateWorkLog(@PathVariable Long worklogid,
                              @RequestPart String data,
                              @AuthenticationPrincipal MemberDetail details,
                              @RequestPart(required = false) List<MultipartFile> images) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        WorkLogRequestDto dto;
        try {
            dto = mapper.readValue(data, WorkLogRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        workLogService.updateWorkLog(worklogid, dto, details, images);
    }
}