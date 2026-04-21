package com.app.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
public class SampleServiceTest {
    @Autowired
    private TempService tempService;

    @Test
    @DisplayName("doAdd - 정상 반환: @Before, @Around, @AfterReturning, @After 순서 확인")
    public void doAdd_정상반환() {
        // given
        String str1 = "3";
        String str2 = "5";

        // when
        int result = tempService.doAdd(str1, str2);

        // then
        assertThat(result).isEqualTo(8);
        log.info("▶ doAdd 결과: {}", result);
    }

    @Test
    @DisplayName("doSubtract - 정상 반환: @AfterReturning에서 반환값 로깅 확인")
    public void doSubtract_정상반환() {
        // given
        String str1 = "10";
        String str2 = "4";

        // when
        int result = tempService.doSubtract(str1, str2);

        // then
        assertThat(result).isEqualTo(6);
        log.info("▶ doSubtract 결과: {}", result);
    }

    @Test
    @DisplayName("doAddWithInvalidInput - 예외 발생: @AfterThrowing, @Around 예외 처리 확인")
    public void doAddWithInvalidInput_예외발생() {
        // given — 숫자로 변환 불가한 문자열
        String invalidInput = "abc";
        String str2 = "2";

        // when & then — NumberFormatException 발생 검증
        assertThatThrownBy(() -> tempService.doAddWithInvalidInput(invalidInput, str2))
                .isInstanceOf(NumberFormatException.class);
        log.info("▶ 예외 발생 정상 확인");
    }
}
