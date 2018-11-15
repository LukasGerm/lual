const tls = require("tls");

//Server for the tls connections
class tlsServer {
  constructor(key, cert) {
    //initialize the clients array the sockets will be pushed here
    this.clients = [];
    //Tls certs
    this.options = {
      key: key,
      cert: cert
    };
  }
  run(port) {
    let server;
    server = tls.createServer(this.options, socket => {
      //Push the client in the client array
      //Here goes some authentication logic first
      //Only testing code
      this.clients.push(socket);
      socket.setEncoding("utf8");
      socket.write("welcome<EOF>\n");
      console.log(this.clients.length);

      //If an error occurs
      socket.on('error', (e) => {
          console.log("Connection reset");
          for (let index = 0; index < this.clients.length; index++) {
              if(socket === this.clients[index]){
                  this.clients.splice(index,1);
              }
          }
          //Destroy the socket
          socket.destroy();
          console.log(this.clients.length);
          console.log(this.clients);
      })
    });
    server.listen(port, () => {});
  }
}

module.exports = tlsServer;
