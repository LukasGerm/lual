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
//socket.write('1|Lukas|'+sha512("Test"));
socket.write('4|eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YmQ0Nzc3YzIxMGJkZDM2YmM2ZTE2OTIiLCJwYXNzd29yZCI6ImM2ZWU5ZTMzY2Y1YzY3MTVhMWQxNDhmZDczZjczMTg4ODRiNDFhZGNiOTE2MDIxZTJiYzBlODAwYTVjNWRkOTdmNTE0MjE3OGY2YWU4OGM4ZmRkOThlMWFmYjBjZTRjOGQyYzU0YjVmMzdiMzBiN2RhMTk5N2JiMzNiMGI4YTMxIiwiZ3JvdXAiOiI1YmQ5YzViY2RmZWI1ODMyMDBiMDcxNTQiLCJmaXJzdE5hbWUiOiJMdWthcyIsImxhc3ROYW1lIjoiR2VybWVyb3R0Iiwicm9vbU51bWJlciI6IjEuMTIiLCJpYXQiOjE1NDI1NTUxMTJ9.JhfJSYsFv2P6-hLzobct1GqoElQYNJBDPzkSZ-2_k4-4nN5W5xYopV-QxcmUDR3FlxtYpVFDGMy_Qa-TwZC5cCRcaslWcF-whBA6ZYc3yXzZ73bq3Z-OjSgnlnisiEwnfdfzHlZgcjUpR8_ObI3DlQPRwU51NvcmtVxPMiC2sOQa_eqMtUJV-vuLwrrKhDid5MMKGiZsDOcfVWJYuV6B6ixElmEVdNHaNr-ae_2GP3ivfTZkELSt5DiR0nE3YK4AFhfJkMqDA56bI3H3kA1uATUeRlqwPOWqaZ5JRtw9j2tNqel61Nfau0AJBiXpwXyM-kgbEje-Y9rLwbm7pZPu-bMv1jvkXCKnlowYZa41Ju5mFPIO7HTrBXs4h5a0LmdaR4oYRHY_M-1pdxX5HPFhdJvovqkVW5Q3rJ7slYHHokKzfMT6qf_VCQ5f729-g-EzuDosNJaR_Cbjey2vPUn7doXBKFHWzyCE4G9kFR-230pQl_Z2nlnH9XvLkjIVODwyyhmQEV8VN88UYv2vbrgrKoK_NqiVEtYtGC7ZmOw3JUtEVGPM0ZbiZS7GDv8OGCQF8iZzcHF4VIOWANWIYKMc_25sUcPkE3GaGhF0UKnQ0G0plPAl7lpoF4pI5WZbx79-7MiZKKMI8S-ZJwKAukk0lM7a8eMr9VXjwgmASlNlrMI');
socket.on('data', (data) => {
  console.log(data);
});

socket.on('end', () => {
  console.log('Ended')
});