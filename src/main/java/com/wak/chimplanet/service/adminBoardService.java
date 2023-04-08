package com.wak.chimplanet.service;


import com.wak.chimplanet.dto.responseDto.AdminUpdateBoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.repository.AdminBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class adminBoardService {

    private final AdminBoardRepository adminBoardRepository;


    @Transactional
    public AdminUpdateBoardResponseDto updateBoard() {
        // Board board = adminBoardRepository.findByArticleId();
        return null;
    }
}
