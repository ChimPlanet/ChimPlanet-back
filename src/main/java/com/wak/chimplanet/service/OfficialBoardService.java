package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.*;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.OfficialBoardReporitory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //생성자 주입
public class OfficialBoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;
    private final OfficialBoardReporitory officialBoardReporitory;
    private final BoardService boardService;

    public List<OfficialBoard> getAllOfiicialBoard(OfficialBoard officialBoard) {
        return officialBoardReporitory.findAll();
    }

    @Transactional
    public List<OfficialBoard> saveOfficialBoard(OfficialBoard officialBoard) {

        BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(officialBoard.getArticleId());

        List<TagObj> tags = boardService.categorizingTag(boardDetail.getContent());

        List<OfficialBoardTag> officialBoardTags = new ArrayList<>();

        for(TagObj tag : tags) {
            OfficialBoardTag officialBoardTag = OfficialBoardTag.createOfficialBoardTag(tag, officialBoard);
            officialBoardTags.add(officialBoardTag); // BoardTag 리스트에도 추가

        }

        officialBoard.setBoardTitle(boardDetail.getBoardTitle());
        officialBoard.setWriter(boardDetail.getWriter());
        officialBoard.setRedirectURL(boardDetail.getRedirectURL());
        officialBoard.setOfficialBoardTags(officialBoardTags);

//        Api로 조회한 Official article 저장
        officialBoardReporitory.save(officialBoard);

        return officialBoardReporitory.findAll();
    }

    public  List<OfficialBoard> deleteOfficialBoard(String articleId) {

        officialBoardReporitory.deleteById(articleId);

        return officialBoardReporitory.findAll();
    }
}
