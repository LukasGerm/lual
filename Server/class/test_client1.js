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
//socket.write('1|Test123|'+sha512("Test123!"));
socket.write('2|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmYxNTFkNzA5ZjgzNTM2MTQ4NDY4ZjAiLCJwYXNzd29yZCI6ImQ3ODdhM2MwMzE3ZmQ3MGRlMDFkYTkyYjcxZDE4MDhiZjNkNGFlNzVmZjY2OTNkYWU5Mjg5YjVmZTk5OTdkMjRiZjJmMWQ0ODEwNTI2YjlmMTJlYTM4ZmZlN2ZkNTI2YjUzODExYmY4YjlkZjU2N2MyYWM5ZmExNzdhMDliMGQ4IiwiZ3JvdXAiOiI1YmQ5YzViY2RmZWI1ODMyMDBiMDcxNTQiLCJmaXJzdE5hbWUiOiJUZXN0Rmlyc3QiLCJsYXN0TmFtZSI6IlRlc3RMYXN0Iiwicm9vbU51bWJlciI6IjEyMyIsImlhdCI6MTU0MjU1NDk2MX0.S9Iy2tnMfNsdiipBuZGSJG8dpZeb06KkqOT5zk3mgVwL-EqMoFT2o6vf0AA8w6iYjiRovpso_1PCH0LzH9waXxHdE0eWYma4NQp_1Fe1fcYq41AmQHwgflC8pnv_H2hMbWnWmAOvTSNpS9nRxuyC0HPCK-cW7BKKeBYMhydTZJnkzqXFM0-tFSrpcAzeMD9k9Oc7H-PGIw0TEmsk8w3OvwpYQJAHDtzem8vKsUu4qPocB42kDbrcIv-U6t3Ztfq89Goh3aMsbBRyCfcfVsngiX1g07Qkdxk_p4Baz2Q82mDpz_JSTH7EMB0WyX7w9p3-FvI-xtd3iQTcsQSxmt8XpSH505UB0pbmDAt_jYUA32mGuATK6gm5s92pQvJzvbXJnyq3Q56Os441wCSZy2lUNjbi2ckafL2a1G5gMTiYugjOQM8vFpgZ4MyP98UJtEdPO2qVgMK0oDnmk3kRAxle2iYH8byOEU3STAf8rh_g_dqW4UwnX-3KEWIplKTHDUhuGtrSPax7oHTeb7wPr6gmapOd1aFBwSTAQMT4U2JkgT-uXBN3lripg4RRUqMFhBIWS1T-uZZ3O-UYuK6kmqZ91aTemB3gy-kn9xWU2d-TemasSzPj1X2Pff5VZGvFUTk_HVEV_1dhVkX3E6knCptFnGWjUKWnYIS-wKbyMnjtOaQ');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});