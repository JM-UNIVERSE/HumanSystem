package com.github.pjm03.humansystem.human;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Schema(description = "사람 정보")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Human {
    @Schema(description = "이름", example = "홍길동")
    @NonNull
    private String name;

    @Schema(description = "태어난 날짜", example = "19700101")
    @NonNull
    private String birthday;

    @Schema(description = "태어난 시각", example = "235959")
    @Column(name = "birthday_time")
    @NonNull
    private String birthdayTime;

    @Schema(description = "주민등록번호(뒷자리)", example = "1234567")
    @Id
    @Column(name = "id_number")
    @NonNull
    private String idNumber;

    @Schema(description = "성별", allowableValues = {"MAN", "WOMAN", "OTHER"})
    @NonNull
    private Sex sex;
}