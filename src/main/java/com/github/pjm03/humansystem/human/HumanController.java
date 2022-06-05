package com.github.pjm03.humansystem.human;

import com.github.pjm03.humansystem.api.Response;
import com.github.pjm03.humansystem.exception.HumanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("human")
@RestControllerAdvice
public class HumanController {
    private final HumanService humanService;

    @ExceptionHandler(HumanNotFoundException.class)
    public ResponseEntity<?> handleNotFoundHuman(HumanNotFoundException e) {
        return Response.fail(HttpStatus.NOT_FOUND, "해당하는 정보를 찾을 수 없습니다.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException e) {
        return Response.fail(HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> handleException(InvalidDataAccessApiUsageException e) {
        return Response.fail(HttpStatus.PRECONDITION_FAILED, e.getCause().getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(Throwable t) {
        t.printStackTrace();
        return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR, "서버측에서 오류 발생. 관리자에게 문의 바랍니다.");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createHuman(
            @RequestParam String name,
            @RequestParam String birthday,
            @RequestParam String birthdayTime,
            @RequestParam Human.Sex sex
    ) {
        return Response.success(humanService.createHuman(name, birthday, birthdayTime, sex));
    }

    @GetMapping
    public ResponseEntity<?> findHuman(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String birthdayTime,
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) Human.Sex sex
    ) {
        List<Human> humanList = humanService.findHuman(name, birthday, birthdayTime, idNumber, sex);
        if (humanList.size() > 0) return Response.success(humanList);
        else throw new HumanNotFoundException();
    }

    @GetMapping("/{idNumber}/serial")
    public ResponseEntity<?> getSerial(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) {
            String serial = humanService.serializeToString(human.getName(), human.getBirthday(), human.getBirthdayTime(), human.getIdNumber(), human.getSex());
            return Response.success(serial);
        } else throw new HumanNotFoundException();
    }

    @GetMapping("/deserialize/{serial}")
    public ResponseEntity<?> deserialize(
            @PathVariable String serial
    ) {
        Human human = humanService.deserialize(serial);
        if (human != null) return Response.success(human);
        else throw new HumanNotFoundException();
    }

    @GetMapping("/{idNumber}")
    public ResponseEntity<?> findHumanByIdNUmber(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) return Response.success(human);
        else throw new HumanNotFoundException();
    }
}
