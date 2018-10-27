const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const indexRouter = require('./routes/index');
const database = require(__dirname + '/class/database');
const session = require('express-session');
const admin = require('./routes/admin');
const app = express();
app.use(session({
  secret: 'gqgrwhubvaoj09pgbqwuigh903ghiu0q34vqas293hndva',
  resave: false,
  cookie: {
    maxAge: 1200000
  },
  saveUninitialized: true
}));

function checkLogin(){
  //Here comes the authentication function to check, if the user is loggedin
  next();
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
app.use('/a', checkLogin,admin)
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
