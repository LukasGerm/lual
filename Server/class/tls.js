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
          //Destroy the socket
          socket.destroy();
          //Delete it from the array
          this.clients.forEach(client => {
              if(client === socket){
                  client = null;
              }
          })
          console.log(socket);
      })
    });
    server.listen(port, () => {});
  }
}

module.exports = tlsServer;
