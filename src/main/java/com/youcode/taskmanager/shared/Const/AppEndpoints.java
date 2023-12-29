package com.youcode.taskmanager.shared.Const;


import org.springframework.stereotype.Component;

@Component
public class AppEndpoints {
    public static   final  String VERSION_1 = "/api/v1";
    public static final  String VERSION_2 = "/api/v2";
    public  static final  String VERSION_3 = "/api/v3";
    public static final  String VERSION = VERSION_1;
    public static final   String IDENTITY_DOCUMENT_TYPE = VERSION + "/identity-documents";
    public static  final  String FIREBASE_ENDPOINT = VERSION + "/firebase";
    public static final String MEMBER = VERSION + "/members";
    public static final String SEEDER = VERSION + "/seed";
    public static final String COMPETITION = VERSION + "/competitions";
    public static final String FISH = VERSION + "/fish";
    public static final String HUNTING = VERSION + "/hunting";
    public static final String LEVEL = VERSION + "/levels";
    public static final String RANKING = VERSION + "/rankings";
    public static final String GAME_PLAY = VERSION + "/game-play";
}
