package com.financebot.transactionservice.event.handler;

import com.financebot.contracts.event.TelegramCommandEvent;
import com.financebot.transactionservice.event.contract.EventHandler;
import com.financebot.transactionservice.event.publisher.TelegramEventPublisher;
import com.financebot.transactionservice.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelpEventHandler implements EventHandler {

    @Autowired
    private TelegramEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/ajuda";
    }

    @Override
    public void process(TelegramCommandEvent event, UserSession session) {
        String text = "Aqui estão os comandos disponíveis:\n\n"
                + "/despesa — registrar uma despesa\n"
                + "/receita — registrar uma receita\n"
                + "/relatorio — gerar relatório por período\n"
                + "/deltransacao — excluir uma transação\n"
                + "/cancelar — cancelar a operação atual\n\n"
                + "Em qualquer etapa, use /cancelar para voltar ao início.";
        publisher.send(event.chatId(), text);
    }
}
