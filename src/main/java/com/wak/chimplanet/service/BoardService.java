package com.wak.chimplanet.service;

import com.wak.chimplanet.batch.NaverCafeCrawler;
import com.wak.chimplanet.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final NaverCafeCrawler naverCafeCrawler;

    public List<Board> getAllBoardList() {
        return naverCafeCrawler.getNaverCafeBoard();
    }
}
