package et.com.movieReview.constants;

import et.com.movieReview.dto.ResponseDto.ResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ApiMessages {


    public ResponseDTO<?> successMessageWithMessageOnly(String responseMessage) {
        return new ResponseDTO<>(
                ResponseCodes.SUCCESS_CODE,
                responseMessage);
    }

    public ResponseDTO<?> errorMessage(String responseMessage) {
        return new ResponseDTO<>(
                ResponseCodes.FAILURE_CODE,
                responseMessage);
    }

    public ResponseDTO<?> exceptionMessage(String message) {
        return new ResponseDTO<>(
                ResponseCodes.SYS_MALFUNCTION_CODE,
                message);
    }

    public ResponseDTO<?> recordNotFound(String message) {
        return new ResponseDTO<>(
                ResponseCodes.RECORD_NOT_FOUND,
                message);
    }

    public ResponseDTO<?> notEligible(String message) {
        return new ResponseDTO<>(
                ResponseCodes.INELIGIBLE_CODE,
                message);
    }

    public ResponseDTO<?> recordNotFoundNoData(String message) {
        return new ResponseDTO<>(
                ResponseCodes.RECORD_NOT_FOUND,
                message,
                new ArrayList<>());
    }

    public ResponseDTO<?> successMessageWithData(Object data) {
        return new ResponseDTO<>(
                ResponseCodes.SUCCESS_CODE,
                RecordStatus.SUCCESS,
                Arrays.asList(data));
    }

    public ResponseDTO<?> successMessageWithListData(List<?> dataList) {
        return new ResponseDTO<>(
                ResponseCodes.SUCCESS_CODE,
                RecordStatus.SUCCESS,
                dataList);
    }

    public ResponseDTO<?> successMessageWithDataAndSummary(Object data, Object summary) {
        return new ResponseDTO<>(
                ResponseCodes.SUCCESS_CODE,
                RecordStatus.SUCCESS,
                Arrays.asList(data),
                Arrays.asList(summary));
    }
    
    public ResponseDTO<?> errorMessage(String responseCode, String responseMessage) {
        return new ResponseDTO<>(
                responseCode,
                responseMessage,
                new ArrayList<>());
    }

    public ResponseDTO<?> errorMessageWithData(String responseCode, String responseMessage, Object data) {
        return new ResponseDTO<>(
                responseCode,
                responseMessage,
                Arrays.asList(data));
    }

    public ResponseDTO<?> errorMessageWithData(Object data) {
        return new ResponseDTO<>(
                ResponseCodes.FAILURE_CODE,
                RecordStatus.FAILED,
                Arrays.asList(data));
    }
}
