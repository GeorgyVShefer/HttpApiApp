package org.example.httpapiapp.exception;

public class ReservedAliasException extends RuntimeException {

    public ReservedAliasException(String alias) {
        super("Alias is reserved: " + alias);
    }
}
