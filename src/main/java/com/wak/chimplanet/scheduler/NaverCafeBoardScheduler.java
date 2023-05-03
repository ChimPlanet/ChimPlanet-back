package com.wak.chimplanet.scheduler;

import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.service.BoardService;
import com.wak.chimplanet.service.CafeBoardScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class NaverCafeBoardScheduler {

    private CafeBoardScheduleService cafeBoardScheduleService;

    /**
     * 10분 주기 naverBoardAPI 조회 스케줄러
     *
     * *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 0 */4 * * *")
    public void scheduleCafeBoard() {
        log.info("scheduleNaverCafeBoard task cron jobs");
        // boardService.saveAllBoards();
        int pageSize = 10; // 수집할 페이지의 갯수 기본 10 페이지
        cafeBoardScheduleService.saveAllBoardsPerPage(pageSize);
    }

}
