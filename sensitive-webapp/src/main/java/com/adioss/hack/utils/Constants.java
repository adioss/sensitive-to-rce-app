package com.adioss.hack.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String DEFAULT_MICRO_SERVICE_URL;
    public static final String BYPASS_LOGIN;
    public static final String BYPASS_PASSWORD;
    public static final boolean HACK_ACTIVATED;

    static {
        DEFAULT_MICRO_SERVICE_URL = System.getenv("DEFAULT_MICRO_SERVICE_URL") != null ? System.getenv("DEFAULT_MICRO_SERVICE_URL") : "http://localhost:12345";
        BYPASS_LOGIN = System.getenv("BYPASS_LOGIN") != null ? System.getenv("BYPASS_LOGIN") : "mouaaaaaaaa";
        BYPASS_PASSWORD = System.getenv("BYPASS_PASSWORD") != null ? System.getenv("BYPASS_PASSWORD") : "wellllldone!!!!";
        HACK_ACTIVATED = System.getenv("HACK_ACTIVATED") == null || Boolean.parseBoolean(System.getenv("HACK_ACTIVATED"));
    }

    public static final List<String> GIF_URLS = Arrays.asList("https://i.giphy.com/media/LXONhtCmN32YU/200.webp",
            "https://i.giphy.com/media/LrmU6jXIjwziE/source.gif",
            "https://media.giphy.com/media/WQd5VxnCNsHja/giphy.gif",
            "https://media.giphy.com/media/tN69v9qyrweGI/giphy.gif",
            "https://i.giphy.com/media/6b9QApjUesyOs/giphy.webp",
            "https://i.giphy.com/media/QC1pvLZEHClmo/200.webp",
            "https://media.giphy.com/media/QG9DL5hAYZWVO/200w_s.gif",
            "https://i.giphy.com/media/Ju7l5y9osyymQ/giphy.webp",
            "https://i.giphy.com/media/25tY7H12n3ZxS/giphy.webp",
            "https://i.giphy.com/media/tqI9aaw8dtoYM/giphy.webp");

    public static final List<String> LYRICS_LINES = Arrays.asList("We're no strangers to love",
            "You know the rules and so do I",
            "A full commitment's what I'm thinking of",
            "You wouldn't get this from any other guy",
            "I just wanna tell you how I'm feeling",
            "Gotta make you understand",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry",
            "Never gonna say goodbye",
            "Never gonna tell a lie and hurt you",
            "We've known each other for so long",
            "Your heart's been aching but you're too shy to say it",
            "Inside we both know what's been going on",
            "We know the game and we're gonna play it",
            "And if you ask me how I'm feeling",
            "Don't tell me you're too blind to see",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry",
            "Never gonna say goodbye",
            "Never gonna tell a lie and hurt you",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry",
            "Never gonna say goodbye",
            "Never gonna tell a lie and hurt you",
            "Never gonna give, never gonna give",
            "(Give you up)",
            "(Ooh) Never gonna give, never gonna give",
            "(Give you up)",
            "We've known each other for so long",
            "Your heart's been aching but you're too shy to say it",
            "Inside we both know what's been going on",
            "We know the game and we're gonna play it",
            "I just wanna tell you how I'm feeling",
            "Gotta make you understand",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry",
            "Never gonna say goodbye",
            "Never gonna tell a lie and hurt you",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry",
            "Never gonna say goodbye",
            "Never gonna tell a lie and hurt you",
            "Never gonna give you up",
            "Never gonna let you down",
            "Never gonna run around and desert you",
            "Never gonna make you cry");

}
