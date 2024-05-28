import { limits } from '../values.mjs';
import { getSelf, putSelf } from "../functions.mjs"

export const ViewLogin = () => 
    getSelf()? window.location.href='/' :
    <div className='ViewLogin'>
        <div className='login-block'>
            <hstack>
                <div className='log accent'>LOG</div>
                <div className='in accent'>IN</div>     
            </hstack>
            <div>
                <input id="email-input" type="email" placeholder='email' maxLength={limits.maxEmailLength} />
                <div className='spacer-default'></div>
                <input id="password-input" type="password" placeholder='password' maxLength={limits.maxPassLength} />
            </div>
            <div className='spacer-default'></div>
            <button id='submit' onClick={() => {
                let submit = document.getElementById('submit');
                submit.hidden = true;
                let load = document.createElement('div')
                load.innerHTML = '/..'
                submit.after(load)

                let email = document.getElementById('email-input').value;
                let password = document.getElementById('password-input').value;

                putSelf({email,password}) //! FOR TESTING
                window.location.reload() //! FOR TESTING

                // fetch(SERVER_URL+'/user/verify', {
                //     method: 'POST',
                //     headers: { 'Content-Type': 'application/json' },
                //     body: JSON.stringify({
                //         email: email, 
                //         password: password
                //     })
                // }).then(res => {
                //     res.json().then(json => {
                //         res.ok ? (putSelf(json), window.location.href='/') : alert('failure')
                //     })
                // }).catch(e=>{
                //     alert(e.message)
                // }).finally(()=>{
                //     load.remove()
                //     submit.hidden = false
                // })
            }}>login</button>
            <div className='spacer-default'></div>
            <a href="/register">
                <small>register</small>
            </a>
            <div className="spacer-default"></div>
            <a href="/join">
                <small>join the game</small>
            </a>

        </div>
    </div>
