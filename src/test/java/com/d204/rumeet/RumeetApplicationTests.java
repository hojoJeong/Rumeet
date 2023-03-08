package com.d204.rumeet;

import com.d204.rumeet.demo.data.RespData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RumeetApplicationTests {


    void test(){
        RespData<Integer> testx = new RespData<>();
        testx.setData(2);
        testx.setFlag("success");
        testx.setMsg("ok");
        Assertions.assertThat(testx.builder()).isNotNull();
        System.out.println("testx.builder() = " + testx.builder());
    }
}
