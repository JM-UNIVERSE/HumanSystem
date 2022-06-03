package com.github.pjm03.humansystem.human;

import com.github.pjm03.humansystem.api.ApiResult;
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<Object> handleMissingParam(MissingServletRequestParameterException e) {
        return ApiResult.fail(HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiResult<Object> handleException(InvalidDataAccessApiUsageException e) {
        return ApiResult.fail(HttpStatus.PRECONDITION_FAILED, e.getCause().getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ApiResult<Object> handleException(Throwable t) {
        t.printStackTrace();
        return ApiResult.fail(HttpStatus.INTERNAL_SERVER_ERROR, "서버측에서 오류 발생. 관리자에게 문의 바랍니다.");
    }

    @GetMapping("/create")
    public ApiResult<?> createHuman(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String birthday,
            @RequestParam(required = false, defaultValue = "") String birthdayTime,
            @RequestParam Sex sex
    ) {
        return ApiResult.success(humanService.createHuman(name, birthday, birthdayTime, sex));
    }

    @GetMapping
    public ApiResult<?> findHuman(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String birthday,
            @RequestParam(required = false) String birthdayTime,
            @RequestParam(required = false) String idNumber,
            @RequestParam(required = false) Sex sex
    ) {
        List<Human> humanList = humanService.findHuman(name, birthday, birthdayTime, idNumber, sex);
        if (humanList.size() > 0) return ApiResult.success(humanList);
        else return ApiResult.fail(HttpStatus.NOT_FOUND, "정보를 찾을 수 없습니다.");
    }

    @GetMapping("/{idNumber}")
    public ApiResult<?> findHumanByIdNUmber(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) return ApiResult.success(human);
        else return ApiResult.fail(HttpStatus.NOT_FOUND, "정보를 찾을 수 없습니다.");
    }
}
