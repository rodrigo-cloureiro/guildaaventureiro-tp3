package br.com.infnet.guildaaventureiro.exception.operacoes;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
