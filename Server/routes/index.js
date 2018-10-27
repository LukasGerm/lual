const express = require('express');
const sha512 = require('js-sha512');
const router = express.Router();
const database = require('../class/database');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});
//Authmethod, reached by ajax
router.post('/auth', (req,res,next) => {
  //get the user from the database
  let user = database.getUser(req.body.username);
  let typedPassword = sha512(req.body.password);
  //Check if the user exists or if its admin
  console.log(user);
  console.log(user.isAdmin);
  if(!user || !user.isAdmin){
    return res.sendStatus(401);
  }
  //If the passwords match
  if(typedPassword = user.password){
    //Set the session 
    req.session.login = true;
    //send the status
    return res.sendStatus(200);
  }
});

module.exports = router;
