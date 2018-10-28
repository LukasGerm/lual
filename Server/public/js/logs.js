$(document).ready(function(){
    $('.sidenav').sidenav();
    //Getrequest to get the logs
    $.get('/api/getlogs', (data,status) => {
        console.log(1);
        $('.preloader-background').fadeOut('slow');
        $('.loader').fadeOut('slow');
        //Code goes here
        let logs = JSON.parse(data);
        logs.forEach(element => {
            //create the log
            document.getElementById('logs').innerHTML += '<tr><td>'+element.date+'</td><td>'+element.logString+'</td></tr>'
        });
        
    });
  });