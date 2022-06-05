package com.github.pjm03.humansystem.human;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.github.pjm03.humansystem.human.QHuman.human;

@RequiredArgsConstructor
public class HumanDslRepositoryImpl implements HumanDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression andOrCreate(BooleanExpression expr, BooleanExpression newExpr) {
        if (expr == null) return newExpr;
        else return expr.and(newExpr);
    }

    @Override
    public List<Human> findHuman(String name, String birthday, String birthdayTime, String idNumber, Human.Sex sex) {
        BooleanExpression whereExpression = null;
        if (name != null && !name.isEmpty()) {
            whereExpression = andOrCreate(null, human.name.eq(name));
        }
        if (birthday != null && !birthday.isEmpty()) {
            whereExpression = andOrCreate(whereExpression, human.birthday.eq(birthday));
        }
        if (birthdayTime != null && !birthdayTime.isEmpty()) {
            whereExpression = andOrCreate(whereExpression, human.birthdayTime.eq(birthdayTime));
        }
        if (idNumber != null && idNumber.length() == 7) {
            whereExpression = andOrCreate(whereExpression, human.idNumber.eq(idNumber));
        }
        if (sex != null) {
            whereExpression = andOrCreate(whereExpression, human.sex.eq(sex));
        }

        if (whereExpression == null) throw new IllegalArgumentException("최소 한 가지 이상의 검색 조건이 필요합니다.");

        return jpaQueryFactory.selectFrom(human)
                .where(whereExpression)
                .fetch();
    }
}
