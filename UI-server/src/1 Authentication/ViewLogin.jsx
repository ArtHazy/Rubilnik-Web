import { CORE_SERVER_URL, limits } from '../values.mjs';
import { getSelf, putSelf, validateSelfInDB } from "../functions.mjs"

export const ViewLogin = () => {

    if ( getSelf()?.id ) window.location.href='/'
    else return <div className='ViewLogin'>
        <div className='form'>
            <hstack><div className='log accent'>LOG</div><div className='in accent'>IN</div></hstack>
            <div className='form'>
                <input id="email-input" type="email" placeholder='email' maxLength={limits.maxEmailLength} />
                <input id="password-input" type="password" placeholder='password' maxLength={limits.maxPassLength} />
                <button id='submit' className='big' onClick={() => {
                    let submit = document.getElementById('submit');
                    submit.hidden = true;
                    let load = document.createElement('div')
                    load.innerHTML = '/..'
                    submit.after(load)

                    let email = document.getElementById('email-input').value;
                    let password = document.getElementById('password-input').value;

                    fetch(CORE_SERVER_URL+'/user/verify', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({email, password})
                    })
                    .then(res => { res.json().then(json => { res.ok ? (putSelf(json), window.location.href='/') : alert('failure') }) })
                    .catch(e=>{ alert(e.message) })
                    .finally(()=>{ load.remove(), submit.hidden = false })
                }}>login</button>
            </div>


            <div className='grid'>
                <a href="/join"><small>join the game</small></a>
                {' | '}
                <a href="/register"><small>register</small></a>
            </div>

        </div>
    </div>
}