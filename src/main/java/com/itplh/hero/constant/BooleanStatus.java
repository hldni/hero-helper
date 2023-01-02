package com.itplh.hero.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gxfgh
 * @date 2023/1/2 11:46
 * @since test
 */
@Getter
@AllArgsConstructor
public enum BooleanStatus {
    TRUE("1", "true"),
    FALSE("0", "false"),
    ;

    String value;
    String text;
}
