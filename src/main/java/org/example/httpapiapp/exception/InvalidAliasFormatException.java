package org.example.httpapiapp.exception;

public class InvalidAliasFormatException extends RuntimeException {

    public InvalidAliasFormatException(String alias) {
        super("Invalid alias format: " + alias);
    }
}