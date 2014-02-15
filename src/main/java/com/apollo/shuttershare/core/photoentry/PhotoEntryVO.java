package com.apollo.shuttershare.core.photoentry;

import lombok.Data;

/**
 * Author: Daniel
 * Date: 2/10/14
 * Time: 4:11 PM
 */
@Data
public class PhotoEntryVO {
    private Long id;
    private Long photoId;
    private Long groupId;
    private Long userId;
    private Long createAt;
    private Boolean acknowledged;
}
