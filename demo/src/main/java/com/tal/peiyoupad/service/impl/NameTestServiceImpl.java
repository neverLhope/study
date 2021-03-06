package com.tal.peiyoupad.service.impl;

import com.tal.peiyoupad.dao.INameTestDAO;
import com.tal.peiyoupad.entity.NameTestEntity;
import com.tal.peiyoupad.service.NameTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author SunGuiyong
 */
@Service
@EnableAsync
public class NameTestServiceImpl implements NameTestService {

  @Autowired
  private INameTestDAO nameTestDAO;
  @Autowired
  private AsyncTaskService asyncTaskService;
  //会出现循环依赖的问题
//    @Autowired
//    private NameTestServiceImpl nameTestServiceImpl;

  @Override
  public List<NameTestEntity> getNameTestByName(String name) {
    return nameTestDAO.getListByName(name);
  }

  @Override
  public void updateByName(Date updateTime, String name) {
    nameTestDAO.updateByName(updateTime, name);
  }

  @Override
  public boolean insertDB(String name) {
    Integer subName = getSubName(name);
    NameTestEntity entity = new NameTestEntity(name, subName);
    entity.setId((long) 34);
    nameTestDAO.save(entity);
    return true;
  }

  @Override
  public List<NameTestEntity> getList(String name) {
    return nameTestDAO.getTop10ByNameContaining(name);
  }

  @Override
  public List<NameTestEntity> testCaseInSql(String name, String sub) {

    return nameTestDAO.getByMultiCondition(name, sub);
  }

  @Override
  public boolean deleteRecord(String name) {
    nameTestDAO.deleteByName(name);
    return true;
  }

  @Override
  public List<NameTestEntity> getModRes(BigInteger num) {
    return nameTestDAO.getByMod(num);
  }

  @Override
  public List<NameTestEntity> getByInSQL(List<Integer> isList) {
    return nameTestDAO.selectByInSQL(isList);
  }

  @Override
  public void asyncTest() {
    System.out.println("async test 1");
    asyncTest1();
    System.out.println("async test 2");
    asyncTaskService.asyncTest();
    System.out.println("async test 3");
    System.out.println("async test 4");
    asyncTaskService.asyncTest();
    System.out.println("async test 5");
  }

  @Override
  public List<NameTestEntity> getPartOfData() {
    return nameTestDAO.getPart();
  }

  @Async
  public void asyncTest1() {
    System.out.println("async test1 1");
    try {
      Thread.sleep(5000);
    } catch (Exception e) {
    }
    System.out.println("async test1 2");
  }

  /**
   * 看是否有重名
   */
  private Integer getSubName(String name) {
    NameTestEntity entity = nameTestDAO.getFirstByName(name);
    if (null == entity) {
      return null;
    }
    if (StringUtils.isEmpty(entity.getSub())) {
      return 1;
    } else {
      Integer nums = Integer.valueOf(entity.getSub()) + 1;
      return nums;
    }
  }
}




