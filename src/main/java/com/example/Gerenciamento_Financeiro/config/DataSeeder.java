package com.example.Gerenciamento_Financeiro.config;

import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final Logger logger = Logger.getLogger(DataSeeder.class.getName());

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (contaRepository.count() == 0 && categoriaRepository.count() == 0 && transacaoRepository.count() == 0) {
            logger.info("Iniciando seeding de dados...");
            seedContas();
            seedCategorias();
            seedTransacoes();
            logger.info("Seeding concluído.");
        } else {
            logger.info("Dados já existentes. Seeder ignorado.");
        }
    }

    private void seedContas() {
        Conta c1 = new Conta();
        c1.setNomeBanco("C3");
        c1.setNomeTitular("João Silva");
        c1.setNumeroConta(123456);
        c1.setEmail("joaos@gmail.com");
        c1.setSenha("senha123"); // em produção não salvar senhas em texto
        c1.setSaldo(new BigDecimal("1500.00"));

        Conta c2 = new Conta();
        c2.setNomeBanco("Banco do Brasil");
        c2.setNomeTitular("Maria Oliveira");
        c2.setNumeroConta(654321);
        c2.setEmail("maria@example.com");
        c2.setSenha("senha456");
        c2.setSaldo(new BigDecimal("3200.50"));

        Conta c3 = new Conta();
        c3.setNomeBanco("Itau");
        c3.setNomeTitular("Carlos Pereira");
        c3.setNumeroConta(112233);
        c3.setEmail("carlos@gmail.com");
        c3.setSenha("senha789");
        c3.setSaldo(new BigDecimal("2750.75"));

        contaRepository.saveAll(Arrays.asList(c1, c2, c3));
    }

    private void seedTransacoes() {
        // busca algumas entidades já salvas
        List<Conta> contas = contaRepository.findAll();
        List<Categoria> categorias = categoriaRepository.findAll();
        if (contas.isEmpty() || categorias.isEmpty()) {
            logger.warning("Contas ou categorias ausentes — não será possível criar transações.");
            return;
        }

        Conta c1 = contas.get(0);
        Conta c2 = contas.get(1);

        Transacao t1 = new Transacao();
        t1.setDescricao("Compra mercado");
        t1.setValor(new BigDecimal("200.75"));
        t1.setData(LocalDate.now().minusDays(3));
        t1.setTipo(Tipo.DESPESA);
        t1.setConta(c1);
        t1.setCategorias(Arrays.asList(categorias.get(0))); // Alimentação

        Transacao t2 = new Transacao();
        t2.setDescricao("Recarga transporte");
        t2.setValor(new BigDecimal("50.00"));
        t2.setData(LocalDate.now().minusDays(1));
        t2.setTipo(Tipo.DESPESA);
        t2.setConta(c1);
        t2.setCategorias(Arrays.asList(categorias.get(1))); // Transporte

        Transacao t3 = new Transacao();
        t3.setDescricao("Recebimento salário");
        t3.setValor(new BigDecimal("3000.00"));
        t3.setData(LocalDate.now().withDayOfMonth(1));
        t3.setTipo(Tipo.RECEITA);
        t3.setConta(c1);
        t3.setCategorias(Arrays.asList(categorias.get(2))); // Salário

        Transacao t4 = new Transacao();
        t4.setDescricao("Jantar fora");
        t4.setValor(new BigDecimal("120.00"));
        t4.setData(LocalDate.now().minusDays(2));
        t4.setTipo(Tipo.DESPESA);
        t4.setConta(c2);
        t4.setCategorias(Arrays.asList(categorias.get(0))); // Alimentação

        transacaoRepository.saveAll(Arrays.asList(t1, t2, t3, t4));
    }

    private void seedCategorias() {
        Categoria cat1 = new Categoria();
        cat1.setNomeCategoria("Alimentação");
        cat1.setCor(Cor.VERMELHO);

        Categoria cat2 = new Categoria();
        cat2.setNomeCategoria("Transporte");
        cat2.setCor(Cor.AMARELO);

        Categoria cat3 = new Categoria();
        cat3.setNomeCategoria("Salário");
        cat3.setCor(Cor.VERDE);

        categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3));
    }
}
