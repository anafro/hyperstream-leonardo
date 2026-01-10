package ru.anafro.hyperstream.leonardo.metadata;

public class HashcodeProfilePictureIdConverter extends ProfilePictureIdConverter {
    @Override
    public int convertUsernameToProfilePictureId(String username) {
        return username.hashCode();
    }
}
