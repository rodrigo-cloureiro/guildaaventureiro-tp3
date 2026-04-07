package br.com.infnet.guildaaventureiro.exception.elastic;

public class ElasticsearchComunicacaoException extends RuntimeException {
    public ElasticsearchComunicacaoException(String message) {
        super(message);
    }
}
