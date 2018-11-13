const express = require('express');
const sha512 = require('js-sha512');
const router = express.Router();
const database = require('../class/database');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index');
});
//Authmethod, reached by ajax
router.post('/auth', (req,res,next) => {
  //get the user from the database
  let typedPassword = sha512(req.body.password);
  database.getUser(req.body.username, (user) => {
      //Check if the user exists or if its admin
      if(!user || !user.isAdmin){
        return res.sendStatus(401);
      }
      //If the passwords match
      if(typedPassword == user.password){
        //Set the session 
        req.session.login = true;
        //send the status
        return res.sendStatus(200);
      }
      else res.sendStatus(401);
  });

});
//Logout
router.get('/logout', (req,res,next) => {
  req.session.destroy();
  res.redirect('/');
})
module.exports = router;
