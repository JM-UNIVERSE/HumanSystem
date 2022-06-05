package com.github.pjm03.humansystem.human;

import com.github.pjm03.humansystem.exception.HumanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class HumanService {
    private final SimpleDateFormat BIRTHDAY_FORMAT = new SimpleDateFormat("yyMMdd");
    private final HumanRepository humanRepository;

    public String getFullIdNumber(Human human, boolean hyphen) {
        String birthday = BIRTHDAY_FORMAT.format(new Date(human.getBirthday()));
        return birthday.substring(2) + (hyphen ? "-" : "") + human.getIdNumber();
    }

    private byte[] idNumberHashing(String idNumber) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(idNumber.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] serialize(String name, String birthday, String birthdayTime, String idNumber, Human.Sex sex) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            byte[] idNumHash = idNumberHashing(idNumber);
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.writeUTF(name);
            oos.writeUTF(birthday);
            oos.writeUTF(birthdayTime);
            oos.writeInt(sex.ordinal());
            oos.write(idNumHash);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("HumanService.serialize: IOException 발생", e);
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("HumanService.serialize>finally: IOException 발생.");
            }
        }
    }

    public String serializeToString(String name, String birthday, String birthdayTime, String idNumber, Human.Sex sex) {
        return Base64.encodeBase64String(serialize(name, birthday, birthdayTime, idNumber, sex));
    }

    public Human deserialize(byte[] serial) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(serial);
            ois = new ObjectInputStream(new BufferedInputStream(bais));
            String name = ois.readUTF();
            String birthday = ois.readUTF();
            String birthdayTime = ois.readUTF();
            Human.Sex sex = Human.Sex.values()[ois.readInt()];
            int hash1 = Arrays.hashCode(ois.readAllBytes());
            List<Human> humans = findHuman(name, birthday, birthdayTime, null, sex);

            Human human = null;
            if (humans.size() > 1) {
                human = humans.stream().filter(h -> {
                    int hash2 = Arrays.hashCode(idNumberHashing(h.getIdNumber()));
                    return hash1 == hash2;
                }).findAny().orElse(null);
            } else if (humans.size() > 0) {
                human = humans.get(0);
            }

            return human;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("HumanService.serialize: IOException 발생", e);
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("HumanService.deserialize>finally: IOException 발생.");
            }
        }
    }

    public Human deserialize(String serial) {
        return deserialize(serial.getBytes(StandardCharsets.UTF_8));
    }

    public Human createHuman(String name, String birthday, String birthdayTime, Human.Sex sex) {
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

    public List<Human> findHuman(String name, String birthday, String birthdayTime, String idNumber, Human.Sex sex) {
        return humanRepository.findHuman(name, birthday, birthdayTime, idNumber, sex);
    }

    public Human findHuman(String idNumber) {
        return humanRepository.findByIdNumber(idNumber);
    }
}
