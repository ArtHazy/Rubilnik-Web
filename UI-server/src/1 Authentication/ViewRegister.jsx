import { CORE_SERVER_URL, limits } from '../values.mjs';
import { getSelf, putSelf, validateSelfInDB } from "../functions.mjs"

export const ViewRegister = () => {

    if ( getSelf()?.id ) window.location.href='/'
    else return <div className='ViewRegister'>
        <div className="form">
            
            <hstack style={{gap:0}} ><div className='reg accent'>REG</div><div className='ister accent'>ISTER</div></hstack>

            <div className='form'>
                <input id="username-input" type="text" placeholder='username' maxLength={limits.maxNameLength} />
                <input id="email-input" type="email" placeholder='email' maxLength={limits.maxEmailLength} />
                <input id="password-input" type="password" placeholder='password' maxLength={limits.maxPassLength} />
                <button id='submit' onClick={() => {
                    let submit = document.getElementById('submit');
                    submit.hidden = true;
                    let load = document.createElement('div');
                    load.innerHTML = '/..'
                    submit.after(load)

                    let username = document.getElementById('username-input').value;
                    let password = document.getElementById('password-input').value;
                    let email = document.getElementById('email-input').value;
                    
                    fetch(CORE_SERVER_URL+'/user', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ name: username, email, password })
                    })
                    .then(res => { res.json().then(json =>{ console.log(json); res.ok ? ( putSelf(json), window.location.href='/', alert ('registered')) : alert('error')}) })
                    .catch(e => { alert(e.message), load.remove(), submit.hidden = false })

                }}>Register</button>
            </div>
            <a href="/login"><small>Login</small></a>
        </div>
    </div>        
}
