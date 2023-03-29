package com.d204.rumeet.record.controller;

import com.d204.rumeet.data.RespData;
import com.d204.rumeet.record.model.dto.RecordDto;
import com.d204.rumeet.record.model.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;
    @Operation(summary = "기록 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getRecord(@PathVariable int userId) {
        RecordDto record = recordService.getRecord(userId);
        RespData<RecordDto> data = new RespData<>();
        data.setData(record);
        return data.builder();
    }

    @Operation(summary = "기록 업데이트")
    @PutMapping
    public ResponseEntity<?> updateRecord(@RequestBody String json_data) throws ParseException, org.json.simple.parser.ParseException {
        recordService.updateRecord(json_data);
        RespData<RecordDto> data = new RespData<>();
        data.setMsg("기록 업데이트 완료");
        return data.builder();
    }

}
