package com.spring.boot.demo.spring_boot.dao;

import com.spring.boot.demo.spring_boot.entity.NameTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author SunGuiyong
 */
@Repository
public interface INameTestDAO extends JpaRepository<NameTestEntity, Long> {

  /**
   * 通过名字获取list
   */
  List<NameTestEntity> getListByName(String name);

  /**
   * 通过名字获取
   */
  @Query(value = "select * from name_test where name = :name ORDER BY sub DESC LIMIT 1", nativeQuery = true)
  NameTestEntity getFirstByName(String name);

  /**
   * 更新数据 //没有使用@Param 怎么也是可以的，好怪~~
   */
  @Query(value = "update `test`.`name_test` set `sub`=999, `update_time`=:updateTime where `name`=:name", nativeQuery = true)
  @Modifying
  @Transactional(rollbackFor = Exception.class)
  void updateByName(@Param("updateTime") Date updateTime, @Param("name") String name);

  @Query(value = "select * from `test`.`name_test` where `name` like concat('%',:name,'%')"
      + "and (case when :sub is null then 1=1 \n"
      + "else `sub`=:sub end) limit 10", nativeQuery = true)
  List<NameTestEntity> getByMultiCondition(@Param("name") String name,
      @Param("sub") String subName);

  /**
   * 模糊查询
   *
   * @param name 公告名
   */
  List<NameTestEntity> getTop10ByNameContaining(String name);
}
