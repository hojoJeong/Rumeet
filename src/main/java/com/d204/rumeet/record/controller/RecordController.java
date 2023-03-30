package com.d204.rumeet.record.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.record.model.dto.RaceInfoDto;
import com.d204.rumeet.record.model.dto.RaceInfoReqDto;
import com.d204.rumeet.record.model.dto.RaceModeInfoDto;
import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "메인페이지 기록 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getRecord(@PathVariable int userId) {
        RecordDto record = recordService.getRecord(userId);
        RespData<RecordDto> data = new RespData<>();
        data.setData(record);
        return data.builder();
    }

    @Operation(summary = "record 업데이트")
    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody String json_data) throws ParseException, org.json.simple.parser.ParseException {
//        recordService.updateRecord(json_data);
        RespData<RecordDto> data = new RespData<>();
        data.setMsg("기록 업데이트 완료");
        return data.builder();
    }

    @Operation(summary = "raceInfo 업데이트")
    @PostMapping("/race")
    public ResponseEntity<?> addRaceInfo(@RequestPart("raceInfo") RaceInfoReqDto raceInfoReqDto,
                                         @RequestPart("polyline") MultipartFile poly) {
        recordService.addRaceInfo(raceInfoReqDto, poly);
        recordService.updateRecord(raceInfoReqDto);
        RespData<RecordDto> data = new RespData<>();
        data.setMsg("기록 추가 완료");
        return data.builder();
    }

    @Operation(summary = "raceInfo 조회")
    @GetMapping("/race/{userId}")
    public ResponseEntity<?> addRaceInfo(@PathVariable int userId) throws ParseException, org.json.simple.parser.ParseException {
        List<RaceModeInfoDto> race = recordService.getRaceInfo(userId);
        RespData<List> data = new RespData<>();
        data.setData(race);
        return data.builder();
    }

}
