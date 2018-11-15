const createError = require('http-errors');
const express = require('express');
const path = require('path');
const tlsServer = require('./class/tls');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const indexRouter = require('./routes/index');
const adminRouter = require('./routes/admin');
const apiRouter = require('./routes/api');
const session = require('express-session');
const fs = require('fs');
//Tls server
const tls = new tlsServer(fs.readFileSync('./certs/tls/server-key.pem'), fs.readFileSync('./certs/tls/server-cert.pem'));
tls.run(8000);
const app = express();
app.use(session({
  secret: 'gqgrwhubvaoj09pgbqwuigh903ghiu0q34vqas293hndva',
  resave: true,
  cookie: {
    maxAge: 1200000
  },
  saveUninitialized: true
}));
const checkLogin = (req,res,next) => {
  //if the client is loggedin, go to the next middleware
  if(req.session.login) return next();
  else res.redirect('/');
}

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//Loginrouter
app.use('/', indexRouter);
//Admininterfacerouter
app.use('/a', checkLogin,adminRouter);
//Api calls
app.use('/api', checkLogin, apiRouter);
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
