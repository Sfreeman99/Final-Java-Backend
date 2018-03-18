package com.example.backend;


import java.util.ArrayList;
import java.util.HashMap;


public class SignUpUserValidation {
    public User newUser;


    public SignUpUserValidation(){}
    public SignUpUserValidation(User newUser) {
        this.newUser = newUser;
    }
    public boolean passwordMatches(){
        HashMap error = new HashMap();
        if (!newUser.Password1.equals(newUser.Password2))
            return false;
        return true;
    }
    public String passwordErrorMessage() {
        return "Password did not match";
    }

    public boolean usernameHasNoSpecialCharacters() {
        int match = 0;
        String[] username = newUser.Username.split("");
        for (int i = 0; i < username.length ; i++) {
            /* This regular expression matches anything that is not a word or letter */
            if (username[i].matches("[^-\\w\\s.@:,]")) {
                match += 1;
                break;
            }
        }
        if (match != 0)
            return false;
        return true;
    }
    public HashMap validationErrors(){
        HashMap hasErrors = new HashMap();
        ArrayList errorList = new ArrayList();
        if (!passwordMatches() || !usernameHasNoSpecialCharacters()) {
            HashMap allErrors = new HashMap();
            hasErrors.put("has-errors", true);
            if (!passwordMatches()) {
                allErrors.put("passworderrors", passwordErrorMessage());
            }
            if (!usernameHasNoSpecialCharacters()) {
                allErrors.put("usernameerrors", "Username must not contain any special characters");
            }
            errorList.add(allErrors);
            hasErrors.put("errors", errorList);
        } else
            hasErrors.put("has-error", false);
        return hasErrors;
    }
    public boolean isValid() {
        if (passwordMatches() && usernameHasNoSpecialCharacters())
            return true;
        return false;
    }
}
