package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAwardDao {

    int insert(Award award);

    Award queryAward(Integer awardId);

    String queryAwardConfigByAwardId(Integer awardId);

    String queryAwardKeyByAwardId(Integer awardId);
}
