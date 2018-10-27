$('#loginForm').submit(e => {
    //Dont perform the action, prevent default
    e.preventDefault();
    //Define the data which gets send via ajax
    const data = {
        username: $('#username').val(),
        password: $('#password').val()
    }
    //Perform the ajax request
    $.ajax({
        type: 'POST',
        url: '/auth',
        data: data,
        success(){
            window.location.href = '/a';
        },
        error(){
            //Error function goes here
        }
    });
})