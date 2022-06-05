package com.github.pjm03.humansystem.human;

import java.util.List;

public interface HumanDslRepository {
    List<Human> findHuman(String name, String birthday, String birthdayTime, String idNumber, Human.Sex sex);
}
