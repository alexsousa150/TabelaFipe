package br.com.alura.TabelaFipe.Service;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String Json, Class<T> classe);
    <T> List<T> obterLista(String json, Class<T> classe);
}
