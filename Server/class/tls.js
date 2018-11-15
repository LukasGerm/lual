const tls = require('tls');



//Server for the tls connections
class tlsServer{
    constructor(key, cert){
        //initialize the clients array the sockets will be pushed here
        this.clients = [];
        //Tls certs
        this.options = {
            key: key,
            cert: cert
        }

    }
    run(port){
        this.server = tls.createServer(this.options, (socket) => {
            //Push the client in the client array
            //Here goes some authentication logic first
            //Only testing code
            this.clients.push(socket);
            socket.setEncoding('utf8');
            socket.write("welcome<EOF>\n");
        });

        try {
            this.server.listen(port, () => {});
        } catch (error) {   
            console.log("Connection closed to client");
        }
    }
};

module.exports = tlsServer;