package com.kuuasema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by DuyAnhPham on 14/03/2017.
 */
class ControllerError {
    HttpStatus httpStatus;
    HsError hsError;

    static class HsError {
        String error;

        public HsError(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public ControllerError(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.hsError = new HsError(message);
    }

    public ResponseEntity<?> build() {
        return ResponseEntity.status(this.httpStatus).body(this.hsError);
    }
}
