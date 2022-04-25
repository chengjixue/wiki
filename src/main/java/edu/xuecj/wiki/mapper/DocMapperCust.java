package edu.xuecj.wiki.mapper;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 11:32
 */
public interface DocMapperCust {
    public void increaseViewCount(@Param("id") Long id);
}
