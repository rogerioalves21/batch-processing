/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cro.cop.batch.service;

/**
 *
 * @author Rogerio Alves Rodrigues
 */
public interface BatchExecutorService {
    
    void shutdown();
    
    void execute(Runnable command);
    
}
