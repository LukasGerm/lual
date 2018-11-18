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
socket.write('2|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmYxNTFkNzA5ZjgzNTM2MTQ4NDY4ZjAiLCJwYXNzd29yZCI6ImQ3ODdhM2MwMzE3ZmQ3MGRlMDFkYTkyYjcxZDE4MDhiZjNkNGFlNzVmZjY2OTNkYWU5Mjg5YjVmZTk5OTdkMjRiZjJmMWQ0ODEwNTI2YjlmMTJlYTM4ZmZlN2ZkNTI2YjUzODExYmY4YjlkZjU2N2MyYWM5ZmExNzdhMDliMGQ4IiwiaWF0IjoxNTQyNTQ2NDc2fQ.MQFBLzCAGobIqr_Z4OMM66t6YVjKS-nvd4Ui11AcYO7Ko7RM_DZIV6r6DRzbLTHOsiyRQ51zeUzYRPKqCnyTCi32NDW--Jau2dIFdKKxYU5Fase8e__or5oL-6f0g5fMXiX0Lyb03B-JLUeEMX6RMelq96JmGWS73cAxVY00s_eWy9gOom2zq34hF3JsCaHwUMFxZ8qspBOuwBIxWos5FTic04Emij9tSZ_XwS9GbWg_hestv3vMqbrOxKG4_iY1rNxtmSAlykS9hW6Q5mth16AyxZrcUE-FvYNw3epYWCJte5PluW6JGo2c6xucjRDisE7HvSxABvcnTtHZ_XEdA_mBQBdW4nUda6_LPTiffnI679TXAKuaKP43-6Ic4g06C12Uvh0hxHFIWSKc1R4cUD7oFDbd-Q3OlEmQmZj4v2RgHBiYsb0yKXTPHfJw7-9bfYcghbGH4ckH2J347Y7ylYunN2s0Icdd41IVbVBnRvL3XaSsnT2-C3oY5aQDwpgjaFq42Ff9PoT2xChKaBzKLS-kaa5o0PUWkQNoSZvaQHY9RbCBMz4r7tJylLGATG-RS8EU0UNxc4rGRDxziHCv-D0iKyI7tQD_Mj5XaRduOveQwU950h3KkeT1N8f2a6aa7sbT6cCvdcFELq_1KMi6EhlsXavBIifa3cHyMOJKMis');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});