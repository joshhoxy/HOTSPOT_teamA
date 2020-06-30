package com.example.se_project;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegisterUnitTest {
    private RegisterActivity registerActivity;

    @Before
    public void setUp(){
        registerActivity = new RegisterActivity();
    }

    @Test
    public void test() {

        boolean len_result = registerActivity.len_check("1", "12");
        assertEquals(false, len_result);

        boolean pw_result = registerActivity.pw_check("1", "1");
        assertEquals(true, pw_result);

    }
}
