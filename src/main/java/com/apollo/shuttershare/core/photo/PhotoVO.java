package com.apollo.shuttershare.core.photo;

import lombok.Data;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 3:24 PM
 */
@Data
public class PhotoVO {
    private Long id;
    private Long userId;
    private Long createAt;
    private Double latitude;
    private Double longitude;
}
