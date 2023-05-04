package com.wak.chimplanet.service;

import com.wak.chimplanet.common.util.Utility;
import com.wak.chimplanet.entity.*;
import com.wak.chimplanet.naver.NaverCafeArticleApi;
import com.wak.chimplanet.repository.OfficialBoardReporitory;
import com.wak.chimplanet.repository.TagObjRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //생성자 주입
public class OfficialBoardService {

    private final NaverCafeArticleApi naverCafeAtricleApi;
    private final OfficialBoardReporitory officialBoardReporitory;
    private final BoardService boardService;
    private final TagObjRepository tagObjRepository;

    public List<OfficialBoard> getAllOfiicialBoard(OfficialBoard officialBoard) {
        return officialBoardReporitory.findAll();
    }

    @Transactional
    public List<OfficialBoard> saveOfficialBoard(OfficialBoard officialBoard) {

        BoardDetail boardDetail = naverCafeAtricleApi.getNaverCafeArticleOne(officialBoard.getArticleId());

        System.out.print("Board Detail :: ");
        System.out.println(boardDetail);

        List<TagObj> tagList = tagObjRepository.findAll();

        List<TagObj> tags = Utility.categorizingTag(boardDetail.getContent(), tagList);

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
