package dev.kush.bloggenius.models;

public class SlackModels {

    public record SlackSendMessage(String channel, String username, String text) {

        public static SlackSendMessage of(String text) {
            return new SlackSendMessage("#java", "BlogGenius", text);
        }
    }
}
