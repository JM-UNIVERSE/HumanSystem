package com.github.pjm03.humansystem.human;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Human {
    @NonNull private String name;

    @NonNull private String birthday;
    @Column(name = "birthday_time")
    @NonNull private String birthdayTime;

    @Id
    @Column(name = "id_number")
    @NonNull private String idNumber;

    @NonNull private Sex sex;
}