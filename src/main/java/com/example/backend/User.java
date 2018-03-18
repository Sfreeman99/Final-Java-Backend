package com.example.backend;

public class User {
        public String Username;
        public String FirstName;
        public String LastName;
        public String Email;
        public String Password1;
        public String Password2;

        public User() {}
        public User(String Username, String FirstName, String LastName, String Email, String Password1, String Password2){
            this.FirstName = FirstName;
            this.LastName = LastName;
            this.Email = Email;
            this.Password1 = Password1;
            this.Password2 = Password2;
            this.Username = Username;
        }

        public String FirstName() {
            return this.FirstName;
        }

        public String LastName() {
            return this.LastName;
        }

        public String Email() {
            return this.Email;
        }
    }
