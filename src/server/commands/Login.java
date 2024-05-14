package server.commands;

import com.ctc.wstx.shaded.msv.relaxng_datatype.Datatype;
import global.facility.Response;
import global.facility.Ticket;
import server.rulers.DatabaseRuler;

import java.sql.SQLException;

public class Login extends Command {
    private DatabaseRuler databaseRuler;

    public Login(DatabaseRuler databaseRuler){
        super("login" , "авторизоваться");
        this.databaseRuler = databaseRuler;
    }

    @Override
    public Response apply(String[] arguments , Ticket ticket , String login , String password){
        try{
            if(databaseRuler.checkUser(login , password)){
                return new Response("Вы успешно вошли в систему" , true);
            }else {
                return new Response("Непредвиденная ошибка во время авторизации пользователя");
            }
        }catch (SQLException e){
            return new Response("Не удалось авторизовать пользователя.Возможно вы указали неверный логин или пароль");
        }
    }
}
