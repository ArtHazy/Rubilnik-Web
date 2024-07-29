package org.arthazy.rubilnik.users;

public class Host extends User {
    public Host(User user) {super(user);}

    public void nextQuestion(){
        if (room!=null){
            room.next();
        }
    }
    public void startRoom(){
        if (room!=null) room.start();
    }
    public void endRoom(){
        if (room!=null) room.end();
    }
}