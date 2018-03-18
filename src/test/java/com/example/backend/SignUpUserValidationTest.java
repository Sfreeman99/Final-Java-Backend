package com.example.backend;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SignUpUserValidationTest {

    @Test
    public void test() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testPasswordDidNotMatch() {
        HashMap hasErrors = new HashMap();
        HashMap expectedErrors = new HashMap();
        ArrayList errorList = new ArrayList();
        hasErrors.put("has-errors", true);
        expectedErrors.put("passworderrors", "Password did not match");
        errorList.add(expectedErrors);
        hasErrors.put("errors", errorList);
        User Bill = new User(
                "Billy",
                "Billy",
                "Bob",
                "billybob@gmail.com",
                "bill123",
                "bill"
        );
        SignUpUserValidation newUser = new SignUpUserValidation(Bill);
        assertEquals(true, newUser.validationErrors().get("has-errors"));
    }

    @Test
    public void testUsernamehasSpecialCharacters() {
        User Bill = new User(
                "Billy>",
                "Billy",
                "Bob",
                "billybob@gmail.com",
                "bill123",
                "bill123"
        );
        SignUpUserValidation userValidation = new SignUpUserValidation(Bill);
        userValidation.validationErrors().get("errors");
        ArrayList expectedErrorList = new ArrayList();
        HashMap expectedHashMap = new HashMap();
        expectedHashMap.put("usernameerrors", "Username must not contain any special characters");
        expectedErrorList.add(expectedHashMap);
        Object errors = userValidation.validationErrors().get("errors");
        assertEquals(true, userValidation.validationErrors().get("has-errors"));
        assertEquals("[{usernameerrors=Username must not contain any special characters}]", errors.toString());

    }
//    @Test
//    public void testHasBothUsernameAndPasswordErrors(){
//        User Bill = new User(
//                "Billy>",
//                "Billy",
//                "Bob",
//                "billybob@gmail.com",
//                "bill123",
//                "bill12"
//        );
//        SignUpUserValidation userValidation = new SignUpUserValidation(Bill);
//        ArrayList expectedErrorList = new ArrayList();
//        HashMap expectedHashMap = new HashMap();
//        expectedHashMap.put("usernameerrors", "Username must not contain any special characters");
//        expectedErrorList.add(expectedHashMap);
//        Object errors = userValidation.isValid().get("errors");
//        assertEquals(true, userValidation.isValid().get("has-errors"));
//        assertEquals(
//                "[{passworderrors=Passwords did not match,  usernameerrors=Username must not contain any special characters}]", errors;
//
//    }


}