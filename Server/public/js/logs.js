$(document).ready(function(){
    $('.sidenav').sidenav();
    //Getrequest to get the logs
    $.get('/api/getlogs', (data,status) => {
        let logs = JSON.parse(data);
        logs.forEach(element => {
            //create the log elements
            document.getElementById('logs').innerHTML += '<tr><td>'+element.date+'</td><td>'+element.logString+'</td></tr>'
        });
        $('.preloader-background').fadeOut('slow');
        $('.loader').fadeOut('slow');
  
    });
  });