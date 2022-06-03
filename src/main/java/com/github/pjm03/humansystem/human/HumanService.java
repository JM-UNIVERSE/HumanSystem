package com.github.pjm03.humansystem.human;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class HumanService {
    private final SimpleDateFormat BIRTHDAY_FORMAT = new SimpleDateFormat("yyMMdd");
    private final HumanRepository humanRepository;

    public String getFullIdNumber(Human human, boolean hyphen) {
        String birthday = BIRTHDAY_FORMAT.format(new Date(human.getBirthday()));
        return birthday.substring(2) + (hyphen ? "-" : "") + human.getIdNumber();
    }

    public Human createHuman(String name, String birthday, String birthdayTime, Sex sex) {
        if (birthday.length() != 8 || !birthday.matches("[0-9]+")) {
            throw new IllegalArgumentException("'birthday' 필드는 8자의 정수(ex. 19700101)여야 합니다.");
        }
        if (birthdayTime.length() != 6 || !birthdayTime.matches("[0-9]+")) {
            throw new IllegalArgumentException("'birthdayTime' 필드는 6자의 정수(ex. 235959) 여야 합니다.");
        }

        int year = Integer.parseInt(birthday.substring(0, 4));
        int sexCode = 1 + sex.ordinal() + (year >= 2000 ? 2 : 0);
        int randomCode = new Random().nextInt(1000000);

        String idNumber = String.format("%d%06d", sexCode, randomCode);

        return humanRepository.save(new Human(name, birthday, birthdayTime, idNumber, sex));
    }

    public List<Human> findHuman(String name, String birthday, String birthdayTime, String idNumber, Sex sex) {
        return humanRepository.findHuman(name, birthday, birthdayTime, idNumber, sex);
    }

    public Human findHuman(String idNumber) {
        return humanRepository.findByIdNumber(idNumber);
    }
}
