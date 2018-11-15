const tls = require('tls');
const fs = require('fs');


//Server for the tls connections
class tlsServer{
    constructor(){
        //initialize the clients array the sockets will be pushed here
        this.clients = [];
    }
    run(){

    }
};

module.exports = tlsServer;