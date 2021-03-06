/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batch.processing.usage.tasklet;

import br.com.sicoob.cro.cop.batch.configuration.annotation.Context;
import br.com.sicoob.cro.cop.batch.step.tasklet.AbstractTasklet;
import br.com.sicoob.cro.cop.batch.step.tasklet.TaskletContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author rogerioalves21
 */
public class ProcessarArquivoTasklet extends AbstractTasklet {

    private static final Logger LOG = Logger.getLogger(ProcessarArquivoTasklet.class.getName());

    @Context
    private TaskletContext context;

    @Override
    public void process() {
        List<OperacaoUsage> operacoes = new ArrayList<>();
        InputStream source = this.getClass().getResourceAsStream(this.context.getParameters().get("nomeArquivo").toString());
        try (Scanner scan = new Scanner(source)) {
            while (scan.hasNext()) {
                String[] dados = scan.next().split(";");
                OperacaoUsage operacao = new OperacaoUsage(dados[0],
                        new BigDecimal(dados[1]), dados[2], new BigDecimal(dados[3]));
                operacoes.add(operacao);
            }
            
            // calcula o percentual de provisionamento
            for (OperacaoUsage operacao : operacoes) {
                LOG.info(operacao.toString());
            }
        }
    }

}
