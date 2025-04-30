package br.ufpb.dcx.apps4society.meuguiapbapi.exception;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
