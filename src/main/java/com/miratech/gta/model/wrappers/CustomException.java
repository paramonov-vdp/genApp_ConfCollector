package com.miratech.gta.model.wrappers;

/**
 * Class represent custom exception, to hide from users system exceptions.
 */
public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }
}
