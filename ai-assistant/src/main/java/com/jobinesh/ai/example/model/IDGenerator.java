package com.jobinesh.ai.example.model;

public class IDGenerator {
    public static int seededId =1000;
    public static String generateUUID(){
        seededId+=1000;
        return String.valueOf(seededId);
    }
}
