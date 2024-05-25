import { SERVER_URL } from "./main";

export const LogOutButton = () => 
    <button id='logoutB' onClick={v=>{
        document.getElementById('logoutB').hidden = true
        let load = document.createElement('div')
        load.innerHTML = '...'

        document.getElementById('logoutB').after(load);


        let user = JSON.parse(localStorage.getItem('self-user') || '{}') 
        console.log(user);
        
        fetch(SERVER_URL+'/user', {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(user)
        }).then(res=>{
            if (res.ok){ 
                localStorage.removeItem('self-user'),
                window.location.href = '/'
            } else if (confirm('Failed to put data\nLeave without saving?')) {
                localStorage.removeItem('self-user')
            }
        }).catch(e => {
            if (confirm("Can't connect to the server\nLog out without saving?")) {
                localStorage.removeItem('self-user')
            }
        }).finally(() => {
            if (localStorage.getItem('self-user')) {
                document.getElementById('logoutB').hidden = false
                load.remove()
            } else {
                window.location.href = '/login'
            }
        })
    }} className='big'>log out</button>