package com.atguigu.service;

import com.atguigu.pojo.Member;

import java.util.List;

/**
 * @author: 13936
 * @date: 2022/5/13 15:53
 * @description:
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);
}
