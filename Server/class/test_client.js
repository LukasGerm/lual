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
socket.write('4|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmYxNTFkNzA5ZjgzNTM2MTQ4NDY4ZjAiLCJwYXNzd29yZCI6ImQ3ODdhM2MwMzE3ZmQ3MGRlMDFkYTkyYjcxZDE4MDhiZjNkNGFlNzVmZjY2OTNkYWU5Mjg5YjVmZTk5OTdkMjRiZjJmMWQ0ODEwNTI2YjlmMTJlYTM4ZmZlN2ZkNTI2YjUzODExYmY4YjlkZjU2N2MyYWM5ZmExNzdhMDliMGQ4IiwiaWF0IjoxNTQyNTQyNzM5fQ.c9-ZwZdvSjO3iN7uLsLMQEpbGog6w0nwPxme07WmvN5vQsnxlQztJeV3KB-O0w_Yb3hD7Ebomiz6UZWOiQDUJVwQ31P42GIOZHOJUYxFtWUuiv2w4yTJ8Ym7H4QPmPaCp4ZustEm4ff8o4fZT5oau9A97qcJzn5mycp6PTvf8-OPWet2Y_9eIT4pl6dRAVvERJ8fXKJBFuIJ7SVWyyli-w8jpYz3Z9UU8wus2BzumoyXEm1fQQ3zq2qWPjuJmjy-KOSywZRxqYFmrYem0wRzTrPZzTvLiqYE9KHQ5ZcJ9YMCG5-UkS1dW61TSNpcgGR76zta7xYnYJ9bb6ouhFScGcXiJ6WkDsStTj3VbJfzS0rt4WaIYynjD7hUiLC4kMq0VbrIaP9kzs4p3QQMlkAwwpQYD6DZIJJwIl87KTLgL9RTdoyj4ksnXU55rCJZ-8cWV-AORsUE4sRSBCXfYl8T-M62KrXS7bT0QjqJiGz9bqs-Y4yNrqiSkI7Uy3h_JXNC6uQ7ME4eK6XCHB83AiPpX6ysS0r6TQC5rLob8Tlxb3GdS_lKpYNIKbKJ6vgPrFRCEZyhMp_2SfKuJ_K8Ayf4FRu3QBa2-7i_XZtkkBuMZG9jnvdL9ScVqM1mPNHFjNpxePzG1HUp_FOjwH7gmdUxDKLG-HO8V5pWqGoRRdXyYu8');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});