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
socket.write('2|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmYxNjFmZmU5YjNlYjM4ZWNhNmFmOTkiLCJwYXNzd29yZCI6ImQ3ODdhM2MwMzE3ZmQ3MGRlMDFkYTkyYjcxZDE4MDhiZjNkNGFlNzVmZjY2OTNkYWU5Mjg5YjVmZTk5OTdkMjRiZjJmMWQ0ODEwNTI2YjlmMTJlYTM4ZmZlN2ZkNTI2YjUzODExYmY4YjlkZjU2N2MyYWM5ZmExNzdhMDliMGQ4IiwiaWF0IjoxNTQyNTQ2MzM3fQ.nok0PHvk1jhqc0PC7YqvBy-0dHbe6MErSly9GPYDxF_Z7KtDZ9E6tJT1oy022OtBHNAVs3MbkCBm6ysLoiyMnECVYYYDycNfUjLLr1gpjfXOuAkiULJbJbYc3k5PvwBUTg1fN0qS2iD2cCQM5PXySLzgZUVmV5vyovhlvIOoeQNATFlXeP__7nTJRHkoZDalE6O5F7SqAX4PDMyc73HiC0B0xZ0MbTveM_IixuZpRA-eTy3VGT7hIkHy35KHDNZLmoWThyfqa8RG50_hvliUJgqoNxlJOtyxQAiRNIhHyoM14NtjyX2PVcU8CracJamoFPt6WWsdv-LC0bUa2qZjSv8b1WTCUAL8l6MwYaN2h8yos1swmaAU8LUpuYhHZSkBd6SZc3Td3KYk4pwunKJWsAnJ9qb7Ba6T1yMnlP1sTrO740gGYGkbTKyciu5kGm-LXeCo_TQzA5DO0QP-E7sPDT8nOEfU4eubKRVK8Yf2uwLDhQH4lNIzGG0vVj_oXvObT49fIzz5_9Ey4_x-_VVeByk-X8QiCmM4L5lDWFvuPjw-zLwJWaP-tsG4lqMVpPsN-7Jif6h7riNJAxDmUH5hTUZdB7v5jl8dXWHJ3X49GEwqXf_ES9bHHlK9RXW40qxQzuHa-dCcmAkOWqLpR5hN8EN6eDRjSBnrevreSRv4eFA');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});