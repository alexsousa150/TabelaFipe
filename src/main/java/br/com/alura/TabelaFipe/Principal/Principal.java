package br.com.alura.TabelaFipe.Principal;

import br.com.alura.TabelaFipe.Model.Dados;
import br.com.alura.TabelaFipe.Model.Modelos;
import br.com.alura.TabelaFipe.Model.Veiculos;
import br.com.alura.TabelaFipe.Service.ConsumoApi;
import br.com.alura.TabelaFipe.Service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();
    private final String URL_Base = "https://parallelum.com.br/fipe/api/v1/";
    public void exibeMenu() {
        var menu = """
                *** Opções ***
                - Carros
                - Motos
                - Caminhão  
                
                Digite uma das opções para consulta:
                """;
        System.out.println(menu);
        var opcao = leitura.nextLine();
        String endereco;

        if(opcao.toLowerCase().contains("carr")){
            endereco = URL_Base + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_Base +"motos/marcas";
        } else {
            endereco = URL_Base +"caminhoes/marcas";
        }
         var json = consumoApi.obterDados(endereco);
        System.out.println(json);

        //Lista de marcas
         var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consultar...");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumoApi.obterDados(endereco);

        //Lista de modelos da marca selecionada
        var modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //Pesquisar carro especifico
        System.out.println("\nQual carro está buscando? ");
        var nomeCarro = leitura.nextLine();
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeCarro.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados");
        modelosFiltrados.forEach(System.out::println);

        //Buscando codigo do carro para avaliação por ano
        System.out.println("Qual código de busca para avaliação da Fipe...");
        var codigoModelo = leitura.nextLine();
        endereco = endereco + "/" + codigoModelo + "/anos";
        json= consumoApi.obterDados(endereco);

        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculos> veiculos= new ArrayList<>();

        for (int i = 0; i < anos.size(); i++){
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculos veiculo = conversor.obterDados(json, Veiculos.class);
            veiculos.add(veiculo);
        }
        System.out.println("\nTodos veiculos filtrados por ano");
        veiculos.forEach(System.out::println);

    }
}
