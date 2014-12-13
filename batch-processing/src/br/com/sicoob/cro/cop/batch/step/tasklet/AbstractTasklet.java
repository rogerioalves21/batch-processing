/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sicoob.cro.cop.batch.step.tasklet;

import br.com.sicoob.cro.cop.batch.core.Result;

/**
 * Classe abstrata para quando o cliente ano quiser implementar a interface
 * tasklet.
 *
 * @author Rogerio Alves Rodrigues
 */
public abstract class AbstractTasklet implements Tasklet {

    public Boolean call() throws Exception {
        this.execute();
        return Boolean.TRUE;
    }
    
    public abstract void execute() throws Exception;

}
