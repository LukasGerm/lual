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
            //Change the innerhtml of the error div
            $('#errormsg').html('Username or Password wrong');
        }
    });
})