package com.wak.chimplanet.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {
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

}
