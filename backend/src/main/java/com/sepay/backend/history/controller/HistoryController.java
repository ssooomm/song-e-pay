package com.sepay.backend.history.controller;

import com.sepay.backend.common.pagination.Page;
import com.sepay.backend.common.pagination.PageRequest;
import com.sepay.backend.history.dto.HistoryDTO;
import com.sepay.backend.history.service.HistoryService;
import com.sepay.backend.user.dto.SearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/histories")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/filter")
    public ResponseEntity<Page<HistoryDTO>> getHistoryList(@RequestBody SearchItem searchItem, PageRequest pageRequest) {
        log.info("SearchItem received: {}", searchItem);
        log.info("PageRequest received: {}", pageRequest);

        try {
            // 필터된 거래 내역을 페이지네이션과 함께 반환
            Page<HistoryDTO> filteredHistories = historyService.getFilter(searchItem, pageRequest);
            log.info("Filtered Histories: {}", filteredHistories);
            return ResponseEntity.ok(filteredHistories);
        } catch (Exception e) {
            // 오류 발생 시 상세 로그 출력
            log.error("Error occurred while filtering histories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 500 에러 발생 시 null 반환
        }
    }

    @GetMapping("/getList")
    public ResponseEntity<Page> getList(PageRequest pageRequest) {
        return ResponseEntity.ok(historyService.getAllHistories(pageRequest));
    }

    @PostMapping("/updateMemo")
    public ResponseEntity<String> updateMemo(@RequestBody HistoryDTO historyDTO) {
        try {
            historyService.updateMemo(historyDTO);
            return ResponseEntity.ok("메모가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            log.error("메모 업데이트 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메모 업데이트에 실패했습니다.");
        }
    }
}