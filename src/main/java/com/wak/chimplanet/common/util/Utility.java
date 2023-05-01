package com.wak.chimplanet.common.util;

import com.wak.chimplanet.entity.TagObj;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Utility {

    /**
     * 문자열이 비어있는지 확인
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 태그명 찾기 - KMP 알고리즘 사용
     */
    public boolean kmpSearch(String content, String tag) {
        int n = content.length();
        int m = tag.length();
        int[] pi = getPi(tag);

        int idx = 0; // 글자수
        for (int i = 0; i < n; i++) {
            while (idx > 0 && content.charAt(i) != tag.charAt(idx)) {
                idx = pi[idx - 1];
            }

            // 글자가 대응되는 경우
            if (content.charAt(i) == tag.charAt(idx)) {
                if (idx == m - 1) {
                    log.info("{} 번째에서 태그 발견 {}", i-idx-1, i+1);
                    return true;
                } else {
                    idx++;
                }
            }
        }
        return false;
    }

    /**
     * tag의 접두사와 접미사가 일치하는 길의를 계산하는 배열 pi 를 리턴
     */
    public int[] getPi(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];

        int j = 0;
        for (int i = 1; i < m; i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                pi[i] = ++j;
            }
        }
        return pi;
    }


    /**
     * 게시글에서 태그 리스트 분류하기
     */
    public static List<TagObj> categorizingTag(String content, List<TagObj> tags) {
        if(content.isEmpty()) return null;

        // 문장에서 찾은 태그명
        Set<String> foundTags = new HashSet<>();

        // 문장에서 찾은 태그 코드
        Set<TagObj> findTagSet = new HashSet<>();

        for(TagObj tag : tags) {
            if(content.contains(tag.getTagName())) {
                foundTags.add(tag.getTagName());
                findTagSet.add(tag);
            }
        }

        log.info("찾은 태그명 : {}", foundTags.toString());

        return new ArrayList<>(findTagSet);
    }

}
