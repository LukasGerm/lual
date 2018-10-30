//Function for getting the logs from the server
//The filter argument is optional
//It is for the datepicker closing
function getLogs(filter, page){
    // if not set, use page 0
    page = !page ? 0 : page;
    $.get(`/api/getlogs?page=${page}&pageSize=2`, (data,status) => {
        $('.preloader-background').fadeIn('fast');
        $('.loader').fadeIn('fast');
        //Clear the div first
        document.getElementById('logs').innerHTML = '';
        let logs = !data ? [] : JSON.parse(data);
        logs.forEach(element => {
            //create the log elements
            if (filter){
                //Split it, because you dont want the time, only the date
                if(filter == element.date.split(' ')[0]){
                    document.getElementById('logs').innerHTML += '<tr><td>'+element.date+'</td><td>'+element.logString+'</td></tr>'
                }
            }
            else{
                document.getElementById('logs').innerHTML += '<tr><td>'+element.date+'</td><td>'+element.logString+'</td></tr>'
            }
        });
        //Fade out the preloader
        $('.preloader-background').fadeOut('fast');
        $('.loader').fadeOut('fast');
    });   
}
function getLogsCount(){
    $.get('/api/getlogscount', (data,status) => {
        const count = data.count;
        // TODO: Create pagination buttons
        console.log(`${count} items of type Log exist in database`);
    });   
}
//Function when the document is ready
document.addEventListener('DOMContentLoaded', function() {
    let sidenav = M.Sidenav.init(document.querySelectorAll('.sidenav'));
    let datepick = M.Datepicker.init(document.querySelectorAll('.datepicker'), {
         format: 'yyyy-mm-dd',
         //If you close the popup, the website gets the logs again but with a filter
         onClose(){
            const dateVal = $('#datepick');
            getLogs(dateVal.val());
         }
        });
    getLogs();
    getLogsCount();
  });
