package com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions;

public class SearchError extends RuntimeException{

    public SearchError() {
    }

    public SearchError(String message) {
        super(message);
    }
}
