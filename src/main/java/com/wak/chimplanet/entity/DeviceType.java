package com.wak.chimplanet.entity;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 기기 별 이미지 타입 :  MOBILE, PC
 */
public enum DeviceType {
    MOBILE, PC;

    private static String deviceType;

    public static DeviceType of(String codeStr) {
        if(deviceType == null) {
            throw new IllegalArgumentException();
        }
        for(DeviceType deviceType : DeviceType.values()) {
            return  deviceType;
        }
        throw new IllegalArgumentException("일치하는 DeviceType 코드가 없습니다");
    }

}
