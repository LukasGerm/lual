const tls = require('tls');
const fs = require('fs');
const sha512 = require('js-sha512');

const options = {
  ca: [ fs.readFileSync('../certs/tls/server-cert.pem') ]
};

var socket = tls.connect(8000, 'localhost', options, () => {
  console.log('client connected',
              socket.authorized ? 'authorized' : 'unauthorized');
  process.stdin.resume();
});
socket.setEncoding('utf8');
socket.write('3|Test123|'+sha512("HalloWelt"));
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});