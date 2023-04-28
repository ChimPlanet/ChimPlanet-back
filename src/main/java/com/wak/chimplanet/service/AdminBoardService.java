package com.wak.chimplanet.service;


import com.wak.chimplanet.dto.requestDto.BoardTagRequestDto;
import com.wak.chimplanet.dto.requestDto.admin.AdminBoardUpdateRequestDto;
import com.wak.chimplanet.entity.Board;
import com.wak.chimplanet.entity.BoardTag;
import com.wak.chimplanet.entity.TagObj;
import com.wak.chimplanet.repository.AdminBoardRepository;
import com.wak.chimplanet.repository.TagObjRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;
    private final TagObjRepository tagRepository;


    /**
     * 관리자 - 게시판 정보 업데이트
     */
    @Transactional
    public Board updateBoard(AdminBoardUpdateRequestDto dto) {
        Board findBoard = adminBoardRepository.findBoardWithTags(dto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id " + dto.getArticleId()));

        // 기존 BoardTag 모두 삭제
        findBoard.getBoardTags().clear();

        List<TagObj> tags = tagRepository.findAllByChildId(dto.getBoardTags().stream()
                .map(BoardTagRequestDto::getChildTagId)
                .collect(Collectors.toList()));

        List<BoardTag> boardTags = new ArrayList<>();
        for(TagObj tag : tags) {
            boardTags.add(BoardTag.addBoardTag(tag, findBoard)); // BoardTag 리스트에도 추가
        }

        // insert 쿼리만 실행되고 delete 쿼리가 실행이 안되는 문제가 있어 하드코딩으로 잠시 대체합니다.
        // BoardTag 컬럼들을 복합키로 대체해야할 듯...
        adminBoardRepository.deleteBoardTags(dto.getArticleId());

        findBoard.updateAdminBoard(dto.getIsEnd(), boardTags);

        return findBoard;
    }
}
