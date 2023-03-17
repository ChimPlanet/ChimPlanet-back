package com.wak.chimplanet.scheduler;

import com.wak.chimplanet.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverCafeBoardScheduler {

    private final BoardService boardService;

    /**
     * 10분 주기 naverBoardAPI 조회 스케줄러
     *
     * *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "* */10 * * * *")
    public void scheduleNaverCafeBoard() {
        log.info("scheduleNaverCafeBoard task cron jobs");
        // boardService.saveAllBoards(); // 임시로 주석처리
    }


}
