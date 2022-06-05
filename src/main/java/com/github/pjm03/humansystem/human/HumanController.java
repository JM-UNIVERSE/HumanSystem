package com.github.pjm03.humansystem.human;

import com.github.pjm03.humansystem.api.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Human", description = "사람 정보 API")
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

    @Operation(
            summary = "사람 정보 생성",
            description = "이름(name), 태어난 날짜(birthday, ex. 19700101), 태어난 시간(birthdayTime, ex. 235959), 성별(sex)을 이용하여 새로운 사람 정보를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공",
                            content = @Content(
                                    mediaType = "application/json"
                            )),
                    @ApiResponse(responseCode = "412", description = "태어난 날짜(birthday) / 태어난 시각(birthdayTime) 값 오류로 인한 생성 실패",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResult.class)
                            ))
            }
    )
    @PostMapping("/create")
    public ApiResult<Human> createHuman(
            @RequestParam String name,
            @RequestParam String birthday,
            @RequestParam String birthdayTime,
            @RequestParam Human.Sex sex
    ) {
        return ApiResult.success(humanService.createHuman(name, birthday, birthdayTime, sex));
    }

    @Operation(
            summary = "사람 정보 가져오기",
            description = "이름(name), 태어난 날짜(birthday), 태어난 시각(birthdayTime), 주민등록번호(idNumber), 성별(sex) 중 최소 1가지 이상의 정보를 이용하여 해당하는 사람들을 찾습니다."
    )
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
        else return ApiResult.fail(HttpStatus.NOT_FOUND, "정보를 찾을 수 없습니다.");
    }

    @Operation(
            summary = "해당하는 사람의 시리얼 확인",
            description = "주민등록번호(idNumber)를 이용하여 해당하는 사람 1명의 시리얼을 받아옵니다."
    )
    @GetMapping("/{idNumber}/serial")
    public ApiResult<String> getSerial(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) {
            String serial = humanService.serializeToString(human.getName(), human.getBirthday(), human.getBirthdayTime(), human.getIdNumber(), human.getSex());
            return ApiResult.success(serial);
        } else return ApiResult.fail(HttpStatus.NOT_FOUND, "정보를 찾을 수 없습니다.");
    }

    @Operation(
            summary = "주민등록번호를 이용한 사람 정보 가져오기",
            description = "주민등록번호(idNumber)를 이용하여 해당하는 사람 1명의 정보를 가져옵니다."
    )
    @GetMapping("/{idNumber}")
    public ApiResult<Human> findHumanByIdNUmber(
            @PathVariable String idNumber
    ) {
        Human human = humanService.findHuman(idNumber);
        if (human != null) return ApiResult.success(human);
        else return ApiResult.fail(HttpStatus.NOT_FOUND, "정보를 찾을 수 없습니다.");
    }
}
