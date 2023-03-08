package com.d204.rumeet.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sample<T> {

    String flag;
    String msg;
    T data;

    public ResponseEntity<?> builder() {
        return new ResponseEntity<Sample>(this, HttpStatus.OK);
    }
}
