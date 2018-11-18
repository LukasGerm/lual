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
//socket.write('1|HelloWorld1|'+sha512("Test123!"));
socket.write('2|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmYxNjFmZmU5YjNlYjM4ZWNhNmFmOTkiLCJwYXNzd29yZCI6ImQ3ODdhM2MwMzE3ZmQ3MGRlMDFkYTkyYjcxZDE4MDhiZjNkNGFlNzVmZjY2OTNkYWU5Mjg5YjVmZTk5OTdkMjRiZjJmMWQ0ODEwNTI2YjlmMTJlYTM4ZmZlN2ZkNTI2YjUzODExYmY4YjlkZjU2N2MyYWM5ZmExNzdhMDliMGQ4IiwiZ3JvdXAiOiI1YmYxNjFmMWU5YjNlYjM4ZWNhNmFmOTciLCJmaXJzdE5hbWUiOiJUZXN0IiwibGFzdE5hbWUiOiJUZXN0Iiwicm9vbU51bWJlciI6IjEyMzQiLCJpYXQiOjE1NDI1NTUwNTB9.LPwa1GXJ45xU7MvoP2B6EzVNaN5lUkPi1wPiG31t8Gt40QFDTJbbWjW6MipFwv9V-1fCCduoMbfeYb1SCu_yN2GqT32fBp-Kw4gwRU4orgbF5836RDA9ffNZIHxqeIyEiJzA2e63GdtFY5cTtgoJZA5vm0nfhzQ7JRjphxT182Rz-zFQeMvW3oHl-FXkNH5HAwCsODnJEc3AZH8KwxBWbKKXvJC5tNmotDRZtBGxfMEo62uSKObMPtZapxVG3c_v_Het6jbbW-5RO1PO2XoS_H5ovdKW8oBFDjXpr9D7d9pWnXHLGjVr96kPwXv1d00L0Lpku4wHhYvnIjwjlGyNxjxQ0eu2sUqiJfeqgdgucxYcsUd8nhoet-eRQDN704NatBpLia5sSyuQ_ZcmVjZAp8IftolgTsZBFUnPSH6w4mjdpXIaWYWgTl0U_I3aacyfsPx2PQacH3deOw0TEKC_Nxhj5ZirzBbt41kMxcWi6fHlkS2VZBxzMKiJdNj8VIdKVesOvavHRxPsuDIvNmEzqIpWpEmk2TTH4vHs5tW623g_EpFA-xx2VUAcK6wTLy2nwYtLv2vRibcd-m6PZKMs99PC5H9bOOu_rmM25grGAS4dDc5XScLO09glooj3bTDZ33lRm7qQ6oXhipihQcAUXAqSoq5PbdwKXuoomPtFtO0');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});