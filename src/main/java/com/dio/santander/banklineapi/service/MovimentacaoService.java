package com.dio.santander.banklineapi.service;

import com.dio.santander.banklineapi.dto.NovaMovimentacao;
import com.dio.santander.banklineapi.model.Correntista;
import com.dio.santander.banklineapi.model.Movimentacao;
import com.dio.santander.banklineapi.model.MovimentacaoTipo;
import com.dio.santander.banklineapi.repository.CorrentistaRepository;
import com.dio.santander.banklineapi.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository Movimentacaorepository;

    @Autowired
    private CorrentistaRepository correntistaRepository;

    public void save (NovaMovimentacao novaMovimentacao){
        Movimentacao movimentacao = new Movimentacao();
        Double valor;

        valor = novaMovimentacao.getTipo() == MovimentacaoTipo.RECEITA
                ? novaMovimentacao.getValor() : novaMovimentacao.getValor() * -1;
        movimentacao.setValor(valor);
        movimentacao.setIdConta(novaMovimentacao.getIdConta());
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipo(novaMovimentacao.getTipo());
        movimentacao.setDescricao(novaMovimentacao.getDescricao());

        Correntista correntista = correntistaRepository.findById(novaMovimentacao.getIdConta()).orElse(null);
        if (correntista != null){
            correntista.getConta().setSaldo(correntista.getConta().getSaldo() + valor);
            correntistaRepository.save(correntista);
        }
        
        Movimentacaorepository.save(movimentacao);
    }
}
