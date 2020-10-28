package com.example.ea2soa.data.model;


import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("env")
    private String env;

    @SerializedName("name")
    private String name;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("dni")
    private Long dni;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("commission")
    private Long commission;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCommission() {
        return commission;
    }

    public void setCommission(Long commission) {
        this.commission = commission;
    }

    /**
     * Valida los datos de un usuario antes de mandar a registrarlo
     * @return
     */
    public boolean validate(){

        if(this.name.isEmpty() || this.lastname.isEmpty() || this.dni == null || this.email.isEmpty() || this.password.isEmpty() || this.password.length() < 8 || this.commission == null){
            return false;
        }
        if (this.email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(this.email).matches();
        } else {
            return !this.email.trim().isEmpty();
        }

    }
}
