SimpleSocket
============

Add socket listener to webMethods Integration Server

The SimpleSocket utility package for webMethods Integration Server has been designed to enable standard TCP server socket listener. SimpleSocket package integrates as component into webMethods Integration Server and provides additional port listener type with functionality of TCP socket interface.

Processing Service
This feature is implemented by this package and enables user to specify any service on Integration Server to be invoked when socket port receives data. This allows for easy integration without custom development. User can write a flow or Java services similar to those provided by this package SimpleSocket:receive and SimpleSocket.util:test to process incoming data from a socket input stream. These services will act as triggers on socket data and can rout processing to other services. The implementation plug-in class for processing service is provided by default with this package - com.net.server.protocol.ServiceProtocol. This class must be used as plug-in for the port in order to use processing service. The service invoke can only be preformed in synchronous mode. The Asynchronous execution is not available with this release.

For details, configuration and samples please use provided documentation in doc folder