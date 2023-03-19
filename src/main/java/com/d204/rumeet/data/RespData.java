package com.d204.rumeet.data;

import com.d204.rumeet.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespData<T> {
    String flag;
    String msg;
    T data;

    public RespData(ErrorEnum error) {
        this.flag = error.flag;
        this.msg = error.msg;
        this.data = null;
    }

    public ResponseEntity<?> builder(){
        return new ResponseEntity<RespData>(this, HttpStatus.OK);
    }
}
