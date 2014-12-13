/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sicoob.cro.cop.batch.step.chunk;

import br.com.sicoob.cro.cop.batch.configuration.ItemProcessorInjector;
import br.com.sicoob.cro.cop.batch.configuration.ItemReaderInjector;
import br.com.sicoob.cro.cop.batch.configuration.ItemWriterInjector;
import br.com.sicoob.cro.cop.batch.core.ItemProcessor;
import br.com.sicoob.cro.cop.batch.core.ItemReader;
import br.com.sicoob.cro.cop.batch.core.ItemWriter;
import br.com.sicoob.cro.cop.batch.core.Result;
import br.com.sicoob.cro.cop.batch.step.Step;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.ConstructorUtils;

/**
 * Esta classe eh reponsavel por executar no estilo chunk o processo.
 *
 * @author Rogerio Alves Rodrigues
 */
public class ChunkExecutor implements IChunkExecutor {

    public Step step;

    /**
     * Construtor.
     */
    public ChunkExecutor() {

    }

    public void setStep(Step step) {
        this.step = step;
    }

    /**
     * Chama o processo terceirizado.
     *
     * @return o resultado do processo.
     * @throws Exception ao ocorrer algum erro.
     */
    public Result execute() throws Exception {
        return this.call();

    }

    /**
     * Executa o ItemReader, ItemProcessor e ItemWriter
     *
     * @return um Resultado.
     * @throws Exception ao ocorrer algum erro
     */
    public Result call() throws Exception {
        // cria uma lista de items lidos
        List<Object> items = new ArrayList();

        // faz a iteracao de dados do ItemReader
        Integer recordNumber = 0;

        // injeta as dependencias
        injectDependencies();

        // obtem os itens
        ItemReader reader = this.step.getReader();
        ItemProcessor processor = this.step.getProcessor();
        ItemWriter writer = this.step.getWriter();

        Object item = reader.readItem(recordNumber);
        if (item != null) {
            item = processor.processItem(item);
        }
        Boolean keepRunning = item != null;

        // logica para rodar enquanto houver itens para processar
        if (keepRunning) {
            items.add(item);
            while (keepRunning) {
                recordNumber++;
                Object itemId = reader.readItem(recordNumber);
                keepRunning = itemId != null;
                if (itemId != null) {
                    itemId = processor.processItem(itemId);
                    items.add(itemId);
                }
            }
        }

        // chama o item writer
        callWriter(writer, items);
        return Result.SUCCESS;
    }

    /**
     * Chama o writer e passa a lista para a escrita.
     *
     * @param writer Objeto writer.
     * @param items Lista de dados.
     */
    private void callWriter(ItemWriter writer, List<Object> items) {
        writer.writeItems(items);
    }

    /**
     * Injeta as dependencias do reader, processor e writer.
     *
     * @throws Exception para algum erro.
     */
    private void injectDependencies() throws Exception {
        ConstructorUtils.invokeConstructor(ItemReaderInjector.class, this.step).inject();
        ConstructorUtils.invokeConstructor(ItemProcessorInjector.class, this.step).inject();
        ConstructorUtils.invokeConstructor(ItemWriterInjector.class, this.step).inject();
    }

}