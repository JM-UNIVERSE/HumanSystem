package com.github.pjm03.humansystem.human;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HumanRepository extends JpaRepository<Human, String>, HumanDslRepository{
    Human save(Human human);
    List<Human> findByName(String name);
    List<Human> findByBirthday(long birthday);
    List<Human> findBySex(Human.Sex sex);
    Human findByIdNumber(String idNumber);
}
