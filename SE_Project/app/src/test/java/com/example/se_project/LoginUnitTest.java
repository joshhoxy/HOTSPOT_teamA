package com.example.se_project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LoginUnitTest {

    private LoginActivity loginActivity;

    @Before
    public void setUp(){

        loginActivity = new LoginActivity();
    }
    @Test
    public void test() {

        boolean len_result = loginActivity.len_check("asdfasdf", "");
        assertEquals(true, len_result);

        boolean pw_result = loginActivity.pw_check("1", "1");
        assertEquals(true, pw_result);

    }


}
