package com.wak.chimplanet.service;


import com.wak.chimplanet.dto.requestDto.BoardTagRequestDto;
import com.wak.chimplanet.dto.requestDto.admin.AdminBoardUpdateRequestDto;
import com.wak.chimplanet.dto.responseDto.admin.AdminUpdateBoardResponseDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.AdminBoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class adminBoardService {

    private final AdminBoardRepository adminBoardRepository;
    private final TagObjRepository tagRepository;


    /**
     * 관리자 - 게시판 정보 업데이트
     */
    @Transactional
    public Board updateBoard(AdminBoardUpdateRequestDto dto) {
        Board findBoard = adminBoardRepository.findBoardWithTags(dto.getArticleId())
            .orElseThrow(() -> new IllegalArgumentException("Board not found with id " + dto.getArticleId()));

        List<TagObj> tags = tagRepository.findAllByChildId(dto.getBoardTags().stream()
            .map(BoardTagRequestDto::getChildTagId)
            .collect(Collectors.toList()));

        List<BoardTag> boardTags = new ArrayList<>();
        List<BoardTag> existingBoardTagId = findBoard.getBoardTags(); // 기존 BoardTag 엔티티 리스트 가져오기

        for(TagObj tag : tags) {

        }

        findBoard.updateAdminBoard(dto.getIsEnd(), boardTags);

        return findBoard;
    }
}
