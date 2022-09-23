package com.example.userservice.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(UserEntity.class);
        this.queryFactory =queryFactory;
    }

    public Iterable<UserEntity> findByUserIds(List<String> userIds){
        QUserEntity userEntity = QUserEntity.userEntity;
        return queryFactory.select(userEntity)
                .from(userEntity)
                .where(userEntity.userId.in(userIds))
                .fetch();
    }
}
