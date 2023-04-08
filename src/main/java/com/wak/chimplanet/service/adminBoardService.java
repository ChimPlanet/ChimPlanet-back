package com.wak.chimplanet.service;


import com.wak.chimplanet.dto.responseDto.admin.AdminGetBoardResponseDto;
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
    public AdminGetBoardResponseDto updateBoard() {
        // Board board = adminBoardRepository.findByArticleId();
        return null;
    }
}
