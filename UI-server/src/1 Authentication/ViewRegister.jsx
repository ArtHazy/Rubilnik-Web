import { CORE_SERVER_URL, limits } from '../values.mjs';
import { getSelf, putSelf, validateSelf } from "../functions.mjs"

export const ViewRegister = () => 
    validateSelf(getSelf())? window.location.href='/' :
        <div className='ViewRegister'>
            <div className="register-block">
                
                <hstack>
                    <div className='reg accent'>REG</div><div className='ister accent'>ISTER</div>
                </hstack>

                <div>
                    <input id="username-input" type="text" placeholder='username' maxLength={limits.maxNameLength} />
                    <div className='spacer-default'></div>
                    <input id="email-input" type="email" placeholder='email' maxLength={limits.maxEmailLength} />
                    <div className='spacer-default'></div>
                    <input id="password-input" type="password" placeholder='password' maxLength={limits.maxPassLength} />
                </div>

                <div className="spacer-default"/>
                <button id='submit' onClick={() => {
                    let submit = document.getElementById('submit');
                    submit.hidden = true;
                    let load = document.createElement('div');
                    load.innerHTML = '/..'
                    submit.after(load)

                    let username = document.getElementById('username-input').value;
                    let password = document.getElementById('password-input').value;
                    let email = document.getElementById('email-input').value;
                    
                    putSelf(JSON({'quizzes':[]})), window.location.href='/'
                }}>Register</button>
                <div className='spacer-default'/>

                <a href="/login"><small>Login</small></a>

            </div>
        </div>
