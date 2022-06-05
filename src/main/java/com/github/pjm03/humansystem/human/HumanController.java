package com.github.pjm03.humansystem.human;

import com.github.pjm03.humansystem.api.ApiResult;
import com.github.pjm03.humansystem.exception.HumanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("human")
@RestControllerAdvice
public class HumanController {
    private final HumanService humanService;

    @ExceptionHandler(HumanNotFoundException.class)
    public ApiResult<?> handleNotFoundHuman(HumanNotFoundException e) {
        return ApiResult.fail(HttpStatus.NOT_FOUND, "해당하는 정보를 찾을 수 없습니다.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<?> handleMissingParam(MissingServletRequestParameterException e) {
        return ApiResult.fail(HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiResult<?> handleException(InvalidDataAccessApiUsageException e) {
        return ApiResult.fail(HttpStatus.PRECONDITION_FAILED, e.getCause().getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ApiResult<?> handleException(Throwable t) {
        t.printStackTrace();
        return ApiResult.fail(HttpStatus.INTERNAL_SERVER_ERROR, "서버측에서 오류 발생. 관리자에게 문의 바랍니다.");
    }

    @PostMapping("/create")
    public ApiResult<Human> createHuman(
            @RequestParam String name,
            @RequestParam String birthday,
            @RequestParam String birthdayTime,
            @RequestParam Human.Sex sex
    ) {
        return ApiResult.success(humanService.createHuman(name, birthday, birthdayTime, sex));
    }

    @GetMapping
    public ApiResult<List<Human>> findHuman(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String birthdayTime,
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) Human.Sex sex
    ) {
        List<Human> humanList = humanService.findHuman(name, birthday, birthdayTime, idNumber, sex);
        if (humanList.size() > 0) return ApiResult.success(humanList);
        else throw new HumanNotFoundException();
    }

    @GetMapping("/{idNumber}/serial")
    public ApiResult<String> getSerial(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) {
            String serial = humanService.serializeToString(human.getName(), human.getBirthday(), human.getBirthdayTime(), human.getIdNumber(), human.getSex());
            return ApiResult.success(serial);
        } else throw new HumanNotFoundException();
    }

    @GetMapping("/deserialize/{serial}")
    public ApiResult<Human> deserialize(
            @PathVariable String serial
    ) {
        Human human = humanService.deserialize(serial);
        if (human != null) return ApiResult.success(human);
        else throw new HumanNotFoundException();
    }

    @GetMapping("/{idNumber}")
    public ApiResult<Human> findHumanByIdNUmber(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) return ApiResult.success(human);
        else throw new HumanNotFoundException();
    }
}
