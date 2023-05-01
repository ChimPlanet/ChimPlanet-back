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

    private final BoardService boardService;
    private final CafeBoardScheduleService cafeBoardScheduleService;

    /**
     * 스케줄러 강제 실행을 위한 메서드
     */
    @GetMapping("/scheduler/start")
    public int scheduleStart() {
        log.info("scheduleNaverCafeBoard task exec");
        List<Board> boardList = boardService.saveAllBoards();
        // cafeBoardScheduleService.saveAllBoardsPerPage();
        return boardList.size();
    }

    /**
     * 10분 주기 naverBoardAPI 조회 스케줄러
     *
     * *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 0 */4 * * *")
    public void scheduleCafeBoard() {
        log.info("scheduleNaverCafeBoard task cron jobs");
        boardService.saveAllBoards();
        // cafeBoardScheduleService.saveAllBoardsPerPage();
    }

}
