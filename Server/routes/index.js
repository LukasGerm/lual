const express = require('express');
const router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});
//Authmethod, reached by ajax
router.post('/auth', (req,res,next) => {
  console.log("AUTH!");
});

module.exports = router;
