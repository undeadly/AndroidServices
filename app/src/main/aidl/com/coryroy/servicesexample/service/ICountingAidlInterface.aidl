// ICountingAidlInterface.aidl
package com.coryroy.servicesexample.service;

// Declare any non-default types here with import statements

interface ICountingAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void startCounting();

    void stopCounting();
}