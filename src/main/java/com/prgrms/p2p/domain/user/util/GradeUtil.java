package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.entity.Grade;

public class GradeUtil {

  public static String getGrade(int score){

    if(score< Grade.BRONZE.getScore()){
      return Grade.BRONZE.name();
    }else if(score < Grade.SLIVER.getScore()){
      return Grade.SLIVER.name();
    }else if(score < Grade.GOLD.getScore()){
      return Grade.GOLD.name();
    }else if(score < Grade.PLATINUM.getScore()){
      return Grade.PLATINUM.name();
    }else if(score<Grade.DIAMOND.getScore()){
      return Grade.DIAMOND.name();
    }else{
      return Grade.MASTER.name();
    }
  }
}