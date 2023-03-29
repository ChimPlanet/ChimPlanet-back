package com.wak.chimplanet.service;

import com.wak.chimplanet.entity.BoardDetail;
import com.wak.chimplanet.entity.OfficialBoard;
import com.wak.chimplanet.naver.NaverCafeAtricleApi;
import com.wak.chimplanet.repository.OfficialBoardReporitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //생성자 주입
public class OfficialBoardService {

    private final NaverCafeAtricleApi naverCafeAtricleApi;

    private final OfficialBoardReporitory officialBoardReporitory;

    public List<OfficialBoard> getAllOfiicialBoard(OfficialBoard officialBoard) {
        return officialBoardReporitory.findAll();
    }

    public List<OfficialBoard> saveOfficialBoard(OfficialBoard officialBoard) {

        //Naver Api 조회확인
        //OfficialBoard getOfficialBoard = naverCafeAtricleApi.getNaverCafeArticleOne(officialBoard.getArticleId());

        //Api로 조회한 Official article 저장
        officialBoardReporitory.save(officialBoard);

        return officialBoardReporitory.findAll();
    }
}
