let currentLogPage = 0;
let logFilter = null;
let logPageCount = 0;
const logPageSize = 10;

//Function for getting the logs from the server
//The filter argument is optional
//It is for the datepicker closing
function getLogs(){
    // set optional date filter in get parameter
    const dateParam = logFilter ? `&date=${logFilter}` : '';
    $.get(`/api/getlogs?page=${currentLogPage}&pageSize=${logPageSize}${dateParam}`, (data,status) => {
        $('.preloader-background').fadeIn('fast');
        $('.loader').fadeIn('fast');
        //Clear the div first
        document.getElementById('logs').innerHTML = '';
        let logs = !data ? [] : JSON.parse(data);
        logs.forEach(element => {
            //create the log elements
            if (logFilter){
                //Split it, because you dont want the time, only the date
                if(logFilter == element.date.split(' ')[0]){
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
function setPage(page) {
    currentLogPage = page;
    getLogs();
    rebuildPaginationButtons();
}
function setPageCount(){
    // set optional date filter in get param
    const dateParam = logFilter ? `?date=${logFilter}` : '';
    $.get('/api/getlogscount' + dateParam, (data,status) => {
        const count = data.count;
        logPageCount = getPageCount(count);
        rebuildPaginationButtons();
        console.log(currentLogPage, logPageCount);
    });   
}
function rebuildPaginationButtons() {
    // there is no previous page if we are on pageindex 0
    const leftButtonDisabled = currentLogPage < 1;
    const rightButtonDisabled = currentLogPage === Math.floor(logPageCount);

    let paginationButtons = '';
    if (leftButtonDisabled) {
        paginationButtons += '<li class="disabled"><a><i class="material-icons">chevron_left</i></a></li>';
    } else {
        paginationButtons += '<li class="waves-effect"><a onclick="setPage(' + (currentLogPage - 1) + ')"><i class="material-icons">chevron_left</i></a></li>';
    }
    if (rightButtonDisabled) {
        paginationButtons += '<li class="disabled"><a><i class="material-icons">chevron_right</i></a></li>';
    } else {
        paginationButtons += '<li class="waves-effect"><a onclick="setPage(' + (currentLogPage + 1) + ')"><i class="material-icons">chevron_right</i></a></li>';
    }
    document.getElementById('pagination-container').innerHTML = paginationButtons;
}
function getPageCount(logCount) {
    if (!logCount || logCount < 1) {
        return 0;
    }
    const pageCount = logCount / logPageSize;
    return pageCount > 0 ? pageCount : 1;
}
function showAllLogs(){
    logFilter = null;
    currentLogPage = 0;
    $('#datepick').val('');
    getLogs();
}
//Function when the document is ready
document.addEventListener('DOMContentLoaded', function() {
    let sidenav = M.Sidenav.init(document.querySelectorAll('.sidenav'));
    let datepick = M.Datepicker.init(document.querySelectorAll('.datepicker'), {
         format: 'yyyy-mm-dd',
         //If you close the popup, the website gets the logs again but with a filter
         onClose(){
            const dateVal = $('#datepick');
            logFilter = dateVal.val();
            // after picking the date, we have to reload the pagecount
            setPageCount();
            getLogs();
         }
        });
    getLogs();
    setPageCount();
  });
