package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.util.TxidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    @Autowired
    private TxidGenerator txidGenerator;

    protected ResponseEntity<ApiResponse> responseBuilder(Object data, HttpStatus httpStatus) {
        ApiResponse apiResponse = ApiResponse.SetSuccessStatus.builder()
                .txid(txidGenerator.getTxid())
                .status(httpStatus)
                .message(ApiResultStatus.REQUEST_SUCCESS.getMessage())
                .data(data)
                .build();

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
